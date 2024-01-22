package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


@UnstableApi class VideoPlayerActivity: AppCompatActivity() {
    private var videoId : Long = 0
    private var playerView : PlayerView? = null
    private var player : ExoPlayer? = null
    private var x:Int=0
    private var playWhenReady = true
    private lateinit var list:ArrayList<Video>
    private var mediaItemIndex = 0
    private var playbackPosition = 0L
    private lateinit var mediaItem:MutableList<MediaItem>
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)
        initializeViews()
        videoId = intent.getLongExtra("videoId", 0L)
        x = intent.getIntExtra("position", 0)
         list = (intent.getSerializableExtra("LIST") as? ArrayList<Video>)!!
        mediaItem =getMediaItems()

            }
    private fun initializeViews() {
        playerView = findViewById(R.id.playerView)

    }

    private fun  initializePlayer() {
        Log.i("run check", "running")
        val exoPlayer = ExoPlayer.Builder(this).build()
        if(!exoPlayer.isPlaying)
        {
            exoPlayer.setMediaItems(mediaItem, x,0)
        }
        else
        {
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
            exoPlayer.setMediaItems(mediaItem,x,0)
        }
        exoPlayer.prepare()
        exoPlayer.play()

            }

   private fun releasePlayer() {
        Log.i("run check", "working")
        player?.let { exoPlayer ->
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
    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
    private fun getMediaItems(): MutableList<MediaItem> {
        val mediaItems=ArrayList<MediaItem>()
        for (video in list) {
            mediaItems.add(
                MediaItem.Builder()
                    .setUri(video.data)
                    .setMediaMetadata(getMetaData(video))
                    .build()
            )
        }
        return mediaItems
    }
    private fun getMetaData(video:Video): androidx.media3.common.MediaMetadata {
        return androidx.media3.common.MediaMetadata.Builder()
            .setTitle(video.title)
            .build()
    }
}