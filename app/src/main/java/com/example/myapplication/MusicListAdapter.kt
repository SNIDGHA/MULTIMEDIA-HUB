package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MusicPlayerActivity.MyMediaPlayer.getInstance



class MusicListAdapter(private var songsList: ArrayList<AudioModel>,val context : Context ) :
    RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songData = songsList[position]
        holder.titleTextView.text = songData.title
         if (MusicPlayerActivity.MyMediaPlayer.currentIndex == position) {
              holder.titleTextView.setTextColor(Color.parseColor("#FF0000"))
          } else {
              holder.titleTextView.setTextColor(Color.parseColor("#000000"))
          }
        holder.itemView.setOnClickListener { //navigate to another activity
            getInstance().reset()

            MusicPlayerActivity.MyMediaPlayer.currentIndex = position
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("LIST", songsList)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        private var iconImageView: ImageView

        init {
            titleTextView = itemView.findViewById(R.id.music_title_text)
            iconImageView = itemView.findViewById(R.id.icon_view)
        }
    }
}