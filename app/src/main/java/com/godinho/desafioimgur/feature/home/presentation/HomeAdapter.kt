package com.godinho.desafioimgur.feature.home.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.godinho.desafioimgur.R

class HomeAdapter(private val contentList: List<String>): RecyclerView.Adapter<HomeAdapter.CustomHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        context = parent.context
        return CustomHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        val link = contentList[position]
        Glide.with(context)
            .load(link)
            .centerCrop()
            .placeholder(R.drawable.ic_photo)
            .into(holder.image)
    }

    class CustomHolder(view: View): RecyclerView.ViewHolder(view) {

        val image: ImageView = view.findViewById(R.id.imgHomeAdapter)

    }
}