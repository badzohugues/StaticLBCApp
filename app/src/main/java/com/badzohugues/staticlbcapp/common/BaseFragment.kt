package com.badzohugues.staticlbcapp.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B: ViewBinding, T>: Fragment() {
    lateinit var binding: B

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean): B
    abstract fun initViews(context: Context)
    abstract fun prepareData()
    abstract fun showSuccess(data: T)
    abstract fun showLoading()
    abstract fun showError(message: String)

    fun getBaseActivity() = activity as BaseActivity<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getViewBinding(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { context -> initViews(context) }
        prepareData()
    }
}