package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMain1Binding

class MainActivity1 : AppCompatActivity() {

    private lateinit var binding : ActivityMain1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(FirstFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.ic_images-> replaceFragment(FirstFragment())
                R.id.ic_videos -> replaceFragment(SecondFragment())
                R.id.ic_audio -> replaceFragment(ThirdFragment())
                R.id.ic_pdf -> replaceFragment(FourthFragment())

                else ->{



                }

            }

            true

        }


    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.bottomNavigationView,fragment)
        fragmentTransaction.commit()


    }
}