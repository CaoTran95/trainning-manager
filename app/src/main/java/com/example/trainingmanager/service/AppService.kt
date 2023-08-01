package com.example.trainingmanager.service

import com.example.trainingmanager.data.model.BaseData
import com.example.trainingmanager.data.model.WorkoutData
import retrofit2.http.GET

interface AppService {
    @GET("workouts")
    suspend fun getWorkouts(): BaseData<WorkoutData>
}