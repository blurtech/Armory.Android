package tech.blur.armory.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.armory.databinding.FragmentLoginBinding
import tech.blur.armory.presentation.common.BindingFragment

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
        }
    }

    private fun updateLoginButton() {
        with(binding) {
            if (editTextLoginEmail.text.isNullOrBlank() || editTextLoginEmail.text.isNullOrBlank()) {
                buttonLoginLogin.isEnabled = false
            }
        }

    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
}