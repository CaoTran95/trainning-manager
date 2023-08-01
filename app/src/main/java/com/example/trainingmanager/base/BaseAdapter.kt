package com.example.trainingmanager.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingmanager.utils.removeLast

abstract class BaseAdapter<T, V : ViewDataBinding> : RecyclerView.Adapter<BaseViewHolder<V>>() {

    var currentList = mutableListOf<T>()
        private set

    fun set(item: T, index: Int) {
        currentList[index] = item
        notifyItemChanged(index)
    }

    fun set(items: List<T>?) {
        if (items == null) return
        currentList.apply {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    /**
     * @param forceAnim: do animation of changed rows if needed
     */
    fun set(items: List<T>?, index: Int, forceAnim: Boolean = true) {
        if (items == null) return
        if (isValidPosition(index)) {
            currentList = currentList.removeLast(index)
        }
        currentList.apply {
            addAll(items)
            if (forceAnim) {
                notifyItemRangeChanged(index, items.size)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun add(item: T, index: Int) {
        currentList.add(index, item)
        notifyItemInserted(index)
    }

    fun add(items: List<T>?) {
        if (items == null) return
        val currentSize = currentList.size
        currentList.addAll(items)
        notifyItemRangeInserted(currentSize, items.size)
    }

    fun addFirst(items: List<T>?) {
        if (items == null) return
        currentList.addAll(0, items)
        notifyItemRangeInserted(0, items.size)
    }

    fun remove(position: Int) {
        if (isValidPosition(position).not()) return
        currentList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun getItem(position: Int): T {
        return currentList[position]
    }

    fun getSafetyItem(position: Int): T? {
        if (isValidPosition(position).not()) return null
        return currentList[position]
    }

    fun removeAll() {
        if (currentList.isNullOrEmpty()) return
        currentList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    private fun isValidPosition(position: Int): Boolean {
        return currentList.size > position
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        val binding = createBinding(parent, viewType)
        val holder = BaseViewHolder(binding)
        registerEvents(holder)
        return holder
    }

    final override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        bind(holder.binding, position)
        holder.binding.executePendingBindings()
    }

    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): V
    protected abstract fun bind(binding: V, position: Int)
    protected open fun registerEvents(holder: BaseViewHolder<V>) {}
}
