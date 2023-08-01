package com.example.trainingmanager.prefs

import android.content.Context
import android.content.SharedPreferences
import com.example.trainingmanager.base.BasePreference
import com.example.trainingmanager.ui.model.WorkoutOfDay
import com.example.trainingmanager.utils.fromJson
import com.google.gson.Gson
import org.json.JSONArray
import java.util.*

class AppPreferences(context: Context) : BasePreference(context, "app-pref") {
    companion object {
        private const val KEY_WORKOUT_DATA = "key-workout-data"
    }

    fun getWorkoutData() = getString(KEY_WORKOUT_DATA, "[]")?.let {
        Gson().fromJson<List<WorkoutOfDay>>(it)
    }

    fun saveWorkoutData(workoutData: List<WorkoutOfDay>) {
        putData(KEY_WORKOUT_DATA, Gson().toJson(workoutData))
    }

}