package org.brightonrobotics.pitbull

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File

fun initSettings() {
    val settingsFile = File("settings.txt")
    if (!settingsFile.exists()) {
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}