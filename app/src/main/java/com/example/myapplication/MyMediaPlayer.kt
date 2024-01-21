package com.example.myapplication

import androidx.media3.exoplayer.ExoPlayer


object Exoplayer{
    private var instance:ExoPlayer? = null
    fun getInstance(): ExoPlayer? {
        if (instance == null) {
            //instance = ExoPlayer()
        }
        return instance
    }

    var currentIndex = -1
}
