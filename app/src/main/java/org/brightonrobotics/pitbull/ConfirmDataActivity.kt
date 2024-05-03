// i am sorry for this nightmarish code
// good luck
// the generateUniqueJoinCode function is found in Utils.kt btw
// also sorry about the nested if statement, i am too lazy to put in a try catch block

package org.brightonrobotics.pitbull

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class ConfirmDataActivity : AppCompatActivity() {
    private lateinit var teamNumber: TextView
    private lateinit var name: EditText
    private lateinit var noButton: Button
    private lateinit var yesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirm_data)

        teamNumber = findViewById(R.id.team_number_show)
        teamNumber.text = intent.getStringExtra("teamNumber")
        name = findViewById(R.id.name)
        name.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("name"))

        noButton = findViewById(R.id.info_incorrect)
        yesButton = findViewById(R.id.info_correct)

        noButton.setOnClickListener {
            startActivity(Intent(this, MentorActivity::class.java))
        }

        suspend fun getCode(teamNumberIn: String) {
            val code = generateAlphanumericCode(6)

            if (
                !isJoinCodeExists(code)
            ) {
                if (!isTeamNumberExists(teamNumberIn)) {
                    val database = Firebase.database
                    val ref = database.getReference("teams").child(teamNumberIn)
                    ref.child("joinCode").setValue(code)
                    ref.child("ownerName").setValue(name.text.toString())

                    val joinCodeRef = database.getReference("joinCodes")
                    joinCodeRef.child(code).setValue(teamNumberIn)

                    startActivity(
                        Intent(
                            this@ConfirmDataActivity,
                            DisplayTeamDataActivity::class.java
                        ).apply {
                            putExtra("teamNumber", teamNumberIn)
                            putExtra("joinCode", code)
                            putExtra("name", name.text.toString())
                        }
                    )
                } else {
                    startActivity(Intent(this, MentorActivity::class.java))

                    runOnUiThread {
                        Toast.makeText(
                            this@ConfirmDataActivity,
                            "Team already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                getCode(teamNumberIn)
            }
        }

        yesButton.setOnClickListener {
            GlobalScope.launch {
                getCode(teamNumber.text.toString())
            }
        }
    }
}
