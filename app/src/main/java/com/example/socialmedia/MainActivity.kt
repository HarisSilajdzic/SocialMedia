package com.example.socialmedia

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fragments.LoginFragment
import com.google.android.gms.auth.api.Auth


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