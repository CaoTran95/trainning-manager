package com.example.trainingmanager.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trainingmanager.base.BaseViewModel
import com.example.trainingmanager.prefs.AppPreferences
import com.example.trainingmanager.ui.model.WorkoutOfDay
import com.example.trainingmanager.ui.model.toWorkoutOfDayModel
import com.example.trainingmanager.usecase.GetWorkoutsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val appPreferences: AppPreferences
): BaseViewModel() {

    private val _getWorkoutsObs = MutableLiveData<List<WorkoutOfDay>>()
    val getWorkoutsObs: LiveData<List<WorkoutOfDay>> = _getWorkoutsObs

    init {
        appPreferences.getWorkoutData()?.let {
            if (it.isEmpty()) return@let null
            _getWorkoutsObs.postValue(it)
        } ?: updateData()
    }

    fun updateData() {
        createListDayOfWeek()
        getWorkouts()
    }

    fun checkMarkAssignment(id: String) {
        if (id.isEmpty()) return
        _getWorkoutsObs.value?.apply {
            flatMap { it.assignments }
                .find { it.id == id }
                ?.let {
                    it.isCheckMark = !it.isCheckMark
                }
        }?.let {
            _getWorkoutsObs.postValue(it)
            appPreferences.saveWorkoutData(it)
        }
    }

    private fun createListDayOfWeek() {
        val format: DateFormat = SimpleDateFormat("EEE")
        val calendar: Calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val days = mutableListOf<WorkoutOfDay>()
        for (i in 0..6) {
            val isToday = calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            days.add(
                WorkoutOfDay(
                    numberOfDay = "%02d".format(calendar.get(Calendar.DAY_OF_MONTH)),
                    nameOfDay = format.format(calendar.time),
                    isToday = isToday
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        _getWorkoutsObs.postValue(days)
    }

    private fun getWorkouts() {
        launchCoroutine {
            getWorkoutsUseCase.execute().subscribe(
                onSuccess = {
                    it?.data?.let { workouts ->
                        if (workouts.isEmpty()) return@let
                        workouts.mapIndexed { index, workoutData ->
                            workoutData.toWorkoutOfDayModel(index).apply {
                                val oldData = _getWorkoutsObs.value?.get(index)
                                nameOfDay = oldData?.nameOfDay.orEmpty()
                                numberOfDay = oldData?.numberOfDay.orEmpty()
                                isToday = oldData?.isToday ?: false
                            }
                        }.let { workoutData ->
                            _getWorkoutsObs.postValue(workoutData)
                            appPreferences.saveWorkoutData(workoutData)
                        }
                    }
                },
                onError = {
                    Log.DEBUG
                }
            )
        }
    }

}