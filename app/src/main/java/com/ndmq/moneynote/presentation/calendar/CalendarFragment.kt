package com.ndmq.moneynote.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.model.dto.toListDateNotes
import com.ndmq.moneynote.databinding.FragmentCalendarBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.presentation.add_note.SelectTimeDialog
import com.ndmq.moneynote.utils.asDate
import com.ndmq.moneynote.utils.asLocalDate
import com.ndmq.moneynote.utils.asYearMonth
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.extension.scrollToNextMonth
import com.ndmq.moneynote.utils.extension.scrollToPrevMonth
import com.ndmq.moneynote.utils.getFirstDayOfMonth
import com.ndmq.moneynote.utils.monthYearFormattedDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment() {

    private val binding by lazy { FragmentCalendarBinding.inflate(layoutInflater) }

    private val calendarDayBinder by lazy { DayBinder(binding.calendar) }

    private val viewModel by viewModels<CalendarViewModel>()

    private val noteAdapter by lazy { DetailDateNotesAdapter() }

    private val selectTimeDialog by lazy { SelectTimeDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        initView()
        handleEvent()
        observeData()
    }

    private fun initData() {
        viewModel.fetchNotes()
    }

    private fun initView() {
        initBottomNavBarIcon()
        initCalendarTitle()
        initCalendarView()
        initNoteAdapter()
    }

    private fun handleEvent() {
        binding.tvSelectedDate.setOnClickListener {
            selectTimeDialog.show()
        }

        selectTimeDialog.onConfirmButtonClick = { month, year ->
            binding.calendar.scrollToMonth(YearMonth.of(year, month))
        }

        calendarDayBinder.setOnDateSelected { localDate ->
            viewModel.selectedDate.value = asDate(localDate)
        }

        binding.calendar.monthScrollListener = { month ->
            viewModel.currentMonth.value = month
        }

        binding.btnNextDay.setOnClickListener {
            binding.calendar.scrollToNextMonth()
        }

        binding.btnPrevDay.setOnClickListener {
            binding.calendar.scrollToPrevMonth()
        }
    }

    private fun observeData() {
        observeSelectedDate()
        observeCurrentMonth()
        observeNotesData()
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.CALENDAR)
    }

    private fun initCalendarTitle() {
        val daysOfWeek = daysOfWeek(firstDayOfWeekFromLocale())
        binding.titlesContainer.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }
    }

    private fun initCalendarView() {
        val currentMonth = asYearMonth(viewModel.selectedDate.value ?: Date())
        val startMonth = currentMonth.minusMonths(RANGE_MONTH)
        val endMonth = currentMonth.plusMonths(RANGE_MONTH)
        binding.calendar.apply {
            setup(startMonth, endMonth, firstDayOfWeekFromLocale())
            scrollToMonth(currentMonth)
            dayBinder = calendarDayBinder
        }
    }

    private fun initNoteAdapter() {
        binding.rclDetailDate.layoutManager = LinearLayoutManager(requireContext())
        binding.rclDetailDate.adapter = noteAdapter
    }

    private fun observeSelectedDate() {
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            calendarDayBinder.setSelectedDate(asLocalDate(date))
        }
    }

    private fun observeCurrentMonth() {
        viewModel.currentMonth.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvSelectedDate.text = monthYearFormattedDate(getFirstDayOfMonth(it))
                viewModel.fetchNotes()
            }
        }
    }

    private fun observeNotesData() {
        viewModel.notes.observe(viewLifecycleOwner) {
            calendarDayBinder.setNoteData(it)
            noteAdapter.setDateNotesList(it.toListDateNotes())
            setAmountView(it)
        }
    }

    private fun setAmountView(data: List<Note>) {
        val expense = data.filter { it.category.categoryType == 1 }.sumOf { it.expense }
        val income = data.filter { it.category.categoryType == 2 }.sumOf { it.expense }
        val total = income - expense

        with(binding) {
            tvExpense.text = "$expense $"
            tvIncome.text = "$income $"
            tvTotal.text = "$total $"
            tvTotal.setTextColor(
                if (total < 0) getColor(
                    requireContext(),
                    R.color.errorColor
                ) else getColor(requireContext(), R.color.primaryColor)
            )
        }
    }

    companion object {

        const val RANGE_MONTH = 500L
    }
}