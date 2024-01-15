package com.example.myapplication

import android.content.Intent
import android.media.audiofx.BassBoost
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi

import com.example.myapplication.databinding.ActivityMain1Binding
@UnstableApi class MainActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityMain1Binding

    override fun  onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ImageFragment())
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }}
        binding.bottomNavigationView.setOnItemSelectedListener {


            when(it.itemId){
                R.id.ic_images ->replaceFragment(ImageFragment())
                R.id.ic_pdf->replaceFragment(PdfFragment())
                R.id.ic_audio->replaceFragment(AudioFragment())
                R.id.ic_videos->replaceFragment(VideoFragment())
                else->{

                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment,fragment)
        fragmentTransaction.commit()

    }
}

