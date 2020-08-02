package com.example.wahooligan

import BindingActivity
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup


class MainActivity : AppCompatActivity() {



        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            BindingActivity()
    }







}