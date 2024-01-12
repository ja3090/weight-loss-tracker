package com.example.weightdojo.screens.home.addmodal.addweight

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.AppConfig
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.database.models.Sex
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.repositories.SetWeightRepo
import com.example.weightdojo.repositories.SetWeightRepoImpl
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.WeightUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class AddWeightVM(
    private val database: AppDatabase = MyApp.appModule.database,
    private val dayData: DayData?,
    private val setWeightRepo: SetWeightRepo = SetWeightRepoImpl(
        dayDao = database.dayDao(),
        configDao = database.configDao()
    ),
    private val configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    private val config: Config? = configSessionCache.getActiveSession()
) : ViewModel() {

    var weight by mutableStateOf(initialWeight(dayData))

    val extraOptions by mutableStateOf(ExtraOptions(config))

    var error by mutableStateOf<String?>(null)

    private fun validateSubmission(): Boolean {
        if (weight.isEmpty()) {
            error = "Please enter your weight"
            return false
        }

        val allFieldsEntered =
            extraOptions.sex !== null && extraOptions.age !== null && extraOptions.height !== null

        val allFieldsEmpty =
            extraOptions.sex == null && extraOptions.age == null && extraOptions.height == null

        if (!allFieldsEntered && !allFieldsEmpty) {
            error = "If entering extra options, all fields must be entered"
            return false
        }

        return true
    }

    fun weightSetter(newWeight: String) {
        weight = newWeight
    }

    suspend fun submit(): Boolean {
        val passes = validateSubmission()

        if (!passes) return false

        val weightAtThisTime = weight

        if (dayData?.day?.id == null || weightAtThisTime.isEmpty() || config == null) {
            Log.e("Error", "One of the required values is null")
            return false
        }

        val weightToInternalUnit = WeightUnit.convert(
            from = config.weightUnit,
            to = AppConfig.internalDefaultWeightUnit,
            value = weightAtThisTime.toFloat()
        )


        val job = viewModelScope.async(Dispatchers.IO) {

            try {
                setWeightRepo.setWeight(
                    dayId = dayData.day.id,
                    weight = weightToInternalUnit,
                    configExtraOptions = extraOptions,
                    configId = config.id
                )

                return@async true

            } catch (e: Exception) {
                Log.e("Exception", e.message.toString())

                return@async false
            }
        }

        return job.await()
    }
}

class ExtraOptions(
    config: Config?,
) {
    var sex by mutableStateOf<Sex?>(null)
    var age by mutableStateOf<String?>(null)
    var height by mutableStateOf<String?>(null)

    init {
        if (config == null) {
            sex = null
            age = null
            height = null
        } else {
            sex = config.sex
            age = if (config.age == null) null else config.age.toString()
            height = if (config.height == null) null else config.height.toString()
        }
    }
}