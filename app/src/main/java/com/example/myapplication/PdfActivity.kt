package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class PdfActivity : AppCompatActivity() {
    private var filePath: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        val pdfView: PDFView = findViewById(R.id.pdfView)
        filePath = intent.getStringExtra("path")
        val file = File(filePath!!)
       val path: Uri? = Uri.fromFile(file)
        pdfView.fromUri(path).load()
    }

}
