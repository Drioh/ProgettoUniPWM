package com.example.progettouni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.progettouni.databinding.ActivityMainBinding
import fragment_classes.RealApp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var log_type: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    fun navigateTo(frag: Fragment,id: String){
        println(id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,frag)
            .addToBackStack(id)
            .commit()
    }
    fun back(){
        supportFragmentManager.popBackStack()
    }
    fun loginCheck(mail: String, password: String){
        if(mail== "cazzi" && password == "cazzi"){
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, RealApp())
                .addToBackStack("realapp")
                .commit()
        }
    }


}