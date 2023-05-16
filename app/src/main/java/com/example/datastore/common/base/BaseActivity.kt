package com.example.datastore.common.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.util.Locale


abstract class BaseActivity<VB : ViewBinding>  : AppCompatActivity() {

    private var _binding: VB? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) { // bu metod child classlarda implement qilinishi kerak emas!
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        setObserver()
        doWork()

    }

    abstract fun getViewBinding(): VB // ViewBindingni init qilish uchun
    abstract fun doWork() // asosiy ishlar qilinadigan metod
    abstract fun setObserver() // viewModelni barcha livedatalari shu metodni ichida observe qilishi shart!

    companion object {
        var dLocale: Locale? = null
    }


    init {
        updateConfig(this)
    }

    private fun updateConfig(wrapper: ContextThemeWrapper) {
        if(dLocale ==Locale("") ) // Do nothing if dLocale is null
            return

        dLocale?.let { Locale.setDefault(it) }
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }


}