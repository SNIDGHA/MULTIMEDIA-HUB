package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class PdfAdapter(
    private val context: Context,
    private val pdfFiles: List<File>,
) :
    RecyclerView.Adapter<PdfAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(context).inflate(R.layout.pdf_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val file:File=pdfFiles[position]
        holder.txtName.text = pdfFiles[position].name
        holder.txtName.isSelected = true
        holder.itemView.setOnClickListener {
            val intent= Intent(context,PdfActivity::class.java)
            intent.putExtra("Name",file.name)
            intent.putExtra("Uri",file.toURI())
            context.startActivity(intent) }
    }

    override fun getItemCount(): Int {
        return pdfFiles.size
    }
    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        var txtName: TextView
        @JvmField
        var cardView: CardView

        init {
            txtName = itemView.findViewById(R.id.pdf_textView)
            cardView = itemView.findViewById(R.id.pdf_cardView)
        }
    }

}
