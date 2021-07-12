package com.example.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.models.ViewHolderFactory
import com.example.application.models.ViewHolder

abstract class RecyclerViewAdapter<T>(
	private val viewHolderFactory: ViewHolderFactory<T>,
	private val listener: (T) -> Unit = {}
	) :	RecyclerView.Adapter<ViewHolder<T>>() {

	private var items: List<T> = listOf()

	fun addItems(items: List<T>) {
		this.items = items
		notifyDataSetChanged()
	}

    protected abstract fun getLayoutId(viewType: Int): Int

	override fun getItemViewType(viewType: Int): Int = getLayoutId(viewType)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
		val inflater = LayoutInflater.from(parent.context)
			.inflate(viewType, parent, false)
		
		return viewHolderFactory.create(inflater)
	}

	override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
		holder.bind(items[position], listener)
	}

	override fun getItemCount(): Int {
		return items.size
	}
}