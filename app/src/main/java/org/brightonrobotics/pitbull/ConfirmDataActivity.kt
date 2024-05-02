// i am sorry for this nightmarish code
// good luck
// the generateUniqueJoinCode function is found in Utils.kt btw

package org.brightonrobotics.pitbull

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ConfirmDataActivity : AppCompatActivity() {
    private lateinit var teamNumber: TextView
    private lateinit var name: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirm_data)

        teamNumber = findViewById(R.id.team_number_show)
        teamNumber.text = intent.getStringExtra("teamNumber")
        name = findViewById(R.id.name)
        name.text = intent.getStringExtra("name")

        noButton = findViewById(R.id.info_incorrect)
        yesButton = findViewById(R.id.info_correct)

        noButton.setOnClickListener {
            startActivity(Intent(this, MentorActivity::class.java))
        }

        yesButton.setOnClickListener {
            val storage = Firebase.storage("gs://pitbull-421814.appspot.com")
            val directoryReference =
                storage.reference.child("data/${intent.getStringExtra("teamNumber")}")

            GlobalScope.launch {
                try {
                    val joinCode =
                        async { generateUniqueJoinCode(intent.getStringExtra("teamNumber")!!) }

                    val newFileReference = directoryReference.child("code.txt")
                    newFileReference.putBytes(joinCode.await().toByteArray()).await()

                    val displayTeamDataIntent =
                        Intent(this@ConfirmDataActivity, DisplayTeamDataActivity::class.java)

                    displayTeamDataIntent.putExtra(
                        "teamNumber",
                        intent.getStringExtra("teamNumber")
                    )
                    displayTeamDataIntent.putExtra("joinCode", joinCode.await())
                    displayTeamDataIntent.putExtra("name", intent.getStringExtra("name"))

                    startActivity(displayTeamDataIntent)
                } catch (e: StorageException) {
                    directoryReference.listAll().await()

                    runOnUiThread {
                        Toast.makeText(
                            this@ConfirmDataActivity,
                            "This team is already signed up.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@ConfirmDataActivity,
                                PickRoleActivity::class.java
                            )
                        )
                    }
                }
            }
        }
    }
}
