package org.brightonrobotics.pitbull

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.UUID

@Suppress("NAME_SHADOWING")
class MentorActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mentor)

        auth = Firebase.auth

        findViewById<Button>(R.id.sign_in_with_google).setOnClickListener {
            val credentialManager = androidx.credentials.CredentialManager.create(this)
            val hashedNonce = UUID.randomUUID().toString().toByteArray().fold("") { str, it -> str + "%02x".format(it) }
            val googleIdOption: androidx.credentials.CredentialOption = GetGoogleIdOption.Builder()
                .setServerClientId("571502467552-bbmlkdi948hrfcnkvm9gmbv7utf2fmqt.apps.googleusercontent.com")
                .setNonce(hashedNonce)
                .build()
            val request: androidx.credentials.GetCredentialRequest = androidx.credentials.GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(request = request, context = this@MentorActivity)
                    val googleIdToken = GoogleIdTokenCredential.createFrom(result.credential.data).idToken
                    auth.signInWithCredential(GoogleAuthProvider.getCredential(googleIdToken, null))
                        .addOnCompleteListener(this@MentorActivity) { task ->
                            if (task.isSuccessful) {
                                Log.d("TAG", "signInWithCredential:success")
                                findViewById<TextView>(R.id.welcome_mentor).text = "Welcome, ${auth.currentUser?.displayName}"
                                findViewById<EditText>(R.id.team_number_input).visibility = EditText.VISIBLE
                            } else {
                                Log.w("TAG", "signInWithCredential:failure", task.exception)
                            }
                        }
                } catch (e: Exception) {
                    Log.i("TAG", "Failed to get credential or parse Google ID token")
                    Toast.makeText(this@MentorActivity, "Failed to get credential or parse Google ID token", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<EditText>(R.id.team_number_input).addTextChangedListener {
            findViewById<Button>(R.id.continue_button_mentor).visibility = if (it.toString().isNotEmpty()) Button.VISIBLE else Button.GONE
        }

        findViewById<Button>(R.id.continue_button_mentor).setOnClickListener {
            val intent = Intent(this, ConfirmDataActivity::class.java)
            intent.putExtra("userData", auth.currentUser)
            intent.putExtra("teamNumber", findViewById<EditText>(R.id.team_number_input).text.toString())
            startActivity(intent)
        }

        findViewById<Button>(R.id.im_not_a_mentor).setOnClickListener {
            startActivity(Intent(this, PickRoleActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}