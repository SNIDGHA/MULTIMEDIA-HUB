package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PdfFragment : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_pdf)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = PdfAdapter(this, pdffiles())

    }
    @SuppressLint("SuspiciousIndentation", "Recycle")
    private fun pdffiles(): List<String>{
          contentResolver
        val mime = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
        val mimitype = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        val args = arrayOf(mimitype)
        val proj = arrayOf(MediaStore.Files.FileColumns.DATA,MediaStore.Files.FileColumns.DISPLAY_NAME)
        val cursor = contentResolver.query(MediaStore.Files.getContentUri("external"),
            proj,mime,args,null)
        val pdfFiles= ArrayList<String>()
        if (cursor != null){
            while (cursor.moveToNext()){
                val index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val path = cursor.getString(index)
                pdfFiles.add(path)
            }

        }
        return pdfFiles
    }
}
