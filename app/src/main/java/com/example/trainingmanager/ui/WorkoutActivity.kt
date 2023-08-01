package com.example.trainingmanager.ui

import com.example.trainingmanager.base.BaseActivity
import com.example.trainingmanager.databinding.WorkoutActivityBinding
import androidx.activity.viewModels
import com.example.trainingmanager.ui.adapter.DayOfWeekAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutActivity: BaseActivity<WorkoutViewModel, WorkoutActivityBinding>() {

    override val viewModel: WorkoutViewModel by viewModels()

    override fun getViewBinding(): WorkoutActivityBinding {
        return WorkoutActivityBinding.inflate(layoutInflater)
    }

    private val dayOfWeekAdapter: DayOfWeekAdapter by lazy {
        DayOfWeekAdapter {
            viewModel.checkMarkAssignment(it)
        }
    }

    override fun initialize() {
        super.initialize()
        initDayOfWeekRecyclerView()
    }

    private fun initDayOfWeekRecyclerView() {
        with(binding.rcvDayOfWeek) {
            adapter = dayOfWeekAdapter
            setHasFixedSize(true)
        }
    }

    override fun observeViewModel() {
        super.observeViewModel()
        viewModel.getWorkoutsObs.observe(this) {
            dayOfWeekAdapter.set(it)
            binding.swipeContainer.isRefreshing = false
        }
    }

    override fun events() {
        super.events()
        binding.swipeContainer.setOnRefreshListener {
            viewModel.updateData()
        }
    }

}