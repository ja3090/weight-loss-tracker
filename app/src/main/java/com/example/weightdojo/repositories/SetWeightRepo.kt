package com.example.weightdojo.repositories

import com.example.weightdojo.MyApp
import com.example.weightdojo.database.dao.DayDao
import com.example.weightdojo.screens.home.addmodal.addweight.ExtraOptions
import com.example.weightdojo.utils.ConfigSessionCache

interface SetWeightRepo {
    fun setWeight(dayId: Long, weight: Float, configExtraOptions: ExtraOptions)
}

class SetWeightRepoImpl(
    private val configSessionCache: ConfigSessionCache = MyApp.appModule.configSessionCache,
    private val dayDao: DayDao,
) : SetWeightRepo {

    override fun setWeight(
        dayId: Long, weight: Float, configExtraOptions: ExtraOptions
    ) {
        dayDao.setWeight(dayId = dayId, weight = weight)

        val age = configExtraOptions.age
        val height = configExtraOptions.height

        var config = configSessionCache.getActiveSession() ?: return

        config = config.copy(
            age = age?.toInt(),
            sex = configExtraOptions.sex,
            height = height?.toFloat(),
        )

        configSessionCache.saveSession(config)
    }
}