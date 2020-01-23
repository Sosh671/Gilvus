package com.soshdev.gilvus.ui.base

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.soshdev.gilvus.MainActivity

open class BaseFragment : Fragment() {

    fun setupToolbar(toolbar: Toolbar, resId: Int) {
        (activity as? MainActivity)?.setupToolbar(toolbar, getString(resId))
    }
}