package com.ndmq.moneynote.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.model.dto.toListDateNotes
import com.ndmq.moneynote.databinding.FragmentSearchBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.presentation.calendar.DetailDateNotesAdapter
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.constant.formatNumberWithDots
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<SearchViewModel>()

    private val noteAdapter by lazy { DetailDateNotesAdapter() }

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
        viewModel.fetchYearsList()
    }

    private fun initView() {
        initBottomNavBarIcon()
        initNoteAdapter()
    }

    private fun handleEvent() {
        binding.edtSearchKey.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.ivClearText.visibility = View.VISIBLE
                viewModel.fetchNotesByKeyword(text.toString())
            } else {
                binding.ivClearText.visibility = View.GONE
                viewModel.searchedNotes.value = listOf()
            }
        }

        binding.ivClearText.setOnClickListener {
            binding.edtSearchKey.text.clear()
        }

        binding.ivCalendar.setOnClickListener {
            SelectSearchTimeDialog(
                requireContext(),
                viewModel.years,
                onTimeSelected = {
                    binding.tvRange.text = "(${it ?: getString(R.string.all_period)})"
                    viewModel.searchYear = it?.toString()
                }
            ).show()
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeData() {
        viewModel.searchedNotes.observe(viewLifecycleOwner) {
            noteAdapter.setDateNotesList(
                it.toListDateNotes().sortedBy { dateNotes -> dateNotes.date })
            setAmountView(it)
        }
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.SEARCH)
    }

    private fun initNoteAdapter() {
        binding.rclDetailDate.layoutManager = LinearLayoutManager(requireContext())
        binding.rclDetailDate.adapter = noteAdapter
    }

    private fun setAmountView(data: List<Note>) {
        val expense = data.filter { it.category.categoryType == 1 }.sumOf { it.expense }
        val income = data.filter { it.category.categoryType == 2 }.sumOf { it.expense }
        val total = income - expense

        with(binding) {
            tvExpense.text = "-${formatNumberWithDots(expense)} $"
            tvIncome.text = "${formatNumberWithDots(income)} $"
            tvTotal.text = "${formatNumberWithDots(total)} $"
            tvTotal.setTextColor(
                if (total < 0) getColor(
                    requireContext(),
                    R.color.errorColor
                ) else getColor(requireContext(), R.color.primaryColor)
            )
        }
    }
}