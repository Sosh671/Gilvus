package com.soshdev.gilvus.ui.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentRoomBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class RoomFragment : BaseFragment() {

    private lateinit var binding: FragmentRoomBinding
    private val vm: RoomViewModel by viewModel()
    private val adapter = RoomtAdapter()
    private val args: RoomFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(binding.toolbar, R.string.home)
        initRecyclerView()

        vm.messages.observe(viewLifecycleOwner, Observer { adapter.replace(it) })

        prefsHelper.getToken()?.let { vm.getMessages(it, args.roomId) }

        binding.btnSendMessage.setOnClickListener {
            val text = binding.edMessage.text?.toString() ?: return@setOnClickListener
            if (text.isNotBlank())
                prefsHelper.getToken()?.let { token -> vm.sendMessage(token, args.roomId, text) }
        }
    }

    private fun initRecyclerView() = with(binding) {
        val llm = androidx.recyclerview.widget.LinearLayoutManager(context)
        recycler.layoutManager = llm
        recycler.adapter = adapter
    }
}