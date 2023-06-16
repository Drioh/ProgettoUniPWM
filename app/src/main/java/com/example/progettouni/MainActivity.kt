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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
    fun navigateTo(frag: Fragment,id: String){
        /*questo metodo viene invocato quando, prima di avere effettuato il login, ci si deve
        spostare tra le pagine di registrazione e login.
        Metodo creato per facilitare il movimento tra fragment.
        INPUT:
        frag: Fragment che si desidera raggiungere
        id: nome con cui si vuole salvare la transazione
        */
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,frag)
            .addToBackStack(id)
            .commit()

    }
    fun realAppNavigateTo(frag: Fragment,id: String){
        /*questo metodo viene invocato e utilizzato quando, dopo avere effettuato il login, ci si
        deve spostare tra le pagine dell'applicazione.
        viene utilizzato un'altro FragmentContainerView perchè appartenente a un'altro layout, che
        questa volta comprende anche taskbar inferiore e superiore.
        Metodo creato per facilitare il movimento tra fragment.
        INPUT:
        frag: Fragment che si desidera raggiungere
        id: nome con cui si vuole salvare la transazione
        */
        var old_id = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1).name
        if (old_id!=id){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView4,frag)
                .addToBackStack(id)
                .commit()
        }


    }
    fun back(){
        /*questo metodo viene invocato ogni qual volta che la funzionalità di un bottone è quella
        di tornare alla pagina precedentemente salvata nel backstack*/
        supportFragmentManager.popBackStack()
    }
    fun backTo(tag: String){
        /*questo metodo viene invocato ogni qual volta che la funzionalità di un bottone è quella
        di tornare alla pagina precedentemente salvata nel backstack.
        si specifica il fragent a cui si deve arrivare, permettendo quindi di fare il pop di più
        fragment contemporaneamente fino a quello desiderato.
        INPUT:
        tag: nome della transazione fino alla quale si vuole fare il pop del backstack*/
        supportFragmentManager.popBackStack(tag,0)
    }

    fun loginCheck(mail: String, password: String) {

        val query = "select * from Utente where mail = '${mail}' and password = '${password}';"

        ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
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
                            showToast("Credenziali Errate")

                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showToast("Errore di rete")
                }
            }
        )
        TODO("Verificare se è possibile creare una sola funzione per effettuare tutte le query")
    }

    fun subChoice(teatro: String){
        /*questo metodo viene invocato quando viene selezionato un teatro per il quale acquistare
        un abbonamento.
        INPUT:
        teatro: nome del teatro selezionato (uno tra Politeama, Biondo e Massimo)*/
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView4,SubscriptionPurchase(teatro))
            .addToBackStack("teatro "+teatro)
            .commit()
    }
    fun showToast(text: String){
        /*Questo metodo viene invocato ogni qual volta che si vuole mostrare a schermo un toast.
        Metodo creato per facilitare la creazione di Toast
        INPUT:
        text: testo da mostrare a schermo tramite toast
         */
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        /*Metodo che gestisce tramite override il bottone back di android.
        Nel caso in cui si arrivi al fragment di home, se non ci sono fragment a cui ritornare,
        si opta per forzare un ritorno alla schermata di login*/
        if(supportFragmentManager.backStackEntryCount ==1 && supportFragmentManager.getBackStackEntryAt(0).name == "Home"){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, UserOrAdmin())
                .commit()
        }
        super.getOnBackPressedDispatcher().onBackPressed()
    }



}