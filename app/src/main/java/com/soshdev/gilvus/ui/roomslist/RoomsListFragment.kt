package com.soshdev.gilvus.ui.roomslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentRoomsListBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class RoomsListFragment : BaseFragment() {

    private lateinit var binding: FragmentRoomsListBinding
    private val vm: RoomsListViewModel by viewModel()
    private val adapter =
        RoomsListAdapter {
            findNavController().navigate(
                RoomsListFragmentDirections.chatDestination(it ?: return@RoomsListAdapter)
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar, R.string.home)
        initRecyclerView()

        binding.fab.setOnClickListener {
            findNavController().navigate(RoomsListFragmentDirections.newRoomDestination())
        }

        vm.rooms.observe(viewLifecycleOwner, Observer { adapter.replace(it) })

        userToken?.let { vm.getRooms(it) }
    }

    private fun initRecyclerView() = with(binding) {
        val llm = androidx.recyclerview.widget.LinearLayoutManager(context)
        recycler.layoutManager = llm
        recycler.adapter = adapter
    }
}