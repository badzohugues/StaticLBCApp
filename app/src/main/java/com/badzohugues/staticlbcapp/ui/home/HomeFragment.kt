package com.badzohugues.staticlbcapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.badzohugues.staticlbcapp.R
import com.badzohugues.staticlbcapp.common.BaseFragment
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.databinding.FragmentHomeBinding
import com.badzohugues.staticlbcapp.misc.ErrorMessage
import com.badzohugues.staticlbcapp.misc.NetworkHelper
import com.badzohugues.staticlbcapp.misc.Status
import com.badzohugues.staticlbcapp.misc.itemdecoration.SpacingDecoration
import com.badzohugues.staticlbcapp.ui.home.adapter.AlbumAdapter


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val homeViewModel : HomeViewModel by activityViewModels()
    private lateinit var homeAdapter: AlbumAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, attachToParent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { context ->
            initViews(context)
            prepareData()
            NetworkHelper.observe(viewLifecycleOwner, { isConnected ->
                homeViewModel.getAlbums(isConnected)
            })
        }
    }

    private fun initViews(context: Context) {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val decoration = SpacingDecoration(spacing = context.resources.getDimensionPixelSize(R.dimen.small_margin))
        homeAdapter = AlbumAdapter (itemAlbumClick = { item ->
            Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
        })

        with(binding.albumRecycler) {
            layoutManager = linearLayoutManager
            addItemDecoration(decoration)
            adapter = homeAdapter
        }
    }

    private fun prepareData() {
        homeViewModel.albumItems().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> showSuccess(it.data ?: emptyList())
                Status.LOADING -> showLoading()
                Status.ERROR -> activity?.let { context -> showError(it.message ?: context.resources.getString(ErrorMessage.UNKNOWN_ERROR.resId)) }
            }
        })
    }

    private fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            noResultTxv.visibility = View.GONE
            albumRecycler.visibility = View.GONE
        }
    }

    private fun showSuccess(items: List<AlbumItem>) {
        with(binding) {
            progressBar.visibility = View.GONE
            noResultTxv.visibility = if(items.isEmpty()) View.VISIBLE else View.GONE
            albumRecycler.visibility = if(items.isEmpty()) View.GONE else View.VISIBLE
            homeAdapter.albumItems = items
        }
    }

    private fun showError(message: String) {
        with(binding) {
            progressBar.visibility = View.GONE
            albumRecycler.visibility = View.GONE
            noResultTxv.visibility = View.VISIBLE
            activity?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
        }
    }
}