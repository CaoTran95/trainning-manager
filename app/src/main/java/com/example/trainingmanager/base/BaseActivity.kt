package com.example.trainingmanager.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import java.util.*

abstract class BaseActivity<T : BaseViewModel, B : ViewDataBinding> : AppCompatActivity() {

    protected abstract val viewModel: T
    protected lateinit var binding: B

    abstract fun getViewBinding(): B

    open fun initialize() {}
    open fun observeViewModel() {}
    open fun viewBinding() {}
    open fun events() {}

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding().apply {
            lifecycleOwner = this@BaseActivity
        }
        setContentView(binding.root)
        initialize()
        observeViewModel()
        viewBinding()
        events()
    }
}