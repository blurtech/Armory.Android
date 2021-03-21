package tech.blur.armory.presentation.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.soywiz.klock.DateFormat
import com.soywiz.klock.KlockLocale
import com.soywiz.klock.days
import com.soywiz.klock.locale.russian
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tech.blur.armory.R
import tech.blur.armory.databinding.FragmentScheduleBinding
import tech.blur.armory.domain.models.Room
import tech.blur.armory.presentation.book.BookFragment
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.common.ErrorPopup
import tech.blur.armory.presentation.common.observeNonNull

class ScheduleFragment : BindingFragment<FragmentScheduleBinding>() {

    private val viewModel: ScheduleViewModel by viewModel {
        parametersOf(room.id)
    }

    val room by lazy { getRoomFromArgs(requireArguments()) }

    private lateinit var calendarAdapter: CalendarAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentScheduleBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!::calendarAdapter.isInitialized) {
            calendarAdapter = CalendarAdapter(
                onDateChanged = {
                    binding.textViewScheduleDate.text =
                        it.format(DateFormat("MMM. yyyy").withLocale(KlockLocale.russian))
                            .capitalize()
                },
                onDayClicked = {
                    viewModel.getBooking(
                        calendarAdapter.currentDateTime.yearInt,
                        calendarAdapter.currentDateTime.month1,
                        it.day.toInt()
                    )
                }
            )
        }

        with(binding) {
            imageButtonScheduleBack.setOnClickListener {
                findNavController().popBackStack()
            }

            textViewScheduleBack.setOnClickListener {
                findNavController().popBackStack()
            }

            with(recyclerViewCalendar) {
                adapter = calendarAdapter
                layoutManager = GridLayoutManager(requireContext(), LIST_COLUMN_COUNT)
            }

            imageButtonScheduleDecrease.setOnClickListener {
                calendarAdapter.buildList(
                    calendarAdapter.currentDateTime.let { currentDateTime ->
                        currentDateTime - currentDateTime.month.days(currentDateTime.year).days
                    }
                )
            }

            imageButtonScheduleIncrease.setOnClickListener {
                calendarAdapter.buildList(
                    calendarAdapter.currentDateTime.let { currentDateTime ->
                        currentDateTime + currentDateTime.month.days(currentDateTime.year).days
                    }
                )
            }

            textViewScheduleRoomName.text = room.name
            buttonScheduleBook.setOnClickListener {
                findNavController().navigate(
                    R.id.action_scheduleFragment_to_bookFragment,
                    BookFragment.bundleArgs(room.id, room.name)
                )
            }

            viewModel.state.observeNonNull(viewLifecycleOwner) { state ->
                with(recyclerViewBookings) {
                    if (state.items.isNotEmpty()) {
                        adapter = BookingTimeAdapter(state.items)
                        cardViewScheduleBookings.isVisible = true
                        buttonScheduleBook.isVisible = true
                    }
                }

                state.onError {
                    it.printStackTrace()
                    ErrorPopup.show(
                        requireContext(),
                        "Ошибка",
                        "При загрузке произошла ошибка",
                        it
                    )
                }
            }
        }
    }

    companion object {
        private const val LIST_COLUMN_COUNT = 7
        private const val ARG_ROOM = "room"

        fun getRoomFromArgs(arguments: Bundle) = arguments.getSerializable(ARG_ROOM) as Room

        fun bundleArgs(room: Room) = bundleOf(
            ARG_ROOM to room
        )
    }
}