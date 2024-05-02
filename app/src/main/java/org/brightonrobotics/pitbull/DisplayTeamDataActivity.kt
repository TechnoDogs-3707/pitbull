package org.brightonrobotics.pitbull

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class DisplayTeamDataActivity : AppCompatActivity() {
    private lateinit var teamNumberText: TextView
    private lateinit var joinCodeText: TextView
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_team_data)

        teamNumberText = findViewById(R.id.team_number_show)
        joinCodeText = findViewById(R.id.join_code)
        continueButton = findViewById(R.id.next_button_cont_to_main)

        val teamNumber = intent.getStringExtra("teamNumber")
        val joinCode = intent.getStringExtra("joinCode")

        teamNumberText.text = teamNumber
        joinCodeText.text = joinCode

        continueButton.setOnClickListener {
            File(getExternalFilesDir(null), "settings.json").writeText(
                """
                    {
                        "role": 1,
                        "name": "${intent.getStringExtra("name")}",
                        "teamNumber": $teamNumber,
                        "joinCode": "$joinCode"
                    }
                """.trimIndent()
            )

            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}