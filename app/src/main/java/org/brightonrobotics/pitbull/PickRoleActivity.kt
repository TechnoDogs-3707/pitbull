// actually somehow i made good code?!??! what is this madness

package org.brightonrobotics.pitbull

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PickRoleActivity : AppCompatActivity() {
    private lateinit var studentButton: Button
    private lateinit var mentorButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pick_role)

        studentButton = findViewById(R.id.select_student)
        mentorButton = findViewById(R.id.select_mentor)

        studentButton.setOnClickListener {
            startActivity(Intent(this, JoinTeamActivity::class.java))
        }

        mentorButton.setOnClickListener {
            startActivity(Intent(this, MentorActivity::class.java))
        }
    }
}