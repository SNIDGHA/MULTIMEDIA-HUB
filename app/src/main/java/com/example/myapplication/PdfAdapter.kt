package com.example.myapplication


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class PdfAdapter(private var activity : Activity, private var list :  ArrayList<File>) :
    RecyclerView.Adapter<PdfAdapter.ViewHolder>() {

    fun filterlist(filterlist : ArrayList<File>) {
        list=filterlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val view : View =
            LayoutInflater.from(parent.context).inflate(R.layout.pdf_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        val file = list[position]
        holder.name.text = file.name
        holder.path.text = file.path
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, PdfActivity::class.java)
            intent.putExtra("name", file.name)
            intent.putExtra("path", file.path)
            activity.startActivity(intent)
        }
        holder.share.setOnClickListener {
            val intent = ShareCompat.IntentBuilder.from(activity).setType("application/pdf")
                .setStream(Uri.parse(file.path)).setChooserTitle("Choose app")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivity(intent)
        }

    }

    override fun getItemCount() : Int {
        return list.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView
        var path : TextView
        var share : ImageView


        init {
            name = itemView.findViewById(R.id.file_name)
            path = itemView.findViewById(R.id.file_path)
            share = itemView.findViewById(R.id.share)
        }
    }
}