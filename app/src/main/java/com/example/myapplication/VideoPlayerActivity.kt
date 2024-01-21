@file:Suppress("DEPRECATION")
package com.example.myapplication


import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView



@UnstableApi class VideoPlayerActivity: AppCompatActivity() {
    private var videoId : Long = 0
    private lateinit var playerView : PlayerView
    private lateinit var player : ExoPlayer

    private var playWhenReady = true
    private var mediaItemIndex :Int?=null
    private var playbackPosition = 0L
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)
        initializeViews()
        videoId = intent.extras!!.getLong("videoId")
    }

    private fun initializeViews() {
        playerView = findViewById(R.id.playerView)
    }

    private fun  initializePlayer() {
        Log.e("check video","running")
        player =ExoPlayer.Builder(this).build()
        playerView.player
        val videoUri =
            ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId)
        val mediaSource = buildMediaSource(videoUri)
        player.prepare(mediaSource)
        player.play()
    }

    private fun buildMediaSource(uri : Uri) : MediaSource {
        Log.i("check working","working")
        val dataSourceFactory =
            DefaultDataSourceFactory(this, getString(R.string.app_name))
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri))

    }

    private fun releasePlayer() {
        player.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            mediaItemIndex = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }

    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
        super.onPause()
    }

    override fun onStop() {
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
        super.onStop()
    }
}