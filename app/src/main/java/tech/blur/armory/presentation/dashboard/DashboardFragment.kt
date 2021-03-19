package tech.blur.armory.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.koin.android.ext.android.inject
import tech.blur.armory.R
import tech.blur.armory.databinding.FragmentDashboardBinding
import tech.blur.armory.domain.commands.GoogleCalendarCommand
import tech.blur.armory.presentation.common.BindingFragment


class DashboardFragment : BindingFragment<FragmentDashboardBinding>() {
    private lateinit var dashboardViewModel: DashboardViewModel

    private val command: GoogleCalendarCommand by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        binding.textDashboard.setOnClickListener {
            val gso: GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .build()

            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

//        command.authorize()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        val account = try {
            completedTask.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            println("TESTING: signInResult:failed code=${e.statusCode}")
            null
        }

        println("TESTING: ${account?.idToken}")
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    companion object {
        private const val RC_SIGN_IN = 1
    }
}