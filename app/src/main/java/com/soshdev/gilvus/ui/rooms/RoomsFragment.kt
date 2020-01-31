package com.soshdev.gilvus.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentChatListBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel


class RoomsFragment : BaseFragment() {

    private lateinit var binding: FragmentChatListBinding
    private val vm: RoomsViewModel by viewModel()
    private val adapter =
        RoomsAdapter { findNavController().navigate(RoomsFragmentDirections.chatDestination(it)) }

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

        binding.fab.setOnClickListener {
            findNavController().navigate(RoomsFragmentDirections.newRoomDestination())
        }

        vm.rooms.observe(viewLifecycleOwner, Observer {
            adapter.clear()
            adapter.add(it)
        })

        vm.getRooms()
    }

    private fun initRecyclerView() = with(binding) {
        val llm = androidx.recyclerview.widget.LinearLayoutManager(context)
        recycler.layoutManager = llm
        recycler.adapter = adapter
    }
}