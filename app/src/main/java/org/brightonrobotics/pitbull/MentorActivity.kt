package org.brightonrobotics.pitbull

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MentorActivity : AppCompatActivity() {
    private lateinit var signInWithGoogleButton: Button
    private lateinit var teamNumberInput: EditText
    private lateinit var signUpForCrowdScoutCheckBox: CheckBox
    private lateinit var pushToCrowdScoutCheckBox: CheckBox
    private lateinit var pullFromCrowdScoutCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mentor)

        signInWithGoogleButton = findViewById(R.id.sign_in_with_google)
        teamNumberInput = findViewById(R.id.team_number_input)
        signUpForCrowdScoutCheckBox = findViewById(R.id.sign_up_for_crowdscout)
        pushToCrowdScoutCheckBox = findViewById(R.id.push_to_crowdscout)
        pullFromCrowdScoutCheckBox = findViewById(R.id.pull_from_crowdscout)

        signInWithGoogleButton.setOnClickListener {
            teamNumberInput.visibility = EditText.VISIBLE
            signUpForCrowdScoutCheckBox.visibility = CheckBox.VISIBLE
        }

        signUpForCrowdScoutCheckBox.setOnClickListener {
            if (signUpForCrowdScoutCheckBox.isChecked) {
                pushToCrowdScoutCheckBox.visibility = CheckBox.VISIBLE
                pullFromCrowdScoutCheckBox.visibility = CheckBox.VISIBLE
            } else {
                pushToCrowdScoutCheckBox.visibility = CheckBox.GONE
                pushToCrowdScoutCheckBox.isChecked = false
                pullFromCrowdScoutCheckBox.visibility = CheckBox.GONE
                pushToCrowdScoutCheckBox.isChecked = false
            }
        }
    }
}