package org.brightonrobotics.pitbull

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

fun getSettings(context: Context): String {
    val settingsFile = File(context.getExternalFilesDir(null), "settings.json")
    return settingsFile.readText()
}

fun initSettings(context: Context) {
    val settingsFile = File(context.getExternalFilesDir(null), "settings.json")
    if (!settingsFile.exists()) {
        settingsFile.writeText("{}")
    }
}

fun getRole(context: Context): Int? {
    val settings = getSettings(context)
    val json = JSONObject(settings)
    return if (json.has("role")) {
        val role = json.getInt("role")
        if (role == 1 /*mentor*/ || role == 2 /*student*/) role else null
    } else {
        null
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSettings(this)
        val role = getRole(this)
        if (role == null) {
            startActivity(Intent(this, PickRoleActivity::class.java))
        } else {
            // do something based on role
        }
    }
}