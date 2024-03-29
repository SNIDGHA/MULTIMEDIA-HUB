package com.example.myapplication


import android.annotation.SuppressLint
import android.content.ContentUris

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@UnstableApi
/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1 : String? = null
    private var param2 : String? = null
    private val videosList = ArrayList<Video>()
    private var adapterVideoList : VideoAdapter? = null
    private var recyclerView : RecyclerView? = null
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?,
    ) : View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_video, container, false)

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1 : String, param2 : String) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initializeViews() {
        recyclerView = view?.findViewById(R.id.recyclerView_videos)
        recyclerView?.layoutManager = GridLayoutManager(requireContext(), 3) //3 = column count
        adapterVideoList = VideoAdapter(requireContext(),requireActivity(), videosList)
        recyclerView?.adapter = adapterVideoList
        loadVideos()



        /*if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                requireContext(),
                "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTINGS",
                Toast.LENGTH_SHORT
            ).show()
        } else{
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123
            )
            loadVideos()
        }


*/

    }
    private fun loadVideos() = object : Thread() {
        @SuppressLint("Recycle")
        override fun run() {
            super.run()
            Log.i("runCheck","load run")
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
            )
            val sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC"
            val cursor : Cursor? = requireContext().contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )
            if (cursor != null) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val titleColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val duration = cursor.getInt(durationColumn)
                    val data = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    var duration_formatted : String
                    val sec = duration / 1000 % 60
                    val min = duration / (1000 * 60) % 60
                    val hrs = duration / (1000 * 60 * 60)
                    duration_formatted = if (hrs == 0) {
                        min.toString() + ":" + java.lang.String.format(Locale.UK, "%02d", sec)
                    } else {
                        "$hrs:" + java.lang.String.format(
                            Locale.UK,
                            "%02d",
                            min
                        ) + ":" + java.lang.String.format(Locale.UK, "%02d", sec)
                    }
                    videosList.add(Video(id,data.toString(), title , duration_formatted))

                    requireActivity().runOnUiThread {
                        /*adapterVideoList!!.notifyItemInserted(
                            videosList.size - 1
                        )*/
                        adapterVideoList?.notifyDataSetChanged()
                    }

                }
            }
        }
    }.start()

}

