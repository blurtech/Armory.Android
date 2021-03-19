package tech.blur.armory.presentation.login

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.armory.R
import tech.blur.armory.databinding.FragmentLoginBinding
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.common.ErrorPopup
import tech.blur.armory.presentation.common.observeNonNull

class LoginFragment : BindingFragment<FragmentLoginBinding>() {
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            editTextLoginEmail.addTextChangedListener {
                updateLoginButton()
            }

            editTextLoginPassword.addTextChangedListener {
                updateLoginButton()
            }

            buttonLoginLogin.setOnClickListener {
                viewModel.signIn(
                    editTextLoginEmail.text!!.toString(),
                    editTextLoginPassword.text!!.toString()
                )
            }

            textViewLoginRegister.apply {
                paintFlags =
                    textViewLoginRegister.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                setOnClickListener {
                    findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
                }
            }
        }

        viewModel.state.observeNonNull(viewLifecycleOwner) { state ->
            with(state) {
                onLoginComlete.handle {
                    findNavController().navigate(R.id.action_loginFragment_to_myEvents)
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

    private fun updateLoginButton() {
        with(binding) {
            editTextLoginEmail.text
            buttonLoginLogin.isEnabled =
                !(editTextLoginEmail.text.isNullOrBlank() || editTextLoginPassword.text.isNullOrBlank())
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
}