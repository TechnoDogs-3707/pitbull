// code simplified by ai, it may be complete shit
// ai simplified code is so unreadable i am actually so sorry but
// i could not live with  my code

package org.brightonrobotics.pitbull

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File

class JoinTeamActivity : AppCompatActivity() {
    private lateinit var teamNumber: EditText
    private lateinit var joinCode: EditText
    private lateinit var name: EditText
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_team)

        teamNumber = findViewById(R.id.team_number_in)
        joinCode = findViewById(R.id.join_code_in)
        name = findViewById(R.id.name_in)
        continueButton = findViewById(R.id.cont_btn)

        teamNumber.addTextChangedListener { updateContinueButtonVisibility() }
        joinCode.addTextChangedListener { updateContinueButtonVisibility() }
        name.addTextChangedListener { updateContinueButtonVisibility() }

        continueButton.setOnClickListener { validateTeam() }
    }

    private fun updateContinueButtonVisibility() {
        continueButton.visibility =
            if (teamNumber.text.isNotEmpty() && joinCode.text.isNotEmpty() && name.text.isNotEmpty()) Button.VISIBLE else Button.GONE
    }

    private fun validateTeam() {
        val ref = Firebase.database.getReference("teams").child(teamNumber.text.toString())
        ref.get().addOnSuccessListener {
            if (it.exists() && it.child("joinCode").value == joinCode.text.toString()) {
                val memberRef =
                    Firebase.database.getReference("teams").child(teamNumber.text.toString())
                        .child("members")
                val newMemberRef =
                    memberRef.push()
                newMemberRef.setValue(name.text.toString())

                File(getExternalFilesDir(null), "settings.json").writeText(
                    """
                    {
                        "role": 2,
                        "name": "${name.text}",
                        "teamNumber": "${teamNumber.text}",
                        "joinCode": "${joinCode.text}",
                        "memberId": "${newMemberRef.key}"
                    }
                    """.trimIndent()
                )

                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(
                    this,
                    "Join code is incorrect or team does not exist",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}