package com.example.progettouni

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.progettouni.databinding.ActivityMainBinding
import com.example.progettouni.databinding.RealAppBinding
import fragment_classes.Home
import fragment_classes.RealApp
import fragment_classes.SubscriptionPurchase
import fragment_classes.UserOrAdmin

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var realBinding: RealAppBinding
    private lateinit var log_type: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
    fun navigateTo(frag: Fragment,id: String){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,frag)
            .addToBackStack(id)
            .commit()

    }
    fun realAppNavigateTo(frag: Fragment,id: String){
        var old_id = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1).name
        if (old_id!=id){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView4,frag)
                .addToBackStack(id)
                .commit()
        }


    }
    fun back(){
        supportFragmentManager.popBackStack()
    }
    fun backTo(tag: String){
        supportFragmentManager.popBackStack(tag,0)
    }
    fun loginCheck(mail: String, password: String){
        if(mail== "cazzi" && password == "cazzi"){
            realBinding = RealAppBinding.inflate(layoutInflater)
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, RealApp())
                .commit()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView4, Home())
                .addToBackStack("Home")
                .commit()

        }
    }
    fun subChoice(teatro: String){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView4,SubscriptionPurchase(teatro))
            .addToBackStack("teatro "+teatro)
            .commit()
    }
    fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount ==1 && supportFragmentManager.getBackStackEntryAt(0).name == "Home"){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, UserOrAdmin())
                .commit()
        }
        super.getOnBackPressedDispatcher().onBackPressed()
    }


}