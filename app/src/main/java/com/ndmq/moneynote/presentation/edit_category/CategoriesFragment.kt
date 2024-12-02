package com.ndmq.moneynote.presentation.edit_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndmq.moneynote.R
import com.ndmq.moneynote.databinding.FragmentCategoriesBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val binding by lazy { FragmentCategoriesBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<CategoriesViewModel>()

    private val categoryAdapter = CategoryAdapter()

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
    }

    private fun initView() {
        initBottomNavBarIcon()
        initCategoryAdapter()
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivAddCategory.setOnClickListener {
            navigateTo(
                Screen.EDIT_CATEGORY,
                R.id.action_categoriesFragment_to_editCategoryFragment,
                bundleOf("CATEGORY_TYPE" to viewModel.categoryType.value)
            )
        }

        categoryAdapter.setOnCategoryClick {
            navigateTo(
                Screen.EDIT_CATEGORY,
                R.id.action_categoriesFragment_to_editCategoryFragment,
                bundleOf("CATEGORY" to it)
            )
        }

        binding.btnExpense.setOnClickListener {
            viewModel.categoryType.value = 1
        }

        binding.btnIncome.setOnClickListener {
            viewModel.categoryType.value = 2
        }
    }

    private fun observeData() {
        observeCategory()
        observeCategoryType()
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.CATEGORIES)
    }

    private fun initCategoryAdapter() {
        binding.rclCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rclCategories.adapter = categoryAdapter
    }

    private fun observeCategory() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            if (categories.isNullOrEmpty()) return@observe

            val categoryType = viewModel.categoryType.value ?: 1
            val filteredCategories = categories.filter { it.categoryType == categoryType }
            categoryAdapter.setDataList(filteredCategories)
        }
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
            viewModel.categories.value = viewModel.categories.value
        }
    }
}