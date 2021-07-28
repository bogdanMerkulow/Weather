package com.example.application.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    open fun bind(data: T, listener: (T) -> Unit) {}
}