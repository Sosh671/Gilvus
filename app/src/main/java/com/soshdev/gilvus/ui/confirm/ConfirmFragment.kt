package com.soshdev.gilvus.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
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
    private val isRegistration by lazy { args.isRegistration }
    private val phoneNumber by lazy { args.phoneNumber }
    private val smsCode by lazy { args.code }

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
        binding.btnConfirm.setOnClickListener {
            findNavController().navigate(ConfirmFragmentDirections.homeDestination())
            if (isRegistration)
                vm.confirmRegistration(phoneNumber, smsCode)
            else
                vm.confirmLogin(phoneNumber, smsCode)
        }
        watchDigits()

        testFillCode()
    }

    private val areInputsFilled = intArrayOf(0, 0, 0, 0)

    // listen to digits fields
    private fun watchDigits() {
        binding.run {
            digit1.addTextChangedListener { e ->
                e?.let {
                    if (it.isNotBlank()) areInputsFilled[0] = 1 else areInputsFilled[0] = 0
                    enableButton()
                }
            }
            digit2.addTextChangedListener { e ->
                e?.let {
                    if (it.isNotBlank()) areInputsFilled[1] = 1 else areInputsFilled[1] = 0
                    enableButton()
                }
            }
            digit3.addTextChangedListener { e ->
                e?.let {
                    if (it.isNotBlank()) areInputsFilled[2] = 1 else areInputsFilled[2] = 0
                    enableButton()
                }
            }
            digit4.addTextChangedListener { e ->
                e?.let {
                    if (it.isNotBlank()) areInputsFilled[3] = 1 else areInputsFilled[3] = 0
                    enableButton()
                }
            }
        }
    }

    private fun enableButton() {
        binding.btnConfirm.isEnabled = areInputsFilled.all { it > 0 }
    }

    fun testFillCode() {
        binding.run {
            digit1.setText("${smsCode % 10000 / 1000}")
            digit2.setText("${smsCode % 1000 / 100}")
            digit3.setText("${smsCode % 100 / 10}")
            digit4.setText("${smsCode % 10}")
        }
    }
}