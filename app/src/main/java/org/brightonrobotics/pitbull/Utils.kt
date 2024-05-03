package org.brightonrobotics.pitbull

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.security.SecureRandom

fun generateAlphanumericCode(codeLength: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val random = SecureRandom()
    val code = StringBuilder(codeLength)

    for (i in 0 until codeLength) {
        code.append(chars[random.nextInt(chars.length)])
    }

    return code.toString()
}

suspend fun isTeamNumberExists(teamNumber: String): Boolean {
    val database = Firebase.database
    val ref = database.getReference("teams").child(teamNumber)
    val snapshot = ref.get().await()
    return snapshot.exists()
}

suspend fun isJoinCodeExists(joinCode: String): Boolean {
    val database = Firebase.database
    val ref = database.getReference("joinCodes").child(joinCode)
    val snapshot = ref.get().await()
    return snapshot.exists()
}