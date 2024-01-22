package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Parcelable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.Serializable


@UnstableApi class VideoAdapter(val context : Context, private val activity : FragmentActivity, videosList : ArrayList<Video> )
    :RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {
    private var videosList = ArrayList<Video>()
    init {
        this.videosList=videosList

    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_video, parent, false)
        return MyViewHolder(itemView)
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
        val item = videosList[position]
        val list: ArrayList<Video> = ArrayList()
        holder.tv_title.text = item.title
        holder.tv_duration.text = item.duration
        Glide.with(context).load(item.data).into(holder.imgView_thumbnail)
        holder.itemView.setOnClickListener { _ ->
           val intent = Intent(activity, VideoPlayerActivity::class.java)
               intent.putExtra("videoId", item.id)
               intent.putExtra("position", position)
               intent.putExtra("LIST", list as Serializable)
               activity.startActivity(intent)
        }
    }


    override fun getItemCount() : Int {

        return videosList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var imgView_thumbnail : ImageView
        var tv_title : TextView
        var tv_duration : TextView

        init {
            tv_title = itemView.findViewById(R.id.tv_title)
            tv_duration = itemView.findViewById(R.id.tv_duration)
            imgView_thumbnail = itemView.findViewById(R.id.imageView_thumbnail)
        }
    }

}