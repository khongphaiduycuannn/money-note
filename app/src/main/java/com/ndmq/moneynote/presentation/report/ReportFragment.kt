package com.ndmq.moneynote.presentation.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.dto.toListCategoryNotes
import com.ndmq.moneynote.data.model.dto.toListChartPart
import com.ndmq.moneynote.databinding.FragmentReportBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.presentation.add_note.SelectTimeDialog
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.constant.formatNumberWithDots
import com.ndmq.moneynote.utils.getFirstDayOfMonth
import com.ndmq.moneynote.utils.getNextMonth
import com.ndmq.moneynote.utils.getPreviousMonth
import com.ndmq.moneynote.utils.monthYearFormattedDate
import com.ndmq.moneynote.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private val binding by lazy { FragmentReportBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<ReportViewModel>()

    private val reportCategoryAdapter = ReportCategoryAdapter()

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
        initReportCategoryList()
    }

    private fun handleEvent() {
        binding.tvSelectedDate.setOnClickListener {
            selectTimeDialog.show()
        }

        selectTimeDialog.onConfirmButtonClick = { month, year ->
            viewModel.currentMonth.value = YearMonth.of(year, month)
        }

        binding.ivSearch.setOnClickListener {
            navigateTo(Screen.SEARCH, R.id.action_reportFragment_to_searchFragment)
        }

        binding.btnNextDay.setOnClickListener {
            viewModel.currentMonth.value = viewModel.currentMonth.value?.let { month ->
                getNextMonth(month)
            }
        }

        binding.btnPrevDay.setOnClickListener {
            viewModel.currentMonth.value = viewModel.currentMonth.value?.let { month ->
                getPreviousMonth(month)
            }
        }

        binding.btnExpense.setOnClickListener {
            viewModel.categoryType.value = 1
        }

        binding.btnIncome.setOnClickListener {
            viewModel.categoryType.value = 2
        }

        binding.reportChart.setOnPartSelected { chartPart ->

        }
    }

    private fun observeData() {
        observeCurrentMonth()
        observeNotesList()
        observeCategoryType()
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.REPORT)
    }

    private fun initReportCategoryList() {
        binding.rclExpense.layoutManager = LinearLayoutManager(requireContext())
        binding.rclExpense.adapter = reportCategoryAdapter
    }

    private fun observeCurrentMonth() {
        viewModel.currentMonth.observe(viewLifecycleOwner) {
            binding.tvSelectedDate.text = monthYearFormattedDate(getFirstDayOfMonth(it))
            viewModel.fetchNotes()
        }
    }

    private fun observeNotesList() {
        viewModel.notes.observe(viewLifecycleOwner) {
            val expense = it
                .filter { note -> note.category.categoryType == 1 }
                .sumOf { note -> note.expense }
            val income = it
                .filter { note -> note.category.categoryType == 2 }
                .sumOf { note -> note.expense }
            val total = income - expense

            with(binding) {
                tvIncome.text = "${formatNumberWithDots(income)} $"
                tvExpense.text = "-${formatNumberWithDots(expense)} $"
                tvTotal.text = "${formatNumberWithDots(total)} $"
                tvTotal.setTextColor(
                    if (total >= 0) getColor(requireContext(), R.color.primaryColor)
                    else getColor(requireContext(), R.color.errorColor)
                )

                tvNoData.visibility =
                    if (it.none { note -> note.category.categoryType == viewModel.categoryType.value }) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

                val filteredNotes =
                    it.filter { note -> note.category.categoryType == viewModel.categoryType.value }
                reportChart.setChartParts(filteredNotes.toListCategoryNotes().toListChartPart())
                reportCategoryAdapter.setDataList(filteredNotes)
            }
        }
    }

    private fun observeCategoryType() {
        viewModel.categoryType.observe(viewLifecycleOwner) { categoryType ->
            when (categoryType) {
                1 -> {
                    binding.tvExpenseTitle.setTextColor(
                        getColor(
                            requireContext(),
                            R.color.primaryColor
                        )
                    )
                    binding.vExpense.setBackgroundResource(R.color.primaryColor)
                    binding.tvIncomeTitle.setTextColor(
                        getColor(
                            requireContext(),
                            R.color.defaultIconColor
                        )
                    )
                    binding.vIncome.setBackgroundResource(R.color.transparent)
                }

                2 -> {
                    binding.tvIncomeTitle.setTextColor(
                        getColor(
                            requireContext(),
                            R.color.primaryColor
                        )
                    )
                    binding.vIncome.setBackgroundResource(R.color.primaryColor)
                    binding.tvExpenseTitle.setTextColor(
                        getColor(
                            requireContext(),
                            R.color.defaultIconColor
                        )
                    )
                    binding.vExpense.setBackgroundResource(R.color.transparent)
                }
            }
            viewModel.fetchNotes()
        }
    }
}