package com.example.application.models

interface OnItemClickListener<T> {
    fun onItemClick(data: T)
}