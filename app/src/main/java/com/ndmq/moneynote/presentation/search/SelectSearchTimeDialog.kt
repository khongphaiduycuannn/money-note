package com.ndmq.moneynote.presentation.search

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndmq.moneynote.databinding.DialogSelectSearchTimeBinding

class SelectSearchTimeDialog(
    private val context: Context,
    private val years: List<String>,
    private val onTimeSelected: (Int?) -> Unit
) : Dialog(context) {

    private val binding by lazy {
        DialogSelectSearchTimeBinding.inflate(LayoutInflater.from(context))
    }

    private val searchTimeAdapter = SearchTimeAdapter()

    init {
        setContentView(binding.root)
        initWindow()
        initView()
    }

    private fun initWindow() {
        window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.apply {
                y = -170
                gravity = Gravity.CENTER
            }
        }
    }

    private fun initView() {
        binding.rclTime.layoutManager = LinearLayoutManager(context)
        binding.rclTime.adapter = searchTimeAdapter
        searchTimeAdapter.setDataList(years)
        searchTimeAdapter.setOnTimeSelected {
            onTimeSelected(it)
            dismiss()
        }
    }
}