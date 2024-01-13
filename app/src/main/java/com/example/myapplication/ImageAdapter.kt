package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class ImageAdapter(
    private val context : Context,
    private val list : ArrayList<ImageModel>,
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(
            R.layout.image_view, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        Glide.with(context).load(
            list[position].imagePath
        ).into(holder.imageView)
        holder.imageView.setOnClickListener {
            val parseData : String = list[position].imagePath.toString()
            context.startActivity(
                Intent(context, ImageFullActivity::class.java)
                    .putExtra("parseData", parseData)
            )
        }
    }

    override fun getItemCount() : Int {
        return list.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var imageView : ImageView
        init {
            imageView = itemView.findViewById(R.id.imageView)
        }
    }
}