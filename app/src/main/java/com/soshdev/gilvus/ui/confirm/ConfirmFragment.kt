package com.soshdev.gilvus.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentConfirmBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class ConfirmFragment : BaseFragment() {

    private lateinit var binding: FragmentConfirmBinding
    private val vm: ConfirmViewModel by viewModel()
    private val args: ConfirmFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txCodeSend.text = getString(R.string.code_sent_to, args.phoneNumber)

        testFillCode()
    }

    fun testFillCode() {
        binding.run {
            val code = args.code
            digit1.setText("${code % 10000 / 1000}")
            digit2.setText("${code % 1000 / 100}")
            digit3.setText("${code % 100 / 10}")
            digit4.setText("${code % 10}")
            btnConfirm.isEnabled = true
            btnConfirm.setOnClickListener {
                findNavController().navigate(ConfirmFragmentDirections.homeDestination())
            }
        }
    }
}