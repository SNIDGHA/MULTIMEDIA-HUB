package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageAdapter(private var context: Context, private var imagesList: ArrayList<ImageModel>):
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){

            class ImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
                var image:ImageView?=null

                init {
                    image=itemView.findViewById(R.id.imageView)
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
       val inflater =LayoutInflater.from(parent.context)
        val view =inflater.inflate(R.layout.image_view,parent,false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
   return imagesList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
     val currentImage=imagesList[position]
        Glide.with(context)
            .load(currentImage.imagePath)
            .apply(RequestOptions().centerCrop())
            .into(holder.image!!)

        val intent= Intent(context,ImageFullActivity::class.java)
        intent.putExtra("path",currentImage.imagePath)
        intent.putExtra("name",currentImage.imageName)
        context.startActivity(intent)

    }
}