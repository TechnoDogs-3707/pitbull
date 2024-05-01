// i am sorry for this nightmarish code
// good luck
// the generateUniqueJoinCode function is found in Utils.kt btw

package org.brightonrobotics.pitbull

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ConfirmDataActivity : AppCompatActivity() {
    private lateinit var teamNumber: TextView
    private lateinit var name: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    @OptIn(DelicateCoroutinesApi::class)
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

            GlobalScope.launch {
                generateUniqueJoinCode(intent.getStringExtra("teamNumber")!!)
            }

            val storage = Firebase.storage("gs://pitbull-421814.appspot.com")
            val usersDirectory = storage.reference.child("data").child(intent.getStringExtra("teamNumber").toString()).child("users")
            val dummyFile = usersDirectory.child("dummy.txt")
            dummyFile.putBytes("dummy".toByteArray())

            startActivity(Intent(this, DisplayTeamDataActivity::class.java))
        }
    }
}
