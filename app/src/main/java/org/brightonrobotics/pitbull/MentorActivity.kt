package org.brightonrobotics.pitbull

import android.content.ContentValues
import android.content.Intent
import android.credentials.GetCredentialException
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialOption
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

val supabase = createSupabaseClient(
    supabaseUrl = "https://ooixjwpxzwtrxnyyirii.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9vaXhqd3B4end0cnhueXlpcmlpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTQ0MDEyODYsImV4cCI6MjAyOTk3NzI4Nn0.ArbpzcyXucBgBJjRQ3KFgSnYeas9cugz8NqJl7UGxT4"
) {
    install(Auth)
    install(Postgrest)
}

@Suppress("NAME_SHADOWING")
class MentorActivity : AppCompatActivity() {
    private lateinit var welcomeMessage: TextView
    private lateinit var signInWithGoogleButton: Button
    private lateinit var teamNumberInput: EditText
    private lateinit var signUpForCrowdScoutCheckBox: CheckBox
    private lateinit var pushToCrowdScoutCheckBox: CheckBox
    private lateinit var pullFromCrowdScoutCheckBox: CheckBox
    private lateinit var imNotAMentorButton: Button
    private lateinit var teamAlreadyHasAccountButton: Button

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mentor)

//        val layout: ConstraintLayout = findViewById(R.id.mentor_layout)

        welcomeMessage = findViewById(R.id.welcome_mentor)
        signInWithGoogleButton = findViewById(R.id.sign_in_with_google)
        teamNumberInput = findViewById(R.id.team_number_input)
        signUpForCrowdScoutCheckBox = findViewById(R.id.sign_up_for_crowdscout)
        pushToCrowdScoutCheckBox = findViewById(R.id.push_to_crowdscout)
        pullFromCrowdScoutCheckBox = findViewById(R.id.pull_from_crowdscout)
        imNotAMentorButton = findViewById(R.id.im_not_a_mentor)
        teamAlreadyHasAccountButton = findViewById(R.id.team_already_has_account)

        signInWithGoogleButton.setOnClickListener {
            val credentialManager = androidx.credentials.CredentialManager.create(this)

            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

            val googleIdOption: CredentialOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("571502467552-bbmlkdi948hrfcnkvm9gmbv7utf2fmqt.apps.googleusercontent.com")
                .setNonce(hashedNonce)
                .build()

            val request: androidx.credentials.GetCredentialRequest =
                androidx.credentials.GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = this@MentorActivity,
                    )
                    val credential = result.credential

                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)

                    val googleIdToken = googleIdTokenCredential.idToken

                    Log.i(ContentValues.TAG, googleIdToken)
                    supabase.auth.signInWith(IDToken) {
                        idToken = googleIdToken
                        provider = Google
                        nonce = rawNonce
                    }

                    teamNumberInput.visibility = EditText.VISIBLE
//                    signUpForCrowdScoutCheckBox.visibility = CheckBox.VISIBLE
                } catch (e: GetCredentialException) {
                    Log.i(ContentValues.TAG, "Failed to get credential")
                    Toast.makeText(this@MentorActivity, "Failed to get credential", Toast.LENGTH_SHORT).show()
                } catch (e: GoogleIdTokenParsingException) {
                    Log.i(ContentValues.TAG, "Failed to parse Google ID token")
                    Toast.makeText(this@MentorActivity, "Failed to parse Google ID token", Toast.LENGTH_SHORT).show()
                } catch (e: GetCredentialCancellationException) {
                    Log.i(ContentValues.TAG, "User cancelled the operation")
                }
            }
        }

        signUpForCrowdScoutCheckBox.setOnClickListener {
            if (signUpForCrowdScoutCheckBox.isChecked) {
                pushToCrowdScoutCheckBox.visibility = CheckBox.VISIBLE
                pullFromCrowdScoutCheckBox.visibility = CheckBox.VISIBLE
            } else {
                pushToCrowdScoutCheckBox.visibility = CheckBox.GONE
                pushToCrowdScoutCheckBox.isChecked = false
                pullFromCrowdScoutCheckBox.visibility = CheckBox.GONE
                pullFromCrowdScoutCheckBox.isChecked = false
            }
        }

//        val insertButton = Button(this)
//        insertButton.text = "Insert"
//        insertButton.setOnClickListener {
//            coroutineScope.launch {
//                try {
//                    supabase.from("posts").insert(mapOf("content" to "Hello, world!"))
//
//                    Toast.makeText(this@MentorActivity, "Inserted", Toast.LENGTH_SHORT).show()
//                    Log.i(TAG, "Inserted")
//                } catch (e: RestException) {
//                    Toast.makeText(this@MentorActivity, e.message, Toast.LENGTH_SHORT).show()
//                    Log.i(TAG, e.message.toString())
//                }
//            }
//        }
//
//        layout.addView(insertButton)

        imNotAMentorButton.setOnClickListener {
            startActivity(Intent(this, PickRoleActivity::class.java))
        }

        teamAlreadyHasAccountButton.setOnClickListener {
            // Start the student sign in activity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the job when the activity is destroyed
    }
}