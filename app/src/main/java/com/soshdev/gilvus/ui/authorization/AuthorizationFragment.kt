package com.soshdev.gilvus.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentAutorizationBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import com.soshdev.gilvus.util.showKeyboard
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class AuthorizationFragment : BaseFragment() {

    private lateinit var binding: FragmentAutorizationBinding
    private val vm: AuthorizationViewModel by viewModel()

    private var state = AuthorizationState.LOGIN

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAutorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.testCode.observe(viewLifecycleOwner, Observer {
            Timber.d("received code: $it")
            findNavController().navigate(
                AuthorizationFragmentDirections.confirmDestination(
                    it,
                    "12"
                )
            )
        })

        binding.run {
            activity?.let { loginField.showKeyboard(it) }

            loginField.addTextChangedListener { e ->
                e?.let { binding.btnConfirm.isEnabled = it.isNotBlank() }
            }
            btnConfirm.setOnClickListener {
                vm.login(loginField.text.toString())
            }
            txSwitch.setOnClickListener {
                if (state == AuthorizationState.LOGIN) {
                    state = AuthorizationState.REGISTRATION
                    txTitle.text = getString(R.string.registration)
                    txSwitch.text = getString(R.string.already_have_account)
                } else {
                    state = AuthorizationState.LOGIN
                    txTitle.text = getString(R.string.login)
                    txSwitch.text = getString(R.string.dont_have_account)
                }
            }
        }

        testPutPhone()
    }

    private fun testPutPhone() {
        binding.loginField.setText("12")
    }

    private enum class AuthorizationState {
        LOGIN,
        REGISTRATION
    }
}