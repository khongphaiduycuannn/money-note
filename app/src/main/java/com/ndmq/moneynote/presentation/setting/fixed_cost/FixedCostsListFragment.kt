package com.ndmq.moneynote.presentation.setting.fixed_cost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.source.in_memory.frequencies
import com.ndmq.moneynote.databinding.FragmentFixedCostsListBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.presentation.setting.fixed_cost.adapter.FixedCostAdapter
import com.ndmq.moneynote.utils.constant.AppConstant
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FixedCostsListFragment : Fragment() {

    private val binding by lazy { FragmentFixedCostsListBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<FixedCostsListViewModel>()

    private val fixedCostAdapter by lazy { FixedCostAdapter(frequencies) }

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
        viewModel.fetchData()
    }

    private fun initView() {
        initBottomNavBarIcon()

        binding.rclFixedCosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rclFixedCosts.adapter = fixedCostAdapter
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivAddFixedCost.setOnClickListener {
            navigateTo(
                Screen.FIXED_COSTS_LIST,
                R.id.action_fixedCostsListFragment_to_fixedCostFragment
            )
        }

        binding.tvAddFixedCost.setOnClickListener {
            navigateTo(
                Screen.FIXED_COSTS_LIST,
                R.id.action_fixedCostsListFragment_to_fixedCostFragment
            )
        }

        fixedCostAdapter.setOnItemClick {
            navigateTo(
                Screen.FIXED_COSTS_LIST,
                R.id.action_fixedCostsListFragment_to_fixedCostFragment,
                bundleOf(AppConstant.FIXED_COST to it)
            )
        }
    }

    private fun observeData() {
        viewModel.fixedCosts.observe(viewLifecycleOwner) {
            fixedCostAdapter.setDataList(it)
        }
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.FIXED_COSTS_LIST)
    }
}