package com.example.application.list.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.application.R
import com.example.application.list.viewmodels.WeatherListViewModel

class CityDialogFragment(private val viewModel: WeatherListViewModel): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.change_city)
        val dialogLayout =
            LayoutInflater.from(activity).inflate(R.layout.fragment_city_dialog, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.city_edit_text)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.enter) { _, _ ->
            viewModel.changeLocation(editText.text.toString())
        }

        return builder.create()
    }
}