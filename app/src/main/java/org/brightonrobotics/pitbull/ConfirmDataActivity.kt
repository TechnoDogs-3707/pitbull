package org.brightonrobotics.pitbull

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class ConfirmDataActivity : AppCompatActivity() {
    private lateinit var teamNumber: Number
    private lateinit var name: String
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirm_data)

        teamNumber = intent.getIntExtra("teamNumber", 0)
        name = intent.getStringExtra("name") ?: ""

        yesButton = findViewById(R.id.info_correct)
        noButton = findViewById(R.id.info_incorrect)

        yesButton.setOnClickListener {
            File(getExternalFilesDir(null), "settings.json").writeText(
                """
                    {
                        "role": 1
                    }
                """.trimIndent()
            )
            startActivity(Intent(this, MainActivity::class.java))
        }

        noButton.setOnClickListener {
            startActivity(Intent(this, MentorActivity::class.java))
        }
    }
}
