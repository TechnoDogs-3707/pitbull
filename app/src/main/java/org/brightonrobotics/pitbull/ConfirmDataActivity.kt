package org.brightonrobotics.pitbull

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class ConfirmDataActivity : AppCompatActivity() {
    private lateinit var teamNumber: TextView
    private lateinit var name: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirm_data)

        teamNumber = findViewById(R.id.team_number)
        teamNumber.text = intent.getStringExtra("teamNumber")
        name = findViewById(R.id.name)
        name.text = intent.getStringExtra("name")

        noButton = findViewById(R.id.info_incorrect)
        yesButton = findViewById(R.id.info_correct)

        noButton.setOnClickListener {
            startActivity(Intent(this, MentorActivity::class.java))
        }

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
    }
}
