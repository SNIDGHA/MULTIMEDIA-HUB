package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AudioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AudioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1 : String? = null
    private var param2 : String? = null

    private lateinit var recyclerView : RecyclerView
    private var noMusicTextView : TextView? = null
    private var songsList = ArrayList<AudioModel>()

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio, container, false)
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
            AudioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("songChecker","viewCreated ran")
        recyclerView = view.findViewById(R.id.recycler_view)
        noMusicTextView = view.findViewById(R.id.no_songs_text)
        /*if (!checkPermission()) {
            requestPermission()
        }*/

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )
        val songUri : Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = requireContext().contentResolver.query(
            songUri,
            projection,
            null,
            null,
            MediaStore.Audio.Media.TITLE
        )
        if (cursor != null) while (cursor.moveToNext()) {
            val songData = AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2))
            if (File(songData.path).exists()) songsList.add(songData)
        }
        val idColumn = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        Log.i("songChecker","cursor fetched")
        while (cursor.moveToNext()) {
            val id : Long = cursor.getLong(idColumn)
            var name : String = cursor.getString(nameColumn)

            val duration : Int = cursor.getInt(durationColumn)


            val uri : Uri =
                ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
            val song = AudioModel(uri.toString(), name, duration.toString())
            songsList.add(song)
        }
        Log.i("songChecker","SongsPut")
        Toast.makeText(context,songsList.size.toString(),Toast.LENGTH_SHORT).show()
        if (songsList.size == 0) {
            noMusicTextView!!.visibility = View.VISIBLE
        } else {
            //recyclerview
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = MusicListAdapter(songsList, requireContext())
        }

    }

    /*private fun checkPermission() : Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                requireContext(),
                "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTINGS",
                Toast.LENGTH_SHORT
            ).show()
        } else ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            123
        )
    }*/

    override fun onResume() {
        super.onResume()
        if (recyclerView != null) {
            recyclerView!!.adapter = MusicListAdapter(songsList,requireContext())
        }
    }
}