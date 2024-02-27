package com.example.weightdojo.datatransferobjects

data class RepoResponse<T>(
    val success: Boolean,
    val data: T,
    val errorMessage: String? = null
)

fun <Data> repoWrapper(dbCall: () -> Data): RepoResponse<Data?> {
    return try {
        RepoResponse(
            success = true,
            data = dbCall(),
            errorMessage = null
        )
    } catch (e: Exception) {
        RepoResponse(
            success = false,
            data = null,
            errorMessage = e.message
        )
    }
}