package com.sudo_pacman.contactonoff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sudo_pacman.contactonoff.domain.ContactRepositoryImpl
import com.sudo_pacman.contactonoff.utils.MyEventBus
import com.sudo_pacman.contactonoff.utils.NetworkStatusValidator

class MainActivity : AppCompatActivity() {
    private val repository = ContactRepositoryImpl.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkStatusValidator.init(
            context = this,
            availableNetworkBlock = {
                repository.syncWithServer(
                    finishBlock = {
                        MyEventBus.reload?.invoke()
                    },
                    errorBlock =  {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }
}