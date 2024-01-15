package com.example.myapplication

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.Locale


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

    private var recyclerView : RecyclerView? = null
    private var adapter : PdfAdapter? = null
    private var list : ArrayList<File>? = null
    private var progressBar : ProgressBar? = null
    private var searchView : SearchView? = null
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

        private fun setupsearch() {
            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query : String) : Boolean {
                    return false
                }
                override fun onQueryTextChange(newText : String) : Boolean {
                    filter(newText)
                    return false
                }
            })
        }

        private fun filter(newText : String) {
            val filterlist : ArrayList<File> = ArrayList()
            for (item in list!!) {
                if (item.name.lowercase(Locale.getDefault()).contains(newText)) {
                    filterlist.add(item)
                }
            }
            adapter!!.filterlist(filterlist)
        }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          recyclerView = view.findViewById(R.id.rv_files)
            progressBar = view.findViewById(R.id.progressBar)
            searchView = view.findViewById(R.id.searchView)
            progressBar!!.visibility = View.VISIBLE
            recyclerView?.layoutManager = LinearLayoutManager(requireContext())
            recyclerView!!.setHasFixedSize(true)
            recyclerView?.adapter=PdfAdapter(requireContext(), list)
            setupsearch()
            progressBar()
            getallFiles()
        }

        private fun progressBar() {
            progressBar!!.visibility = View.GONE
            if (adapter!!.itemCount == 0) {
                Toast.makeText(requireContext(), "No Pdf File In Phone", Toast.LENGTH_SHORT).show()
            } else {
                recyclerView!!.visibility = View.VISIBLE
            }
        }

        private fun getallFiles() : List<File> {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
            val selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            val selectionArgs = arrayOf("application/pdf")
            val cursor = requireContext().contentResolver.query(uri, projection, selection, selectionArgs, null)
            val list = ArrayList<File>()
            val pdfPathIndex = cursor!!.getColumnIndex(MediaStore.Files.FileColumns.DATA)
            while (cursor.moveToNext()) {
                if (pdfPathIndex != -1) {
                    val pdfPath = cursor.getString(pdfPathIndex)
                    val pdfFile = File(pdfPath)
                    if (pdfFile.exists() && pdfFile.isFile) {
                        list.add(pdfFile)
                    }
                }
            }
            cursor.close()
            return list
        }
    }

