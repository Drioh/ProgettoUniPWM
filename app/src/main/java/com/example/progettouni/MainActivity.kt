package com.example.progettouni

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.progettouni.databinding.ActivityMainBinding
import fragment_classes.Login

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun userLogin(view : View){
        supportFragmentManager.beginTransaction().replace(R.id.loginID,Login()).commit()
    }
}