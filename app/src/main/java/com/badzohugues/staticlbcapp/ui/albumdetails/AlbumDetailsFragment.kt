package com.badzohugues.staticlbcapp.ui.albumdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.badzohugues.staticlbcapp.R
import com.badzohugues.staticlbcapp.common.BaseFragment
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.databinding.FragmentAlbumDetailsBinding
import com.badzohugues.staticlbcapp.misc.Status
import com.badzohugues.staticlbcapp.misc.itemdecoration.SpacingDecoration
import com.badzohugues.staticlbcapp.ui.home.HomeViewModel

private const val COLUMN = 3

class AlbumDetailsFragment : BaseFragment<FragmentAlbumDetailsBinding, List<AlbumItem>>() {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var albumDetailsAdapter: AlbumDetailsAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentAlbumDetailsBinding {
        return FragmentAlbumDetailsBinding.inflate(inflater, container, attachToParent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val albumId = navArgs<AlbumDetailsFragmentArgs>().value.albumId
        homeViewModel.getItemsOfAlbum(albumId)
    }

    override fun initViews(context: Context) {
        val linearLayoutManager = GridLayoutManager(context, COLUMN)
        val decoration = SpacingDecoration(
            spacing = context.resources.getDimensionPixelSize(R.dimen.small_margin),
            true
        )

        albumDetailsAdapter = AlbumDetailsAdapter()

        with(binding.albumItemRecycler) {
            layoutManager = linearLayoutManager
            addItemDecoration(decoration)
            adapter = albumDetailsAdapter
        }
    }

    override fun prepareData() {
        homeViewModel.itemsOfAlbum().observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Status.SUCCESS -> showSuccess(it.data ?: emptyList())
                    Status.LOADING -> showLoading()
                    Status.ERROR -> activity?.let { context ->
                        showError(
                            it.message ?: context.resources.getString(R.string.error_unknown)
                        )
                    }
                }
            }
        )
    }

    override fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            noResultTxv.visibility = View.GONE
            albumItemRecycler.visibility = View.GONE
        }
    }

    override fun showSuccess(data: List<AlbumItem>) {
        with(binding) {
            progressBar.visibility = View.GONE
            noResultTxv.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
            albumItemRecycler.visibility = if (data.isEmpty()) View.GONE else View.VISIBLE
            albumDetailsAdapter.albumItems = data
        }
    }

    override fun showError(message: String) {
        with(binding) {
            progressBar.visibility = View.GONE
            albumItemRecycler.visibility = View.GONE
            noResultTxv.visibility = View.VISIBLE
            activity?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
        }
    }
}
