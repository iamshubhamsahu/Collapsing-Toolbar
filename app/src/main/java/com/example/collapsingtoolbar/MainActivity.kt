package com.example.collapsingtoolbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.actions.FloatAction
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingButton = findViewById(R.id.floating_action_button)

        floatingButton.setOnClickListener {
            startActivity(Intent(this,MainActivityTwo::class.java))
        }


    }
}