package com.example.datastore.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datastore.R
import com.example.datastore.common.base.BaseActivity
import com.example.datastore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun doWork() {

    }

    override fun setObserver() {

    }

}