package com.soshdev.gilvus.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soshdev.gilvus.databinding.FragmentChatBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import timber.log.Timber

class ChatFragment : BaseFragment() {

    private lateinit var binding: FragmentChatBinding
//    private val vm: ChatListViewModel by viewModel()
//    private val adapter = ChatListAdapter {
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("cre")
    }
}