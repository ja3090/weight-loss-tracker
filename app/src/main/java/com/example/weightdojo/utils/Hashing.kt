package com.example.weightdojo.utils

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class HashDetails(
    var hash: ByteArray,
    var salt: ByteArray
)

class Hashing {
    companion object {
        private fun generateRandomSalt(): ByteArray {
            val random = SecureRandom()
            val salt = ByteArray(16)
            random.nextBytes(salt)

            return salt
        }

        fun generateHashDetails(
            passcode: String,
            salt: ByteArray = generateRandomSalt()
        ): HashDetails {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
            val spec = PBEKeySpec(passcode.toCharArray(), salt, 120_000, 128)
            val key = factory.generateSecret(spec)

            return HashDetails(key.encoded, salt)
        }
    }
}