package com.sudo_pacman.contactonoff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.domain.ContactRepositoryImpl
import com.sudo_pacman.contactonoff.utils.MyEventBus
import com.sudo_pacman.contactonoff.utils.NetworkStatusValidator
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: ContactRepository

    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        networkStatusValidator.init(
            availableNetworkBlock = {
                executor.execute {
                    repository.syncWithServer(
                        finishBlock = {
                            this@MainActivity.runOnUiThread {
                                MyEventBus.reload?.invoke()
                            }
                        },
                        errorBlock = {
                            this@MainActivity.runOnUiThread {
                                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            },
            lostConnection = { Toast.makeText(this@MainActivity, "Not connection", Toast.LENGTH_SHORT).show() }
        )
    }
}