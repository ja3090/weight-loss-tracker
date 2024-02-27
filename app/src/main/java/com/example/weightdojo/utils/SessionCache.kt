package com.example.weightdojo.utils


import android.content.SharedPreferences
import com.example.weightdojo.database.models.Config
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

interface SessionCache <T> {

    fun saveSession(session: T?)

    fun getActiveSession(): T?

    fun clearSession()
}

class ConfigSessionCache (
    private val sharedPreferences: SharedPreferences
): SessionCache<Config> {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter = moshi.adapter(Config::class.java)

    override fun saveSession(session: Config?) {
        sharedPreferences.edit()
            .putString("config", adapter.toJson(session))
            .apply()
    }

    override fun getActiveSession(): Config? {
        val json = sharedPreferences.getString("config", null) ?: return null
        return adapter.fromJson(json)
    }

    override fun clearSession() {
        sharedPreferences.edit().remove("config").apply()
    }
}