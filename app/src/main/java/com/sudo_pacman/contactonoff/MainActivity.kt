package com.sudo_pacman.contactonoff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.sudo_pacman.contactonoff.navigation.AppNavigationHandler
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.utils.MyEventBus
import com.sudo_pacman.contactonoff.utils.NetworkStatusValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: ContactRepository

    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator


    @Inject
    lateinit var appNavigationHandler: AppNavigationHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = hostFragment.navController

        appNavigationHandler.buffer
            .onEach { it.invoke(navController) }
            .launchIn(lifecycleScope)

        networkStatusValidator.init(
            availableNetworkBlock = {
                repository.syncWithServer().onEach {
                    it.onSuccess {
                        this@MainActivity.runOnUiThread {
                            MyEventBus.reload?.invoke()
                        }
                    }
                    it.onFailure {
                        this@MainActivity.runOnUiThread {
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }.launchIn(lifecycleScope)
            },
            lostConnection = {
                Toast.makeText(this@MainActivity, "Not connection", Toast.LENGTH_SHORT).show()
            }
        )
    }
}