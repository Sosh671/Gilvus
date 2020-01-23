package com.soshdev.gilvus.ui.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentChatListBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class ChatListFragment : BaseFragment() {

    private lateinit var binding: FragmentChatListBinding
    private val vm: ChatListViewModel by viewModel()
    private val adapter = ChatListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar, R.string.home)
        initRecyclerView()
        
        vm.chats.observe(viewLifecycleOwner, Observer {
            adapter.add(it)
        })

        vm.getChats()
    }

    private fun initRecyclerView() = with(binding) {
        val llm = androidx.recyclerview.widget.LinearLayoutManager(context)
        recycler.layoutManager = llm
        recycler.adapter = adapter
    }
}