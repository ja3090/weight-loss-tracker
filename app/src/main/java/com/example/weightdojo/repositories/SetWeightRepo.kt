package com.example.weightdojo.repositories

import android.util.Log
import com.example.weightdojo.MyApp
import com.example.weightdojo.database.dao.ConfigDao
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.database.models.Sex
import com.example.weightdojo.screens.home.addmodal.addweight.ExtraOptions
import com.example.weightdojo.utils.ConfigSessionCache
import com.example.weightdojo.utils.WeightUnit
import com.example.weightdojo.utils.WeightUnits

interface SetWeightRepo {
    fun setWeight(dayId: Long, weight: Float, configExtraOptions: ExtraOptions, configId: Long)
}

class SetWeightRepoImpl(
    private val configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    private val dayDao: DayDao,
    private val configDao: ConfigDao
) : SetWeightRepo {

    override fun setWeight(
        dayId: Long, weight: Float, configExtraOptions: ExtraOptions, configId: Long
    ) {
        dayDao.setWeight(dayId = dayId, weight = weight)

        val age = configExtraOptions.age
        val height = configExtraOptions.height

        configDao.setConfigExtraOptions(
            age = age?.toInt(),
            sex = configExtraOptions.sex,
            height = height?.toFloat(),
            id = configId,
        )

        val latestConfig = configDao.getConfig()

        configSessionCache.saveSession(latestConfig)
    }
}