package com.example.progettouni

import android.os.Bundle
import android.util.Log
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
    private var userId: Int = 0 // Variabile per l'ID dell'utente
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    fun getUserId(): Int {
        return userId
    }
    /**questo metodo viene invocato quando, prima di avere effettuato il login, ci si deve
     * spostare tra le pagine di registrazione e login.
     *  Metodo creato per facilitare il movimento tra fragment.
     *  @param frag Fragment che si desidera raggiungere
     *  @param id nome con cui si vuole salvare la transazione
     */
    fun navigateTo(frag: Fragment,id: String){

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,frag)
            .addToBackStack(id)
            .commit()
    }
    /**
     *  questo metodo viene invocato e utilizzato quando, dopo avere effettuato il login, ci si
     *  deve spostare tra le pagine dell'applicazione.
     *  viene utilizzato un'altro FragmentContainerView perchè appartenente a un'altro layout, che
     *  questa volta comprende anche taskbar inferiore e superiore.
     *  Metodo creato per facilitare il movimento tra fragment.
     *  @param frag: Fragment che si desidera raggiungere
     *  @param id: nome con cui si vuole salvare la transazione
     */
    fun realAppNavigateTo(frag: Fragment,id: String){

        var old_id = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1).name
        if (old_id!=id){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView4,frag)
                .addToBackStack(id)
                .commit()
        }
    }
    /**
     *  Questo metodo viene invocato ogni qual volta che la funzionalità di un bottone è quella
     *  di tornare alla pagina precedentemente salvata nel backstack
     */
    fun back(){

        supportFragmentManager.popBackStack()
    }
    /**
     *  Questo metodo viene invocato ogni qual volta che la funzionalità di un bottone è quella
     *  di tornare alla pagina precedentemente salvata nel backstack.
     *  si specifica il fragent a cui si deve arrivare, permettendo quindi di fare il pop di più
     *  fragment contemporaneamente fino a quello desiderato.
     *  @param tag: nome della transazione fino alla quale si vuole fare il pop del backstack
     */
    fun backTo(tag: String){

        supportFragmentManager.popBackStack(tag,0)
    }
    /**
     *   Verifica le credenziali di accesso dell'utente se sono corrette porta alla home dell'applicazione.
     *   @param mail: Indirizzo email dell'utente.
     *   @param password: Password dell'utente.
     */
    fun loginCheck(mail: String, password: String) {
        val query = "select * from Utente where mail = '${mail}' and password = '${password}';"
        ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            val userJsonObject = (response.body()?.get("queryset") as JsonArray)[0] as JsonObject
                            userId = userJsonObject.get("id_utente").asInt // Assegna l'ID dell'utente alla variabile userId

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
                            showToast("Credenziali Errate")
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showToast("Errore di rete")
                }
            }
        )

    }
    /**
     *  questo metodo viene invocato quando viene selezionato un teatro per il quale acquistare
     *  un abbonamento.
     *  @param teatro: nome del teatro selezionato (uno tra Politeama, Biondo e Massimo)
     */
    fun subChoice(teatro: String){

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView4,SubscriptionPurchase(teatro))
            .addToBackStack("teatro "+teatro)
            .commit()
    }
    /**
     *  Questo metodo viene invocato ogni qual volta che si vuole mostrare a schermo un toast.
     *  Metodo creato per facilitare la creazione di Toast
     *  @param text: testo da mostrare a schermo tramite toast
     */
    fun showToast(text: String){

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
    /**
     *  Metodo che gestisce tramite override il bottone back di android.
     *  Nel caso in cui si arrivi al fragment di home, se non ci sono fragment a cui ritornare,
     *   si opta per forzare un ritorno alla schermata di login
     */
    override fun onBackPressed() {

        if(supportFragmentManager.backStackEntryCount ==1 && supportFragmentManager.getBackStackEntryAt(0).name == "Home"){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, UserOrAdmin())
                .commit()
        }
        super.getOnBackPressedDispatcher().onBackPressed()
    }
}