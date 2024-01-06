package com.example.myapplication

import android.Manifest
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File


abstract class PdfFragment: AppCompatActivity(), OnPdfSelectListener {
    private var adapter: PdfAdapter? = null
    private var pdfList: MutableList<File>? = null
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_pdf)
    }

    private fun runtimePermission() {
        Dexter.withContext(this@PdfFragment)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {}
                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}
                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).check()
    }

    private fun findPdf(file: File): ArrayList<File> {
        val arrayList = ArrayList<File>()
        val files = file.listFiles()
        for (singleFile in files!!) {
            if (singleFile.isDirectory && singleFile.isHidden) {
                arrayList.addAll(findPdf(singleFile))
            } else {
                if (singleFile.name.endsWith(".pdf")) {
                    arrayList.add(singleFile)
                }
            }
        }
        return arrayList
    }

    fun displayPdf() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        pdfList = ArrayList()
        (pdfList as ArrayList<File>).addAll(findPdf(Environment.getExternalStorageDirectory()))
        adapter = PdfAdapter(this, pdfList as ArrayList<File>)
        recyclerView!!.adapter = adapter
    }

}


