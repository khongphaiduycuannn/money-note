package com.ndmq.moneynote.presentation.edit_note

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.source.in_memory.categories
import com.ndmq.moneynote.databinding.FragmentEditNoteBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.presentation.add_note.CategoryAdapter
import com.ndmq.moneynote.presentation.add_note.DatePickerListener
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.fullFormattedDate
import com.ndmq.moneynote.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

    private val binding by lazy { FragmentEditNoteBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<EditNoteViewModel>()

    private val categoryAdapter = CategoryAdapter()

    private val datePickerListener = DatePickerListener()

    var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        handleEvent()
        observeData()
    }

    private fun initData() {
        viewModel.fetchData()

        note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("NOTE", Note::class.java)
        } else {
            arguments?.getParcelable<Note>("NOTE")
        }?.also { note ->
            viewModel.id = note.id
            viewModel.categoryType.value = note.category.categoryType
            viewModel.selectedDate.value = note.createdDate
            viewModel.selectedCategory.value = note.category
            viewModel.fixedCostId = note.fixedCostId
            binding.edtNote.setText(note.note)
            binding.edtExpense.setText(note.expense.toString())
        }
    }

    private fun initView() {
        initBottomNavBarIcon()
        initEditView()
        initCategoryListView()
    }

    private fun handleEvent() {
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

        categoryAdapter.onCategoryClick = { category ->
            if (category == null) {
                navigateTo(Screen.CATEGORIES, R.id.action_editNoteFragment_to_categoriesFragment)
            } else {
                viewModel.selectedCategory.value = category
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvSubmit.setOnClickListener {
            if (viewModel.fixedCostId != null) {
                Toast.makeText(
                    requireContext(),
                    "Can't edit fixed expense/income",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            viewModel.saveNote(
                binding.edtNote.text.toString(),
                binding.edtExpense.text.toString()
            ) {
                findNavController().navigateUp()
            }
        }

        binding.ivSaveNote.setOnClickListener {
            if (viewModel.fixedCostId != null) {
                Toast.makeText(
                    requireContext(),
                    "Can't edit fixed expense/income",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            viewModel.saveNote(
                binding.edtNote.text.toString(),
                binding.edtExpense.text.toString()
            ) {
                findNavController().navigateUp()
            }
        }

        binding.tvDelete.setOnClickListener {
            if (viewModel.fixedCostId != null) {
                Toast.makeText(
                    requireContext(),
                    "Can't edit fixed expense/income",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            viewModel.deleteNote {
                findNavController().navigateUp()
            }
        }

        binding.tvCopy.setOnClickListener {
            if (viewModel.fixedCostId != null) {
                Toast.makeText(
                    requireContext(),
                    "Can't edit fixed expense/income",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            navigateTo(
                Screen.EDIT_NOTE, R.id.action_editNoteFragment_self,
                bundleOf("NOTE" to note?.apply {
                    id = null
                    fixedCostId = null
                })
            )
        }
    }

    private fun observeData() {
        observeCategory()
        observeCategoryType()
        observeSelectedDate()
        observeSelectedCategory()
        observeError()
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.EDIT_NOTE)
    }

    private fun initEditView() {
        if (viewModel.id == null) {
            binding.tvTitle.text = getString(R.string.clone_note)
            binding.llBottomBar.visibility = View.GONE
            binding.tvSubmit.text = getString(R.string.submit)
        }
    }

    private fun initCategoryListView() {
        binding.rclCategories.adapter = categoryAdapter
        categoryAdapter.setCategoryList(categories)
    }

    private fun observeCategory() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            if (categories.isNullOrEmpty()) return@observe

            val categoryType = viewModel.categoryType.value ?: 1
            val filteredCategories = categories.filter { it.categoryType == categoryType }
            categoryAdapter.setCategoryList(filteredCategories)
        }
    }

    private fun observeCategoryType() {
        viewModel.categoryType.observe(viewLifecycleOwner) { categoryType ->
            when (categoryType) {
                1 -> binding.tvExpense.text = getString(R.string.expense)
                2 -> binding.tvExpense.text = getString(R.string.income)
            }
            viewModel.categories.value = viewModel.categories.value
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