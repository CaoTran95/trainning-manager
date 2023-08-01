package com.example.trainingmanager.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.trainingmanager.R
import com.example.trainingmanager.base.BaseAdapter
import com.example.trainingmanager.databinding.ItemDayOfWeekBinding
import com.example.trainingmanager.databinding.ItemWorkoutContainerBinding
import com.example.trainingmanager.ui.model.WorkoutOfDay

class DayOfWeekAdapter(
    private val onViewClickListener: (String) -> Unit
): BaseAdapter<WorkoutOfDay, ItemDayOfWeekBinding>() {

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemDayOfWeekBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_day_of_week,
            parent,
            false
        )
    }

    override fun bind(binding: ItemDayOfWeekBinding, position: Int) {
        val day = getItem(position)
        binding.dayOfWeek = day
        binding.workoutExercises.removeAllViews()
        day.assignments.forEach { workoutContainer ->
            binding.workoutExercises.addView(
                ItemWorkoutContainerBinding.inflate(LayoutInflater.from(binding.root.context)).also {
                    it.workoutContainer = workoutContainer
                    it.root.setOnClickListener {
                        onViewClickListener.invoke(workoutContainer.id)
                    }
                }.root
            )
        }
    }
}