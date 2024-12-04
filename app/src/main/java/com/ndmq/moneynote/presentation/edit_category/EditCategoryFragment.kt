package com.ndmq.moneynote.presentation.edit_category

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.source.in_memory.defaultColorResources
import com.ndmq.moneynote.data.source.in_memory.defaultIconResources
import com.ndmq.moneynote.databinding.FragmentEditCategoryBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.presentation.edit_category.adapter.EditCategoryColorAdapter
import com.ndmq.moneynote.presentation.edit_category.adapter.EditCategoryIconAdapter
import com.ndmq.moneynote.utils.constant.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCategoryFragment : Fragment() {

    private val binding by lazy { FragmentEditCategoryBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<EditCategoryViewModel>()

    private val editCategoryColorAdapter = EditCategoryColorAdapter()
    private val editCategoryIconAdapter = EditCategoryIconAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

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

    private fun initData() {
        viewModel.selectedColor.value = requireContext().getColor(defaultColorResources[0])

        arguments?.getInt("CATEGORY_TYPE")?.let {
            viewModel.categoryType = it
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("CATEGORY", Category::class.java)
        } else {
            arguments?.getParcelable("CATEGORY") as Category?
        }?.let { category ->
            viewModel.id = category.id
            viewModel.categoryType = category.categoryType
            viewModel.selectedIcon.value = category.iconResource
            viewModel.selectedColor.value = category.tintColor
            binding.edtName.setText(category.categoryName)
        }

        arguments?.clear()
    }

    private fun initView() {
        initBottomNavBarIcon()
        initEditMode()
        initAdapter()
    }

    private fun handleEvent() {
        editCategoryColorAdapter.onColorClick = { color ->
            viewModel.selectedColor.value = color
        }

        editCategoryIconAdapter.onItemClick = { iconResource ->
            viewModel.selectedIcon.value = iconResource
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvSubmit.setOnClickListener {
            val title = binding.edtName.text
            if (title.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.title_mustn_t_be_blank),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            viewModel.saveCategory(title.toString()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.save_successfully),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigateUp()
            }
        }

        binding.ivDelete.setOnClickListener {
            viewModel.deleteCurrentCategory {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.delete_successfully),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun observeData() {
        viewModel.selectedColor.observe(viewLifecycleOwner) {
            editCategoryColorAdapter.setSelectedColor(it)
            editCategoryIconAdapter.setSelectedColor(it)
        }

        viewModel.selectedIcon.observe(viewLifecycleOwner) {
            editCategoryIconAdapter.setSelectedIcon(it)
        }
    }

    private fun initEditMode() {
        if (viewModel.id != null) {
            binding.tvTitle.text = binding.edtName.text
            binding.ivDelete.visibility = View.VISIBLE
        }
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.EDIT_CATEGORY)
    }

    private fun initAdapter() {
        binding.rclColor.adapter = editCategoryColorAdapter
        editCategoryColorAdapter.setDataList(
            defaultColorResources.map { requireContext().getColor(it) }
        )

        binding.rclIcon.adapter = editCategoryIconAdapter
        editCategoryIconAdapter.setDataList(defaultIconResources)
    }
}