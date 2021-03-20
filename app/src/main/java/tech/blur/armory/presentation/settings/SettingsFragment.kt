package tech.blur.armory.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import tech.blur.armory.R
import tech.blur.armory.databinding.FragmentSettingsBinding
import tech.blur.armory.presentation.common.BindingFragment
import tech.blur.armory.presentation.common.observeNonNull

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSettingsLogout.setOnClickListener {
            viewModel.logOut()
        }

        viewModel.state.observeNonNull(viewLifecycleOwner) {
            it.loggedOut.handle {
                findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
            }
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)
}