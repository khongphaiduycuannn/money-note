package com.ndmq.moneynote.presentation.add_note

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.source.in_memory.categories
import com.ndmq.moneynote.data.source.in_memory.defaultExpenseCategory
import com.ndmq.moneynote.data.source.in_memory.defaultIncomeCategory
import com.ndmq.moneynote.databinding.FragmentAddNoteBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.fullFormattedDate
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private val binding by lazy { FragmentAddNoteBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<AddNoteViewModel>()

    private val categoryAdapter = CategoryAdapter()

    private val datePickerListener = DatePickerListener()

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
        viewModel.categories = categories
    }

    private fun initView() {
        initBottomNavBarIcon()
        initCategoryListView()
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

        datePickerListener.setOnDateSelected {
            viewModel.selectedDate.value = it
        }

        binding.tvSelectedDate.setOnClickListener {
            val calendar = Calendar.getInstance().apply {
                time = viewModel.selectedDate.value ?: Date()
            }
            DatePickerDialog(
                requireContext(),
                R.style.DatePickerDialogTheme,
                datePickerListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnPrevDay.setOnClickListener {
            viewModel.moveToPrevDate()
        }

        binding.btnNextDay.setOnClickListener {
            viewModel.moveToNextDate()
        }

        binding.tvSubmit.setOnClickListener {
            viewModel.saveNote(binding.edtNote.text.toString(), binding.edtExpense.text.toString())
        }

        binding.ivSaveNote.setOnClickListener {
            viewModel.saveNote(binding.edtNote.text.toString(), binding.edtExpense.text.toString())
        }

        categoryAdapter.onCategoryClick = { category ->
            viewModel.selectedCategory.value = category
        }
    }

    private fun observeData() {
        observeCategoryType()
        observeSelectedDate()
        observeSelectedCategory()
        observeError()
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.ADD_NOTE)
    }

    private fun initCategoryListView() {
        binding.rclCategories.adapter = categoryAdapter
        categoryAdapter.setCategoryList(categories)
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

            categoryAdapter.setCategoryList(viewModel.categories.filter { it.categoryType == categoryType })
        }
    }

    private fun observeSelectedDate() {
        viewModel.selectedDate.observe(viewLifecycleOwner) {
            binding.tvSelectedDate.text = fullFormattedDate(it)
        }
    }

    private fun observeSelectedCategory() {
        viewModel.selectedCategory.observe(viewLifecycleOwner) {
            categoryAdapter.setSelectedCategory(it)
        }
    }

    private fun observeError() {
        viewModel.notify.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.notify.value = null
            }
        }
    }
}
