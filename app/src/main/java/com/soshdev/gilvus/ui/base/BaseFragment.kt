package com.soshdev.gilvus.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.soshdev.gilvus.MainActivity
import com.soshdev.gilvus.R
import com.soshdev.gilvus.util.PrefsHelper
import com.soshdev.gilvus.util.showSnackbar
import org.koin.android.viewmodel.ext.android.sharedViewModel

open class BaseFragment : Fragment() {

    protected val prefsHelper: PrefsHelper by lazy { PrefsHelper(context!!.applicationContext) }
    protected val userToken by lazy { prefsHelper.getToken() }
    protected val sharedViewModel by sharedViewModel<SharedViewModel>()
    private val authorizationDestinations = intArrayOf(
        R.id.authorization,
        R.id.confirm
    )

    protected fun setupToolbar(toolbar: Toolbar, resId: Int) {
        (activity as? MainActivity)?.setupToolbar(toolbar, getString(resId))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (prefsHelper.getToken() == null)
            findNavController().currentDestination?.let {
                if (it.id !in authorizationDestinations)
                    findNavController().navigate(R.id.global_authorization_destination)
            }
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.clearErrors()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.serverConnectionLost.observe(viewLifecycleOwner, Observer {
            if (it)
                view.showSnackbar(R.string.server_connection_lost, actionText = R.string.retry,
                    actionListener = View.OnClickListener { sharedViewModel.reconnect() })
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideKeyboard()
    }

    @Suppress("unused")
    protected fun showKeyboard() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.toggleSoftInput(
            0,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun hideKeyboard() {
        view?.windowToken?.let {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
                it,
                0
            )
        }
    }
}