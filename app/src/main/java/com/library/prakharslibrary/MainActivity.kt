package com.library.prakharslibrary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.global.extensionlibrary.AndroidUtils
import com.learning.prakharslibrary.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testLibrary()
    }

    private fun testLibrary() {
        AndroidUtils.showToast(this, "Test Library", Toast.LENGTH_SHORT)
    }


}