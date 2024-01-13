package com.example.myapplication

import android.Manifest
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PdfFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PdfFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1 : String? = null
    private var param2 : String? = null

    private var adapter : PdfAdapter? = null
    private var pdfList : MutableList<File>? = null
    private var recyclerView : RecyclerView? = null
    private var noPdfTextView : TextView? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?,
    ) : View? {
        return inflater.inflate(R.layout.fragment_pdf, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PdfFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1 : String, param2 : String) =
            PdfFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun runtimePermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse : PermissionGrantedResponse) {}
                override fun onPermissionDenied(permissionDeniedResponse : PermissionDeniedResponse) {}
                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest : PermissionRequest,
                    permissionToken : PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).check()
    }

    private fun findPdf(file : File) : ArrayList<File> {
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
        recyclerView = view?.findViewById(R.id.recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        pdfList = ArrayList()
        (pdfList as ArrayList<File>).addAll(findPdf(Environment.getExternalStorageDirectory()))
        adapter = PdfAdapter(requireContext(), pdfList as ArrayList<File>)
        recyclerView!!.adapter = adapter

    }
}

