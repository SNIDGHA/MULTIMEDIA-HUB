package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.github.barteksc.pdfviewer.PDFView
import java.io.File


class PdfActivity : AppCompatActivity() {
    private var pdfView : PDFView? = null
    private var back : ImageView? = null
    private var share : ImageView? = null
    private var title : TextView? = null
    private var name : String? = null
    private  var path : String? = null
    private var ishide = false
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        initvar()
    }

    private fun initvar() {
        back = findViewById(R.id.back)
        share = findViewById(R.id.share)
        title = findViewById(R.id.file_name)
        pdfView = findViewById(R.id.pdfView)
        back()
        share()
        getIntentData()
        showPdf()
        fullScreen()
    }

    private fun fullScreen() {
        pdfView!!.setOnClickListener {
            if (ishide) {
                findViewById<View>(R.id.materialToolbar).visibility =
                    View.VISIBLE
                ishide = false
            } else {
                findViewById<View>(R.id.materialToolbar).visibility =
                    View.GONE
                ishide = true
            }
        }
    }

    private fun showPdf() {
        pdfView!!.fromFile(File(path!!)).load()
    }

    private fun getIntentData() {
        name = intent.getStringExtra("name")
        path = intent.getStringExtra("path")
        title!!.text = name
    }

    private fun back() {
        back!!.setOnClickListener {  }
    }

    private fun share() {
        share!!.setOnClickListener {
            val intent = ShareCompat.IntentBuilder(this@PdfActivity).setType("application/pdf")
                .setStream(Uri.parse(path))
                .setChooserTitle("Choose app")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
    }
}
