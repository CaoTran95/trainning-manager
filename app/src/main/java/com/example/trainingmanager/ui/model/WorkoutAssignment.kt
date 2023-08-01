package com.example.trainingmanager.ui.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.trainingmanager.R
import com.example.trainingmanager.data.model.Assignment
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutAssignment(
    val id: String = "",
    val status: WorkoutStatus = WorkoutStatus.DEFAULT,
    val title: String = "",
    val exercisesCount: String = "",
    val dayState: DayState,
    val isVisibleStatus: Boolean = false,
    val isVisibleDot: Boolean = false,
    var isCheckMark: Boolean = false
): Parcelable {
    companion object {
        fun fromJson(json: String?): WorkoutAssignment? {
            return try {
                json ?: throw Exception("pref: no user data")
                Gson().fromJson(json, WorkoutAssignment::class.java)
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

fun Assignment.toWorkoutAssignmentModel(dayState: DayState) : WorkoutAssignment {
    val status = when {
        status == 0 && dayState == DayState.PAST-> WorkoutStatus.MISSED
        status == 0 && dayState == DayState.TODAY-> WorkoutStatus.ASSIGNED
        status == 0 && dayState == DayState.FUTURE-> WorkoutStatus.GREYED_OUT
        status == 2 -> WorkoutStatus.COMPLETED
        else -> WorkoutStatus.DEFAULT
    }
    return WorkoutAssignment(
        id = id.orEmpty(),
        status = status,
        title = title.orEmpty(),
        exercisesCount = exercises_count?.toString().orEmpty(),
        dayState = dayState,
        isVisibleStatus = status == WorkoutStatus.MISSED || status == WorkoutStatus.COMPLETED,
        isVisibleDot = status == WorkoutStatus.MISSED
    )
}

enum class WorkoutStatus(@StringRes val stringResId: Int) {
    MISSED(R.string.missed), ASSIGNED(R.string.assigned), COMPLETED(R.string.completed), GREYED_OUT(R.string.greyed_out), DEFAULT(R.string.tx_default)
}
