package com.example.application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.factories.ViewHolderFactory

abstract class WeatherRecyclerViewAdapter<T>() :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private var listener: (T) -> Unit? = {}

	constructor(listener: (T) -> Unit) : this() {
		this.listener = listener
	}

	private var items: List<T> = listOf()

	fun addItems(items: List<T>){
		this.items = items
		notifyDataSetChanged()
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val inflater = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		return getViewHolder(inflater, viewType, parent)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		(holder as Binder<T>).bind(items[position], listener)
	}

	override fun getItemViewType(position: Int): Int = getLayoutId(position, items[position])

	protected abstract fun getLayoutId(position: Int, obj: T): Int

	protected open fun getViewHolder(view: View, viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder{
		return ViewHolderFactory().create(view, viewType)
	}
	
	override fun getItemCount(): Int {
		return items.size
	}

	internal interface Binder<T>{
		fun bind(data: T, listener: (T) -> Unit?)
	}
}