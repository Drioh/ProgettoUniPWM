package com.example.progettouni

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.progettouni.databinding.ActivityMainBinding
import com.example.progettouni.databinding.FragmentUserOrAdminBinding
import fragment_classes.Login
import fragment_classes.Register
import fragment_classes.UserOrAdmin

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var log_type: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    fun navigateTo(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,fragment).addToBackStack(null).commit()
    }
    fun back(){
        supportFragmentManager.popBackStack()
    }
    fun loginCheck(mail: String, password: String){
        println("mail inserita: " +mail)
        println("password inserita: "+password)
        if(mail== "cazzi" && password == "cazzi"){
            println("bravissimo")
        }
    }


}