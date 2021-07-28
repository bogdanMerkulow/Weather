package com.example.application.adapters

import android.view.View

interface ViewHolderFactory<T> {
    fun create(view: View): ViewHolder<T>
}