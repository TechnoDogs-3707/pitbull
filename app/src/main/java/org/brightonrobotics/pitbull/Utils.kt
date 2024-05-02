package org.brightonrobotics.pitbull

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.*

suspend fun generateUniqueJoinCode(teamNumber: String): String {
    val storage = Firebase.storage("gs://pitbull-421814.appspot.com")
    val directory = storage.reference.child("data").child(teamNumber)

    var uniqueCode: String
    do {
        uniqueCode = "${System.currentTimeMillis()}${Random().nextInt(999)}"
    } while (codeExistsInFirebase(directory, uniqueCode))

    val codeFile = directory.child("code.txt")
    codeFile.putBytes(uniqueCode.toByteArray()).await()
    return uniqueCode
}

suspend fun codeExistsInFirebase(directory: StorageReference, code: String): Boolean {
    val codeFile = directory.child("code.txt")
    return try {
        val existingCode = codeFile.getBytes(Long.MAX_VALUE).await().toString(Charsets.UTF_8)
        existingCode == code
    } catch (e: StorageException) {
        if (e.httpResultCode == 404) {
            // File does not exist, return false
            false
        } else {
            // Other exceptions, rethrow them
            throw e
        }
    }
}