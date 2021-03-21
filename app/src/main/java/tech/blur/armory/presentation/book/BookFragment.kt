package tech.blur.armory.presentation.book

import android.Manifest
import android.accounts.AccountManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.soywiz.klock.*
import com.soywiz.klock.locale.russian
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tech.blur.armory.common.TrueTime
import tech.blur.armory.databinding.FragmentBookBinding
import tech.blur.armory.databinding.ItemBookEmailBinding
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.common.ErrorPopup
import tech.blur.armory.presentation.common.observeNonNull
import tech.blur.armory.presentation.registration.EmailFormatValidator


class BookFragment : BindingFragment<FragmentBookBinding>() {
    private val viewModel: BookViewModel by viewModel {
        parametersOf(getRoomIdFromArgs(requireArguments()))
    }

    private lateinit var emailAdapter: EmailAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBookBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!::emailAdapter.isInitialized) {
            emailAdapter = EmailAdapter {
                // todo delete from vm
            }
        }

        with(binding) {
            var date: Date
            var dateTime: DateTimeTz

            TrueTime.nowLocal().let { now ->
                date = Date(now.yearInt, now.month, now.dayOfMonth)
                dateTime = now

                textViewBookRoomName.text = getRoomNameFromArgs(requireArguments())

                textViewBookTime.text = now.format("HH:mm")
                textViewBookDate.text =
                    now.format(DateFormat("dd MMM").withLocale(KlockLocale.russian))

                cardViewBookDate.setOnClickListener {
                    DatePickerDialog(requireContext()).apply {
                        setOnDateSetListener { _, year, month, dayOfMonth ->
                            date = Date(year, month + 1, dayOfMonth)

                            textViewBookDate.text =
                                date.format(DateFormat("dd MMM").withLocale(KlockLocale.russian))
                        }

                        datePicker.minDate = now.utc.unixMillisLong
                        datePicker.updateDate(
                            date.year,
                            date.month.index0,
                            date.day
                        )
                    }.show()
                }

                cardViewBookTime.setOnClickListener {
                    TimePickerDialog(
                        requireContext(),
                        { _, hourOfDay, minutes ->
                            dateTime = DateTimeTz.local(
                                DateTime(date, Time(hourOfDay, minutes)),
                                now.offset
                            )
                            textViewBookTime.text = dateTime.format("HH:mm")
                        },
                        dateTime.hours,
                        dateTime.minutes,
                        true
                    ).show()
                }
            }

            imageButtonBookDecreaseDuration.setOnClickListener {
                handleCapacityChange(binding, CapacityChange.DECREASE)
            }

            imageButtonBookIncreaseDuration.setOnClickListener {
                handleCapacityChange(binding, CapacityChange.INCREASE)
            }

            editTextBookingEmail.addTextChangedListener {
                fabBookAddEmail.isEnabled =
                    !it.isNullOrBlank() && EmailFormatValidator.validateEmailFormat(it.toString()) == EmailFormatValidator.EmailValidationResult.VALID
            }

            fabBookAddEmail.setOnClickListener {
                viewModel.inviteUser(editTextBookingEmail.text!!.toString())
            }

            buttonBookBook.setOnClickListener {
                viewModel.book(
                    editTextBookingName.text?.toString() ?: "",
                    dateTime,
                    date,
                    textViewBookDuration.text!!.toString().split(" ").first().toInt()
                )
            }

            recyclerViewBookingInvites.adapter = emailAdapter

            viewModel.state.observeNonNull(viewLifecycleOwner) { state ->
                with(state) {
                    requestToken {
                        getAccountToken()
                    }

                    onInviteFailed {
                        editTextBookingEmail.text = null
                        fabBookAddEmail.isEnabled = false
                    }

                    onInviteSucceed {
                        editTextBookingEmail.text = null
                        fabBookAddEmail.isEnabled = false
                        emailAdapter.addItem(it)
                    }

                    onBookCompleted {
                        findNavController().popBackStack()
                    }

                    onError {
                        it.printStackTrace()
                        ErrorPopup.show(
                            requireContext(),
                            "Ошибка",
                            "Неудалось создать бронирование",
                            it
                        )
                    }
                }

            }
        }
    }

    private fun handleCapacityChange(
        binding: FragmentBookBinding,
        capacityChange: CapacityChange
    ) {
        with(binding) {
            when (capacityChange) {
                CapacityChange.DECREASE -> {
                    textViewBookDuration.text =
                        (textViewBookDuration.text.toString().split(" ").first()
                            .toInt() - 15).toString() + " м"
                }
                CapacityChange.INCREASE -> {
                    textViewBookDuration.text =
                        (textViewBookDuration.text.toString().split(" ").first()
                            .toInt() + 15).toString() + " м"
                }
            }

            imageButtonBookDecreaseDuration.isEnabled =
                textViewBookDuration.text.toString().split(" ").first().toInt() > 30

            imageButtonBookIncreaseDuration.isEnabled =
                textViewBookDuration.text.toString().split(" ").first().toInt() < 180
        }
    }

    private fun getAccountToken() {
        if (requireActivity().checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            val intent = AccountManager.newChooseAccountIntent(
                null,
                null,
                arrayOf("com.google"),
                null,
                null,
                null,
                null
            )
            startActivityForResult(intent, 42)
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.GET_ACCOUNTS),
                1
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 42) {
            val accountManager = AccountManager.get(requireContext())
            val accounts = accountManager.getAccountsByType("com.google")
            println("TESTING: ${accounts.size}")

            val options = bundleOf()

            accountManager.getAuthToken(
                accounts.first(),
                "oauth2:https://www.googleapis.com/auth/calendar",
                options,
                requireActivity(),
                { result ->
                    val bundle: Bundle = result.result

                    val token: String? = bundle.getString(AccountManager.KEY_AUTHTOKEN)

                    viewModel.token = token
                },
                Handler(Looper.getMainLooper()) {
                    true
                }
            )
        } else if (requestCode == 1) {
            getAccountToken()
        }
    }

    private class EmailAdapter(
        private val onDelete: (Person) -> Unit
    ) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

        private var items = listOf<Person>()

        fun addItem(person: Person) {
            if (items.isEmpty()) {
                items = items + person
                notifyDataSetChanged()
            } else {
                items = items + person
                notifyItemInserted(items.size)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EmailViewHolder(
            { person, position ->
                onDelete(person)
                items = items.filterNot {
                    it == person
                }

                notifyItemRemoved(position)
            },
            ItemBookEmailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size

        class EmailViewHolder(
            private val onDelete: (Person, Int) -> Unit,
            private val binding: ItemBookEmailBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(person: Person) {
                binding.textViewEmailName.text = person.name
                binding.imageButtonEmailRemove.setOnClickListener {
                    onDelete(person, adapterPosition)
                }
            }
        }
    }

    enum class CapacityChange {
        DECREASE,
        INCREASE
    }

    companion object {
        private const val ARG_ROOM_ID = "roomId"
        private const val ARG_ROOM_NAME = "roomName"

        private fun getRoomIdFromArgs(arguments: Bundle) = arguments.getInt(ARG_ROOM_ID, -1)
        private fun getRoomNameFromArgs(arguments: Bundle) = arguments.getString(ARG_ROOM_NAME)!!

        fun bundleArgs(roomId: Int, roomName: String) = bundleOf(
            ARG_ROOM_ID to roomId,
            ARG_ROOM_NAME to roomName
        )
    }
}