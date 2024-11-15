package com.ndmq.moneynote.presentation.setting

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.PeriodicMoney.Companion.BEFORE_DATE
import com.ndmq.moneynote.data.model.PeriodicMoney.Companion.DO_NO_THING
import com.ndmq.moneynote.data.source.categories
import com.ndmq.moneynote.data.source.defaultExpenseCategory
import com.ndmq.moneynote.data.source.defaultIncomeCategory
import com.ndmq.moneynote.data.source.frequencies
import com.ndmq.moneynote.databinding.FragmentFixedCostBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.presentation.add_note.DatePickerListener
import com.ndmq.moneynote.presentation.setting.dialog.SelectCategoryPopupWindow
import com.ndmq.moneynote.presentation.setting.dialog.SelectEndDatePopupWindow
import com.ndmq.moneynote.presentation.setting.dialog.SelectFrequencyPopupWindow
import com.ndmq.moneynote.presentation.setting.dialog.SelectOnSaturdayOrSundayPopupWindow
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.fullFormattedDate
import java.util.Calendar
import java.util.Date

class FixedCostFragment : Fragment() {

    private val binding by lazy { FragmentFixedCostBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<FixedCostViewModel>()

    private val selectCategoryPopupWindow by lazy { SelectCategoryPopupWindow(requireContext()) }
    private val selectFrequencyPopupWindow by lazy { SelectFrequencyPopupWindow(requireContext()) }
    private val selectEndDatePopupWindow by lazy { SelectEndDatePopupWindow(requireContext()) }
    private val selectOnSaturdayOrSundayPopupWindow by lazy {
        SelectOnSaturdayOrSundayPopupWindow(requireContext())
    }

    private val startDatePickerListener = DatePickerListener()
    private val endDatePickerListener = DatePickerListener()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        handleEvent()
        observeData()
    }

    private fun initView() {
        scrollLongText()
        initBottomNavBarIcon()
        initPopupCategoriesData()
        initPopupFrequenciesData()
    }

    private fun handleEvent() {
        binding.btnExpense.setOnClickListener {
            viewModel.categoryType.value = 1
            viewModel.selectedCategory.value = defaultExpenseCategory
        }

        binding.btnIncome.setOnClickListener {
            viewModel.categoryType.value = 2
            viewModel.selectedCategory.value = defaultIncomeCategory
        }

        binding.btnSelectCategory.setOnClickListener {
            selectCategoryPopupWindow.apply {
                if (isShowing) dismiss() else showAsDropDown(it)
            }
        }

        binding.btnSelectFrequency.setOnClickListener {
            selectFrequencyPopupWindow.apply {
                if (isShowing) dismiss() else showAsDropDown(it)
            }
        }

        binding.btnEndDate.setOnClickListener {
            selectEndDatePopupWindow.apply {
                if (isShowing) dismiss() else showAsDropDown(it)
            }
        }

        binding.btnOnSaturdayAndSunday.setOnClickListener {
            selectOnSaturdayOrSundayPopupWindow.apply {
                if (isShowing) dismiss() else showAsDropDown(it)
            }
        }

        startDatePickerListener.setOnDateSelected {
            viewModel.startDate.value = it
        }

        binding.btnStartDate.setOnClickListener {
            val calendar = Calendar.getInstance().apply {
                time = viewModel.startDate.value ?: Date()
            }
            DatePickerDialog(
                requireContext(),
                R.style.DatePickerDialogTheme,
                startDatePickerListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        selectCategoryPopupWindow.setOnCategorySelected {
            viewModel.selectedCategory.value = it
            selectCategoryPopupWindow.dismiss()
        }

        selectFrequencyPopupWindow.setOnFrequencySelected {
            viewModel.frequency.value = it
            selectFrequencyPopupWindow.dismiss()
        }

        selectEndDatePopupWindow.apply {
            endDatePickerListener.setOnDateSelected {
                viewModel.endDate.value = it
                dismiss()
            }

            setOnNoneButtonClick {
                viewModel.endDate.value = null
                dismiss()
            }

            setOnSelectDateButtonClick {
                val calendar = Calendar.getInstance().apply {
                    time = viewModel.startDate.value ?: Date()
                }
                DatePickerDialog(
                    requireContext(),
                    R.style.DatePickerDialogTheme,
                    endDatePickerListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        selectOnSaturdayOrSundayPopupWindow.apply {
            setOnNothingButtonClick {
                viewModel.onSaturdayOrSunday.value = 1
                dismiss()
            }

            setOnBeforeButtonClick {
                viewModel.onSaturdayOrSunday.value = 2
                dismiss()
            }

            setOnAfterButtonClick {
                viewModel.onSaturdayOrSunday.value = 3
                dismiss()
            }
        }
    }

    private fun observeData() {
        observeCategoryType()
        observeSelectedCategory()
        observeSelectedFrequency()
        observeDate()
        observeOnSaturdayAndSunday()
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.FIXED_COST)
    }

    private fun scrollLongText() {
        binding.tvTitleOnSaturday.isSelected = true
    }

    private fun initPopupCategoriesData() {
        selectCategoryPopupWindow.setDataList(categories)
    }

    private fun initPopupFrequenciesData() {
        selectFrequencyPopupWindow.setDataList(frequencies)
    }

    private fun observeCategoryType() {
        viewModel.categoryType.observe(viewLifecycleOwner) { categoryType ->
            when (categoryType) {
                1 -> {
                    binding.tvExpense.setTextColor(getColor(requireContext(), R.color.primaryColor))
                    binding.vExpense.setBackgroundResource(R.color.primaryColor)
                    binding.tvIncome.setTextColor(
                        getColor(
                            requireContext(),
                            R.color.defaultIconColor
                        )
                    )
                    binding.vIncome.setBackgroundResource(R.color.transparent)
                }

                2 -> {
                    binding.tvIncome.setTextColor(getColor(requireContext(), R.color.primaryColor))
                    binding.vIncome.setBackgroundResource(R.color.primaryColor)
                    binding.tvExpense.setTextColor(
                        getColor(
                            requireContext(),
                            R.color.defaultIconColor
                        )
                    )
                    binding.vExpense.setBackgroundResource(R.color.transparent)
                }
            }

            selectCategoryPopupWindow.setDataList(categories.filter { it.categoryType == categoryType })
        }
    }

    private fun observeSelectedCategory() {
        viewModel.selectedCategory.observe(viewLifecycleOwner) {
            binding.ivCategoryImage.setColorFilter(it.tintColor)
            binding.ivCategoryImage.setImageResource(it.iconResource)
            binding.tvSelectedCategory.text = it.categoryName
        }
    }

    private fun observeSelectedFrequency() {
        viewModel.frequency.observe(viewLifecycleOwner) {
            binding.tvFrequency.text = it.content
        }
    }

    private fun observeDate() {
        viewModel.startDate.observe(viewLifecycleOwner) {
            binding.tvStartDate.text = fullFormattedDate(it)
        }

        viewModel.endDate.observe(viewLifecycleOwner) {
            binding.tvEndDate.text =
                if (it == null) getString(R.string.note) else fullFormattedDate(it)
        }
    }

    private fun observeOnSaturdayAndSunday() {
        viewModel.onSaturdayOrSunday.observe(viewLifecycleOwner) {
            binding.tvOnSaturdayAndSunday.text = when (it) {
                DO_NO_THING -> getString(R.string.do_nothing)
                BEFORE_DATE -> getString(R.string.set_the_start_date_as_the_before_date)
                else -> getString(R.string.set_the_start_date_as_the_after_date)
            }
        }
    }
}
