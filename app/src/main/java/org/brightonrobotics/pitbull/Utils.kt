package org.brightonrobotics.pitbull

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.security.SecureRandom

//suspend fun generateUniqueJoinCode(teamNumber: String): String {
//    val storage = Firebase.storage("gs://pitbull-421814.appspot.com")
//    val directory = storage.reference.child("data").child(teamNumber)
//
//    var uniqueCode: String
//    do {
//        uniqueCode = generateAlphanumericCode(6)
//    } while (codeExistsInFirebase(directory, uniqueCode))
//
//    val codeFile = directory.child("code.txt")
//    codeFile.putBytes(uniqueCode.toByteArray()).await()
//    return uniqueCode
//}
//
//fun generateAlphanumericCode(codeLength: Int): String {
//    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
//    val random = SecureRandom()
//    val code = StringBuilder(codeLength)
//
//    for (i in 0 until codeLength) {
//        code.append(chars[random.nextInt(chars.length)])
//    }
//
//    return code.toString()
//}
//
//suspend fun codeExistsInFirebase(directory: StorageReference, code: String): Boolean {
//    val codeFile = directory.child("code.txt")
//    return try {
//        val existingCode = codeFile.getBytes(Long.MAX_VALUE).await().toString(Charsets.UTF_8)
//        existingCode == code
//    } catch (e: StorageException) {
//        if (e.httpResultCode == 404) {
//            false
//        } else {
//            throw e
//        }
//    }
//}

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