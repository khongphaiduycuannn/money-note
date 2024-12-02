package com.ndmq.moneynote.presentation.edit_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ndmq.moneynote.databinding.FragmentEditNoteBinding
import com.ndmq.moneynote.presentation.MainActivity
import com.ndmq.moneynote.utils.constant.Screen

class EditNoteFragment : Fragment() {

    private val binding by lazy { FragmentEditNoteBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        initBottomNavBarIcon()
    }

    private fun initBottomNavBarIcon() {
        (activity as MainActivity).setCurrentScreen(Screen.EDIT_NOTE)
    }
}