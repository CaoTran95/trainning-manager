package com.example.trainingmanager.repository

import com.example.trainingmanager.data.model.BaseData
import com.example.trainingmanager.data.model.WorkoutData
import com.example.trainingmanager.service.AppService
import javax.inject.Inject

interface AppRepository {
    suspend fun getWorkouts(): BaseData<WorkoutData>
}

class AppRepositoryImpl @Inject constructor(
    private val appService: AppService
) : AppRepository {

    override suspend fun getWorkouts(): BaseData<WorkoutData> {
        return appService.getWorkouts()
    }

}