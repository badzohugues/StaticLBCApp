package com.badzohugues.staticlbcapp.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B: ViewBinding>: Fragment() {
    lateinit var binding: B

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean): B

    fun getBaseActivity() = activity as BaseActivity<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getViewBinding(inflater, container, false)

        return binding.root
    }
}