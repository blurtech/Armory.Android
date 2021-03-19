package tech.blur.armory.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.armory.R
import tech.blur.armory.presentation.common.observeNonNull

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private val bottomBar: BottomNavigationView get() = findViewById(R.id.nav_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.state.observeNonNull(this) {
            if (it.isLoggedIn) {
                setUpMainScene(navController)
            } else {
                setUpLoginScene(navController)
            }
        }
    }

    private fun setUpLoginScene(navController: NavController) {
        actionBar!!.hide()
        bottomBar.isVisible = false

        navController.navigate(
            R.id.navigation_loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.navigation_loginFragment, true).build()
        )
    }

    private fun setUpMainScene(navController: NavController) {
        actionBar!!.show()
        bottomBar.isVisible = true

        navController.navigate(
            R.id.navigation_myEvents,
            null,
            NavOptions.Builder().setPopUpTo(R.id.navigation_myEvents, true).build()
        )
    }
}