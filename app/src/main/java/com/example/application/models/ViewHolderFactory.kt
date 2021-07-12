package com.example.application.models

import android.view.View

interface ViewHolderFactory<T> {
    fun create(view: View): ViewHolder<T>
}