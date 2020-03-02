package com.soshdev.gilvus.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentProfileBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import com.soshdev.gilvus.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.net.InetSocketAddress
import java.net.Socket

class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val vm: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar, R.string.home)

        binding.btnApply.setOnClickListener {
            // todo server testing
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val socket = Socket()
                    socket.connect(InetSocketAddress(Constants.emulatorLocalHost, Constants.port))
                } catch (e: Exception) {}
            }
        }
    }
}