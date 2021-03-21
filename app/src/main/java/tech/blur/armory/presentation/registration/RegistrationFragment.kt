package tech.blur.armory.presentation.registration

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import tech.blur.armory.R
import tech.blur.armory.databinding.FragmentRegistrationBinding
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.common.ErrorPopup
import tech.blur.armory.presentation.common.observeNonNull

class RegistrationFragment : BindingFragment<FragmentRegistrationBinding>() {

    private val viewModel: RegistrationViewModel by inject()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegistrationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            editTextRegistrationName.addTextChangedListener {
                it?.let {
                    viewModel.nameFlow.tryEmit(it.toString())
                }
            }

            editTextRegistrationSurname.addTextChangedListener {
                it?.let {
                    viewModel.surnameFlow.tryEmit(it.toString())
                }
            }

            editTextRegistrationEmail.addTextChangedListener {
                it?.let {
                    viewModel.emailFlow.tryEmit(it.toString())
                }
            }

            editTextRegistrationPassword.addTextChangedListener {
                processPasswords(
                    it?.toString(),
                    editTextRegistrationRepeatPassword.text?.toString()
                )

                textInputLayoutRegistrationRepeatPassword.isEnabled = !it.isNullOrEmpty()
            }

            editTextRegistrationRepeatPassword.addTextChangedListener {
                processPasswords(editTextRegistrationPassword.text?.toString(), it?.toString())
            }

            textViewRegistrationHaveAccount.apply {
                paintFlags =
                    textViewRegistrationHaveAccount.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                setOnClickListener {
                    findNavController().popBackStack()
                }
            }

            buttonRegistrationRegister.setOnClickListener {
                viewModel.register()
            }

            viewModel.state.observeNonNull(viewLifecycleOwner) { state ->
                with(state) {
                    buttonRegistrationRegister.isEnabled = isFieldsValid

                    editTextRegistrationEmail.error = when {
                        !isEmailValid -> "Неправильный e-mail"
                        !isEmailAlreadyTaken -> "E-mail уже занят"
                        else -> null
                    }

                    onRegistrationSucceed.handle {
                        findNavController().navigate(R.id.action_registrationFragment_to_myEvents)
                    }

                    onError.handle {
                        it.printStackTrace()
                        ErrorPopup.show(
                            requireContext(),
                            "Ошибка",
                            "При входе произошла ошибка, попробуйте еще раз",
                            it
                        )
                    }
                }
            }
        }
    }

    private fun processPasswords(password: String?, repeatPassword: String?) {
        if (password == repeatPassword && password != null && repeatPassword != null) {
            binding.textInputLayoutRegistrationRepeatPassword.error = null
            viewModel.passwordFlow.tryEmit(password)
        }

        if (password != repeatPassword && repeatPassword != "") {
            binding.textInputLayoutRegistrationRepeatPassword.error = "Пароли не совпадают"
        }
    }

    private fun updateButton() {

    }
}