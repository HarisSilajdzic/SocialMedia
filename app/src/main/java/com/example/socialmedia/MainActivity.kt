package com.example.socialmedia

import android.app.ActionBar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fragments.LoginFragment


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionBar: androidx.appcompat.app.ActionBar? = getSupportActionBar()
        actionBar!!.hide()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginFragment())
            .commitAllowingStateLoss()
    }




}