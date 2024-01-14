@file:Suppress("DEPRECATION")

package com.example.myapplication


import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


@Suppress("DEPRECATION")
class VideoPlayerActivity: AppCompatActivity() {
    private var videoId : Long = 0
    private var playerView : PlayerView? = null
    private var player : SimpleExoPlayer? = null
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)
        initializeViews()
        videoId = intent.extras!!.getLong("videoId")
    }

    private fun initializeViews() {
        playerView = findViewById(R.id.playerView)
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        playerView!!.player = player
        val videoUri =
            ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId)
        val mediaSource = buildMediaSource(videoUri)
        player!!.prepare(mediaSource)
        player!!.playWhenReady = true
    }

    private fun buildMediaSource(uri : Uri) : MediaSource {
        val dataSourceFactory : DataSource.Factory =
            DefaultDataSourceFactory(this, getString(R.string.app_name))
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri))
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
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
