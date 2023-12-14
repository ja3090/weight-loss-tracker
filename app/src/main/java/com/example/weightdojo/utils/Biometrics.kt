package com.example.weightdojo.utils

import android.os.Build
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import kotlin.reflect.KSuspendFunction0

class Biometrics {
    companion object {
        fun canUseBiometrics(context: FragmentActivity): Boolean {
            val biometricManager = BiometricManager.from(context)
            val canAuthenticateWithBiometrics =
                when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                    BiometricManager.BIOMETRIC_SUCCESS -> true
                    else -> {
                        Log.d("TAG", "Device does not support strong biometric authentication")
                        false
                    }
                }

            return canAuthenticateWithBiometrics
        }
        fun authenticateWithBiometric(
            context: FragmentActivity,
            onSuccess: () -> Unit,
            onError: () -> Unit,
            onFail: () -> Unit,
        ) {
            val executor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.mainExecutor
            } else {
                TODO("VERSION.SDK_INT < P")
            }
            val biometricPrompt = BiometricPrompt(
                context,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        onSuccess()
                        Log.d("TAG", "Authentication successful!!!")
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        onError()
                        Log.e("TAG", "onAuthenticationError")
                    }

                    override fun onAuthenticationFailed() {
                        onFail()
                        Log.e("TAG", "onAuthenticationFailed")
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setDescription("Place your finger the sensor or look at the front camera to authenticate.")
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }
}