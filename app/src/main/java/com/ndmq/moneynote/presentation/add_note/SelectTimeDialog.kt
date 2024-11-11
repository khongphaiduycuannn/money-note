package com.ndmq.moneynote.presentation.add_note

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.view.forEach
import com.ndmq.moneynote.databinding.DialogSelectCalendarTimeBinding

class SelectTimeDialog(
    private val context: Context,
    var onConfirmButtonClick: (month: Int, year: Int) -> Unit = { _, _ -> }
) : Dialog(context) {

    private val binding by lazy {
        DialogSelectCalendarTimeBinding.inflate(LayoutInflater.from(context))
    }

    private var selectedYear = Calendar.getInstance().get(Calendar.YEAR)
    private var selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    init {
        setContentView(binding.root)
        initView()
        handleEvent()
    }

    private fun initView() {
        initWindow()

        with(binding) {
            tvSelectedDate.text = selectedYear.toString()
            mapMonthToRadioButton(selectedMonth)
        }
    }

    private fun handleEvent() {
        with(binding) {
            rgMonth.forEach { linear ->
                (linear as LinearLayout).forEach { rbutton ->
                    rbutton.setOnClickListener {
                        val id = rbutton.id
                        (rbutton as RadioButton).isChecked = true
                        clearChecked(id)
                        selectedMonth = mapRadioButtonToMonth(id)
                    }
                }
            }

            btnPrevYear.setOnClickListener {
                if (selectedYear > 1995) {
                    selectedYear--
                    tvSelectedDate.text = selectedYear.toString()
                }
            }

            btnNextYear.setOnClickListener {
                if (selectedYear < 2045) {
                    selectedYear++
                    tvSelectedDate.text = selectedYear.toString()
                }
            }

            btnCancel.setOnClickListener {
                dismiss()
            }

            btnOk.setOnClickListener {
                onConfirmButtonClick(selectedMonth, selectedYear)
                dismiss()
            }
        }
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

    private fun mapMonthToRadioButton(month: Int) {
        when (month) {
            1 -> binding.rb1.isChecked = true
            2 -> binding.rb2.isChecked = true
            3 -> binding.rb3.isChecked = true
            4 -> binding.rb4.isChecked = true
            5 -> binding.rb5.isChecked = true
            6 -> binding.rb6.isChecked = true
            7 -> binding.rb7.isChecked = true
            8 -> binding.rb8.isChecked = true
            9 -> binding.rb9.isChecked = true
            10 -> binding.rb10.isChecked = true
            11 -> binding.rb11.isChecked = true
            12 -> binding.rb12.isChecked = true
        }
    }

    private fun mapRadioButtonToMonth(rbId: Int): Int {
        return when (rbId) {
            binding.rb1.id -> 1
            binding.rb2.id -> 2
            binding.rb3.id -> 3
            binding.rb4.id -> 4
            binding.rb5.id -> 5
            binding.rb6.id -> 6
            binding.rb7.id -> 7
            binding.rb8.id -> 8
            binding.rb9.id -> 9
            binding.rb10.id -> 10
            binding.rb11.id -> 11
            binding.rb12.id -> 12
            else -> 12
        }
    }

    private fun clearChecked(id: Int) {
        binding.rgMonth.forEach { linear ->
            (linear as LinearLayout).forEach { rbutton ->
                if (rbutton.id != id) (rbutton as RadioButton).isChecked = false
            }
        }
    }
}