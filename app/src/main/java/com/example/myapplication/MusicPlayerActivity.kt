package com.example.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MusicPlayerActivity.MyMediaPlayer.getInstance
import java.io.IOException
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class MusicPlayerActivity : AppCompatActivity() {
    private var titleTv : TextView? = null
    var currentTimeTv : TextView? = null
    private var totalTimeTv : TextView? = null
    var seekBar : SeekBar? = null
    var pausePlay : ImageView? = null
    private var nextBtn : ImageView? = null
    private var previousBtn : ImageView? = null
    var musicIcon : ImageView? = null
    private var songsList : ArrayList<AudioModel>? = null
    private var currentSong : AudioModel? = null
    var mediaPlayer : MediaPlayer? = getInstance()
    var x = 0
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        titleTv = findViewById(R.id.song_title)
        currentTimeTv = findViewById(R.id.current_time)
        totalTimeTv = findViewById(R.id.total_time)
        seekBar = findViewById(R.id.seek_bar)
        pausePlay = findViewById(R.id.pause_play)
        nextBtn = findViewById(R.id.next)
        previousBtn = findViewById(R.id.previous)
        musicIcon = findViewById(R.id.music_icon_big)
        titleTv?.isSelected = true
        songsList = intent.getSerializableExtra("LIST") as ArrayList<AudioModel>
        setResourcesWithMusic()
        runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    seekBar?.progress = mediaPlayer!!.currentPosition
                    currentTimeTv?.text = convertToMMSS(mediaPlayer!!.currentPosition.toString() + "")
                    if (mediaPlayer!!.isPlaying) {
                        pausePlay?.setImageResource(R.drawable.baseline_pause_circle_outline_24)
                        musicIcon?.rotation = x++.toFloat()
                    } else {
                        pausePlay?.setImageResource(R.drawable.baseline_play_circle_24)
                        musicIcon?.rotation = 0f
                    }
                }
                Handler().postDelayed(this, 100)
            }
        })
        seekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar : SeekBar, progress : Int, fromUser : Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar : SeekBar) {}
            override fun onStopTrackingTouch(seekBar : SeekBar) {}
        })
    }

    fun setResourcesWithMusic() {
        currentSong = songsList!![MyMediaPlayer.currentIndex]
        titleTv!!.text = currentSong!!.title
        totalTimeTv!!.text = convertToMMSS(currentSong!!.duration)
        pausePlay!!.setOnClickListener { v : View? -> pausePlay() }
        nextBtn!!.setOnClickListener { v : View? -> playNextSong() }
        previousBtn!!.setOnClickListener { v : View? -> playPreviousSong() }
        playMusic()
    }

    private fun playMusic() {
        mediaPlayer!!.reset()
        try {
            mediaPlayer!!.setDataSource(currentSong!!.path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            seekBar!!.progress = 0
            seekBar!!.max = mediaPlayer!!.duration
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }

    private fun playNextSong() {
        if (MyMediaPlayer.currentIndex == songsList!!.size - 1) return
        MyMediaPlayer.currentIndex += 1
        mediaPlayer!!.reset()
        setResourcesWithMusic()
    }

    private fun playPreviousSong() {
        if (MyMediaPlayer.currentIndex == 0) return
        MyMediaPlayer.currentIndex -= 1
        mediaPlayer!!.reset()
        setResourcesWithMusic()
    }

    private fun pausePlay() {
        if (mediaPlayer!!.isPlaying) mediaPlayer!!.pause() else mediaPlayer!!.start()
    }

    companion object {
        fun convertToMMSS(duration : String) : String {
            val millis = duration.toLong()
            return String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
            )
        }
    }
    object MyMediaPlayer {
        private var instance : MediaPlayer? = null
        fun getInstance() : MediaPlayer {
            if (instance == null) {
                instance = MediaPlayer()
            }
            return instance as MediaPlayer
        }

        var currentIndex = -1
    }

}