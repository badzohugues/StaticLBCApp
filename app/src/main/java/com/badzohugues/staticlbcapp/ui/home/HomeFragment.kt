package com.badzohugues.staticlbcapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.badzohugues.staticlbcapp.R
import com.badzohugues.staticlbcapp.common.BaseFragment
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.databinding.FragmentHomeBinding
import com.badzohugues.staticlbcapp.misc.NetworkHelper
import com.badzohugues.staticlbcapp.misc.Status
import com.badzohugues.staticlbcapp.misc.itemdecoration.SpacingDecoration


class HomeFragment : BaseFragment<FragmentHomeBinding, List<AlbumItem>>() {
    private val homeViewModel : HomeViewModel by activityViewModels()
    private lateinit var homeAdapter: HomeAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, attachToParent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NetworkHelper.observe(viewLifecycleOwner, { isConnected ->
            homeViewModel.getAlbums(isConnected)
        })
    }

    override fun initViews(context: Context) {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val decoration = SpacingDecoration(spacing = context.resources.getDimensionPixelSize(R.dimen.small_margin))
        homeAdapter = HomeAdapter (itemAlbumClick = { item ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAlbumDetails(item.albumId,
                    context.getString(R.string.txv_text_album_title, item.albumId))
            )
        })

        with(binding.albumRecycler) {
            layoutManager = linearLayoutManager
            addItemDecoration(decoration)
            adapter = homeAdapter
        }
    }

    override fun prepareData() {
        homeViewModel.albums().observe(viewLifecycleOwner, { result ->
            when (result.status) {
                Status.SUCCESS -> showSuccess(result.data ?: emptyList())
                Status.LOADING -> showLoading()
                Status.ERROR -> activity?.let { context -> showError(result.message ?: context.resources.getString(R.string.error_unknown)) }
            }
        })
    }

    override fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            noResultTxv.visibility = View.GONE
        }
    }

    override fun showSuccess(data: List<AlbumItem>) {
        with(binding) {
            progressBar.visibility = View.GONE
            noResultTxv.visibility = if(data.isEmpty()) View.VISIBLE else View.GONE
            albumRecycler.visibility = if(data.isEmpty()) View.GONE else View.VISIBLE
            homeAdapter.albumItems = data
        }
    }

    override fun showError(message: String) {
        with(binding) {
            progressBar.visibility = View.GONE
            noResultTxv.visibility = View.VISIBLE
            activity?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
        }
    }
}