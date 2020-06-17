package com.soshdev.gilvus.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.soshdev.gilvus.R
import com.soshdev.gilvus.data.AuthorizationState
import com.soshdev.gilvus.databinding.FragmentAutorizationBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import com.soshdev.gilvus.util.Constants
import com.soshdev.gilvus.util.begone
import com.soshdev.gilvus.util.showKeyboard
import com.soshdev.gilvus.util.showSnackbar
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class AuthorizationFragment : BaseFragment() {

    private lateinit var binding: FragmentAutorizationBinding
    private val vm: AuthorizationViewModel by viewModel()

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

        checkToken()
        vm.state = AuthorizationState.LOGIN
        vm.testCode.observe(viewLifecycleOwner, Observer {
            // todo temp solution
            if (it > 0) {
                findNavController().navigate(
                    AuthorizationFragmentDirections.confirmDestination(
                        it,
                        vm.phone,
                        vm.state == AuthorizationState.REGISTRATION
                    )
                )
                vm.testClear()
            }
        })
//        vm.clearErrors()
//        vm.errors.observe(viewLifecycleOwner, Observer {
//            binding.root.showSnackbar(it)
//        })
        initUI()
        testPutPhone()

        // todo for testing
//        binding.testSwitch.isChecked = sharedViewModel.host == Constants.emulatorLocalHost
//        binding.testSwitch.setOnCheckedChangeListener { _, _ -> sharedViewModel.toggleHostAndReconnect() }
        sharedViewModel.reconnect()
    }

    private fun initUI() {
        binding.run {
            // todo mb delete not sure it works
            activity?.let { loginField.showKeyboard(it) }

            // enable or disable confirm btn
            loginField.addTextChangedListener { e ->
                e?.let { binding.btnConfirm.isEnabled = it.isNotBlank() }
            }
            btnConfirm.setOnClickListener {
                vm.phone = loginField.text.toString()
                val password = if (passwordField.text?.isNotBlank() == true)
                    passwordField.text?.toString()
                else
                    null
                if (vm.state == AuthorizationState.LOGIN)
                    vm.login(vm.phone, password)
                else
                    vm.registration(vm.phone, password)
            }
            // change layout to registration/login
            txSwitch.setOnClickListener {
                if (vm.state == AuthorizationState.LOGIN) {
                    vm.state = AuthorizationState.REGISTRATION
                    txTitle.text = getString(R.string.registration)
                    txSwitch.text = getString(R.string.already_have_account)
                } else {
                    vm.state = AuthorizationState.LOGIN
                    txTitle.text = getString(R.string.login)
                    txSwitch.text = getString(R.string.dont_have_account)
                }
            }
        }
    }

    private fun checkToken() {
        prefsHelper.getToken()?.let {
            findNavController().navigate(AuthorizationFragmentDirections.homeDestination())
        }
    }

    private fun testPutPhone() {
        binding.loginField.setText("2020")
    }
}