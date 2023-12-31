package com.example.myapplication

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ImageFragment :AppCompatActivity(){

            private var recyclerView:RecyclerView?=null
    private var allPictures:ArrayList<ImageModel>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_images)

        recyclerView =findViewById(R.id.recyclerView)
        recyclerView?.layoutManager= LinearLayoutManager(this)
        recyclerView?.setHasFixedSize(true)

        //storage Permissions

        if(ContextCompat.checkSelfPermission(
            this@ImageFragment, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                this@ImageFragment,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }
        allPictures=ArrayList()
        if(allPictures!!.isEmpty()){
            allPictures=getAllImages()
            recyclerView?.adapter= allPictures?.let { ImageAdapter(this, it) }
        }

    }
    private fun getAllImages(): ArrayList<ImageModel> {
        val images=ArrayList<ImageModel>()
        val allImageUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection= arrayOf(MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media.DISPLAY_NAME)
        val cursor=this@ImageFragment.contentResolver.query(allImageUri,projection,null,null,null)

        try {
            cursor!!.moveToFirst()
            do {
                val image=ImageModel()
                image.imagePath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.imageName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            }while (cursor.moveToNext())
            cursor.close()

        }
        catch(e:Exception)
        {
            e.printStackTrace()
        }
        return images
    }

  }