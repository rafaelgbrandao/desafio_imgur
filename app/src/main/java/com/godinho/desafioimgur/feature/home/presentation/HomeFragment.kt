package com.godinho.desafioimgur.feature.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.godinho.desafioimgur.R
import com.godinho.desafioimgur.extensions.observe
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
        initObservers()
        viewModel.onRequestCatList()
    }

    private fun initObservers() {
        viewModel.fetchListWithData().observe(this) {
            Toast.makeText(context, "Teste", Toast.LENGTH_LONG).show()
        }

        viewModel.showErrorDialog().observe(this) {
            showDialog(message = R.string.dialog_request_error_message)
        }

        viewModel.showNoContentDialog().observe(this) {
            showDialog(message = R.string.dialog_no_content_message)
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