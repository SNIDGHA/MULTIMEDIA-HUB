package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImageFullActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_full_activity)

        val imagePath=intent.getStringExtra("path")
        val imageName=intent.getStringExtra("name")

        supportActionBar?.title = imageName
       Glide.with(this)
            .load(imagePath)
            .into(findViewById(R.id.full_image))
    }

}