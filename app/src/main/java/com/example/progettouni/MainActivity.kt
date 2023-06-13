package com.example.progettouni

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.databinding.ActivityMainBinding
import com.example.progettouni.databinding.RealAppBinding
import fragment_classes.Home
import fragment_classes.RealApp
import fragment_classes.SubscriptionPurchase
import fragment_classes.UserOrAdmin
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var realBinding: RealAppBinding
    private lateinit var log_type: String
    private val apiService = ApiService()

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
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val utenti = apiService.getUtenti()
                println("Utenti nel database:")
                utenti?.forEach {
                    println("ID: ${it.id}")
                    println("Mail: ${it.mail}")
                    println("Nome: ${it.nome}")
                    println("Cognome: ${it.cognome}")
                    println("Password: ${it.password}")
                    println("Profilo: ${it.propic}")
                    println("Codice Verifica: ${it.cod_ver}")
                    println("Verificato: ${it.verificato}")
                    println("------------------------------")
                }
                if (!utenti.isNullOrEmpty()) {
                    val utenteTrovato = utenti.find { it.mail == mail && it.password == password }
                    if (utenteTrovato != null) {
                        // Utente trovato nel database
                        // Esegui l'azione di navigazione verso l'area riservata all'utente
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
                        // Utente non trovato o credenziali errate
                        // Gestisci il caso di autenticazione fallita
                        handleAuthenticationFailure()
                    }
                } else {
                    // Nessun utente nel database
                    // Gestisci il caso di autenticazione fallita
                    handleAuthenticationFailure()
                }
            } catch (e: Exception) {
                // Gestisci l'errore di rete o altre eccezioni
                handleNetworkFailure()
            }
        }
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