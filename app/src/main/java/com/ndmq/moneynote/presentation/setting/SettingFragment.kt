package com.ndmq.moneynote.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ndmq.moneynote.R
import com.ndmq.moneynote.databinding.FragmentSettingBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.utils.constant.Screen
import com.ndmq.moneynote.utils.navigateTo

class SettingFragment : Fragment() {

    private val binding by lazy { FragmentSettingBinding.inflate(layoutInflater) }

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
    }

    private fun initView() {
        initBottomNavBarIcon()
    }

    private fun handleEvent() {
        binding.btnFixedCost.setOnClickListener {
            navigateTo(Screen.FIXED_COST, R.id.action_settingFragment_to_fixedCostsListFragment)
        }
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.SETTING)
    }
}