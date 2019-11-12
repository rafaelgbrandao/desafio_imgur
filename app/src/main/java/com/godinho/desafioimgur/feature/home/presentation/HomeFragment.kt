package com.godinho.desafioimgur.feature.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.godinho.desafioimgur.R
import com.godinho.desafioimgur.extensions.observe
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject

class HomeFragment: Fragment() {

    private val viewModel by inject<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureHomeRv()
        initObservers()
        viewModel.onRequestContentList()
    }

    private fun configureHomeRv() {
        homeRVContent.setHasFixedSize(true)
        homeRVContent.layoutManager = GridLayoutManager(context, 3)
    }

    private fun initObservers() {
        viewModel.fetchListWithData().observe(this) {
            homeRVContent.adapter = HomeAdapter(it)
        }

        viewModel.showErrorDialog().observe(this) {
            showDialog(message = R.string.dialog_request_error_message)
        }

        viewModel.showNoContentDialog().observe(this) {
            showDialog(message = R.string.dialog_no_content_message)
        }

        viewModel.showLoadingBar().observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(shouldShowLoading: Boolean) {
        when {
            shouldShowLoading -> {
                homeCardView.visibility = View.VISIBLE
                homeRVContent.visibility = View.GONE
            }
            else -> {
                homeCardView.visibility = View.GONE
                homeRVContent.visibility = View.VISIBLE
            }
        }
    }

    private fun showDialog(
        @StringRes title: Int = R.string.dialog_default_title,
        @StringRes message: Int) {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle(getString(title))
                setMessage(getString(message))
                setPositiveButton(getString(R.string.dialog_btn_close)) { _, _ -> }
            }.show()
        }
    }
}