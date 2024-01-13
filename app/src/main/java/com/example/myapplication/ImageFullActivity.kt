package com.example.myapplication

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class ImageFullActivity : AppCompatActivity() {
    var fullImage : ImageView? = null
    private var image : String? = ""
    private var scaleGestureDetector : ScaleGestureDetector? = null
    var scaleFactor = 1.0f

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_full_activity)
        fullImage = findViewById(R.id.full_image)
        val intent = intent
        image = intent.getStringExtra("parseData")
         Glide.with(this)
            .load(image)
            .into(fullImage!!)
        scaleGestureDetector = ScaleGestureDetector(
            this,
            ScaleListener()
        )
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        scaleGestureDetector!!.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector : ScaleGestureDetector) : Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = 0.1f.coerceAtLeast(scaleFactor.coerceAtMost(10.0f))
            fullImage!!.scaleX = scaleFactor
            fullImage!!.scaleY = scaleFactor
            return true
        }
    }
}