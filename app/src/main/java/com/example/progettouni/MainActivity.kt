package com.example.progettouni

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import api.ApiService
import api.UtenteJSON
import com.example.progettouni.databinding.ActivityMainBinding
import com.example.progettouni.databinding.RealAppBinding
import com.google.gson.JsonArray
import fragment_classes.Home
import fragment_classes.RealApp
import fragment_classes.SubscriptionPurchase
import fragment_classes.UserOrAdmin
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    fun loginCheck(mail: String, password: String) {

        val query = "select * from Utente where mail = '${mail}' and password = '${password}';"

       ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                                if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                                    realBinding = RealAppBinding.inflate(layoutInflater)
                            supportFragmentManager.popBackStack()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, RealApp())
                                .commit()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentContainerView4, Home())
                                .addToBackStack("Home")
                                .commit()

                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "credenziali errate",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    /*
                     * gestisci qui il fallimento della richiesta
                     */
                    handleNetworkFailure()
                }
            }
        )

    }


    private fun handleAuthenticationFailure() {
        Toast.makeText(this, "Autenticazione fallita. Credenziali errate.", Toast.LENGTH_SHORT).show()

    }

    private fun handleQueryFailure() {
        Toast.makeText(this, "Errore nella risposta della query.", Toast.LENGTH_SHORT).show()

    }

    private fun handleNetworkFailure() {
        Toast.makeText(this, "Errore di rete o altre eccezioni.", Toast.LENGTH_SHORT).show()

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