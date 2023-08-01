package com.example.trainingmanager.usecase

import com.example.trainingmanager.base.BaseUseCase
import com.example.trainingmanager.base.ResultApi
import com.example.trainingmanager.data.model.BaseData
import com.example.trainingmanager.data.model.WorkoutData
import com.example.trainingmanager.repository.AppRepository
import com.example.trainingmanager.utils.runCatching
import javax.inject.Inject

class GetWorkoutsUseCase @Inject constructor(
    private val appRepository: AppRepository
) : BaseUseCase<ResultApi<BaseData<WorkoutData>>>() {
    override suspend fun execute(vararg params: Any): ResultApi<BaseData<WorkoutData>> {
        return runCatching { appRepository.getWorkouts() }
    }
}