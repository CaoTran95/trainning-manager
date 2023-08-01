package com.example.trainingmanager.ui.model

import android.os.Parcelable
import com.example.trainingmanager.data.model.WorkoutData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class WorkoutOfDay (
    val id: String = "",
    var nameOfDay: String = "",
    var numberOfDay: String = "",
    val assignments: List<WorkoutAssignment> = listOf(),
    var isToday: Boolean = false
): Parcelable {
    companion object {
        fun fromJson(json: String?): WorkoutOfDay? {
            return try {
                json ?: throw Exception("pref: no user data")
                Gson().fromJson(json, WorkoutOfDay::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun toJson(): String? {
        return try {
            Gson().toJson(this)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun WorkoutData.toWorkoutOfDayModel(indexOfDay: Int) : WorkoutOfDay {
    return WorkoutOfDay(
        id = id ?: "",
        assignments = assignments.mapNotNull { workout ->
            val calendar = Calendar.getInstance()
            val dayInWeek = calendar.get(Calendar.DAY_OF_WEEK)
            workout?.id ?: return@mapNotNull null
            workout.toWorkoutAssignmentModel(
                try {
                    when {
                        (indexOfDay == 6 && dayInWeek == Calendar.SUNDAY) || ((indexOfDay + 2) == dayInWeek) -> DayState.TODAY
                        indexOfDay + 2 < dayInWeek -> DayState.PAST
                        else -> DayState.FUTURE
                    }
                } catch (e: java.lang.Exception) {
                    DayState.DEFAULT
                }
            )
        }
    )
}

enum class DayState {
    PAST, TODAY, FUTURE, DEFAULT
}