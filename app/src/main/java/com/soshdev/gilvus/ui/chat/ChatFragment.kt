package com.soshdev.gilvus.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentChatBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class ChatFragment : BaseFragment() {

    private lateinit var binding: FragmentChatBinding
    private val vm: ChatViewModel by viewModel()
    private val adapter = ChatAdapter()
    private val args: ChatFragmentArgs by navArgs()

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
        setupToolbar(binding.toolbar, R.string.home)
        initRecyclerView()

        vm.messages.observe(viewLifecycleOwner, Observer { adapter.replace(it) })

        vm.getMessages(args.userId)
        prefsHelper.getToken()?.let { vm.fetchMessagesFromNetwork(it, args.userId) }
    }

    private fun initRecyclerView() = with(binding) {
        val llm = androidx.recyclerview.widget.LinearLayoutManager(context)
        recycler.layoutManager = llm
        recycler.adapter = adapter
    }
}