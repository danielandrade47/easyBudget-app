package br.com.orcamentofacil.ui.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import br.com.orcamentofacil.BR
import br.com.orcamentofacil.R

@SuppressLint("NotifyDataSetChanged")
open class GenericAdapter<T>(
    @LayoutRes val layoutId: Int,
    private val listener: ((T, View) -> Unit)? = null,
    private val delete: ((T) -> Unit)? = null
) : RecyclerView.Adapter<GenericAdapter.GenericViewHolder<T>>() {

    protected val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId, parent, false
        )
        return GenericViewHolder(binding, listener, delete)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }

    fun setupItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setupItemsAnimated(items: List<T>) {
        this.items.clear()
        items.forEach { item ->
            Handler(Looper.getMainLooper()).postDelayed({
                this.items.add(item)
                notifyItemInserted(this.items.indexOf(item))
            }, 1)
        }
    }

    fun removeItem(position: Int) {
        this.items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, this.items.size)
    }

    fun editItem(item: T, position: Int) {
        this.items[position] = item
        notifyItemChanged(position)
    }

    fun addItem(item: T) {
        this.items.add(item)
        notifyItemInserted(this.items.lastIndex)
    }

    fun getAdapterList(): MutableList<T> = this.items

    class GenericViewHolder<T>(
        private val binding: ViewDataBinding,
        private val listener: ((T, View) -> Unit)? = null,
        private val delete: ((T) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T) {
            binding.setVariable(BR.item, item)
            itemView.setOnClickListener { listener?.invoke(item, it) }
            val ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
            ivDelete?.setOnClickListener { delete?.invoke(item) }
            binding.executePendingBindings()
        }
    }
}