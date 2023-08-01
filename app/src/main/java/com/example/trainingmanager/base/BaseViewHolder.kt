package com.example.trainingmanager.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<out VB : ViewDataBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root)
