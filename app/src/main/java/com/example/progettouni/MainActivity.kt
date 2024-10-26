package com.example.progettouni

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import api.ApiService
import api.DBHelper
import api.DBManager
import com.example.progettouni.databinding.ActivityMainBinding
import com.example.progettouni.databinding.RealAppBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import fragment_classes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var realBinding: RealAppBinding
    private lateinit var log_type: String
    private lateinit var db: DBManager
    private var userId: Int = 0 // Variabile per l'ID dell'utente
    private lateinit var sharedPreferences: SharedPreferences
    private var isOffline: Boolean = false

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        Viene effettuata una query di test all'avvio dell'app. La query è di tipo ansincrono e permette all'applicazione di entrare, o meno, in modalità offline. Una volta ottenuta o meno
        una risposta dal database, verrà mostrata all'utente la schermata iniziale.
        Ciò si traduce nel sacrificio di velocità e nel dovere fare aspettare all'utente un paio di secondi prima di potere entrare nell'app.
        Le modalità di inflate cambiano anche se è presente o meno un savedInstanceState che, se presente, evita che venga effettuata di nuovo la query di apertura
         */
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            db = DBManager(this@MainActivity)
            db.open()

            // Controllo se ci sono credenziali salvate nelle SharedPreferences
            sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val email = sharedPreferences.getString("email", "")
            val password = sharedPreferences.getString("password", "")
            // Eseguo automaticamente l'accesso al profilo dell'utente
            loadUserData()
            isOffline = savedInstanceState.getBoolean("isOffline")
            realBinding = RealAppBinding.inflate(layoutInflater)
            cleanDB()
            syncDB()

        } else {

            val query = "SELECT * FROM Teatro;"
            ApiService.retrofit.select(query).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.i("ApiService", "Success")
                        isOffline = false
                        binding = ActivityMainBinding.inflate(layoutInflater)
                        val view = binding.root
                        setContentView(view)
                        db = DBManager(this@MainActivity)
                        db.open()

                        // Controllo se ci sono credenziali salvate nelle SharedPreferences
                        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                        val email = sharedPreferences.getString("email", "")
                        val password = sharedPreferences.getString("password", "")
                        // Eseguo automaticamente l'accesso al profilo dell'utente
                        loadUserData()
                        realBinding = RealAppBinding.inflate(layoutInflater)
                        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {

                            supportFragmentManager.popBackStack()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, RealApp())
                                .commit()

                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentContainerView2, TopTaskbar())
                                .commit()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentContainerView3, BottomTaskbar())
                                .commit()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentContainerView4, Home())
                                .addToBackStack("Home")
                                .commit()
                        }
                        cleanDB()
                        syncDB()

                        if (isOffline) {
                            showToast("OFFLINE MODE ON")
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("offline", t.message.toString())
                    isOffline = true
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    val view = binding.root
                    setContentView(view)
                    db = DBManager(this@MainActivity)
                    db.open()

                    // Controllo se ci sono credenziali salvate nelle SharedPreferences
                    sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                    val email = sharedPreferences.getString("email", "")
                    val password = sharedPreferences.getString("password", "")
                    // Eseguo automaticamente l'accesso al profilo dell'utente
                    loadUserData()
                    realBinding = RealAppBinding.inflate(layoutInflater)
                    if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {

                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, RealApp())
                            .commit()

                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainerView2, TopTaskbar())
                            .commit()
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainerView3, BottomTaskbar())
                            .commit()
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainerView4, Home())
                            .addToBackStack("Home")
                            .commit()
                    }
                    cleanDB()
                    syncDB()
                }
            })

        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    fun changeTitle (s:String){
        realBinding.fragmentContainerView2.getFragment<TopTaskbar>().binding.TopTaskbarText.text = s
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isOffline", isOffline)
        super.onSaveInstanceState(outState)
    }


    fun getIsOffline(): Boolean {
        return isOffline
    }
    fun getUserId(): Int {
        return userId
    }
    fun setUserId(id: Int){
        userId = id
    }
    fun getSharedPreferences(): SharedPreferences {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = applicationContext.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }
    /**
     *Questo metodo viene invocato quando, prima di avere effettuato il login, ci si deve
     *spostare tra le pagine di registrazione e login.
     *Metodo creato per facilitare il movimento tra fragment.
     *@param frag: Fragment che si desidera raggiungere
     *@param id: nome con cui si vuole salvare la transazione
     */
    fun navigateTo(frag: Fragment,id: String){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,frag)
            .addToBackStack(id)
            .commit()
    }
    /**
     *Questo metodo viene invocato e utilizzato quando, dopo avere effettuato il login, ci si
     *deve spostare tra le pagine dell'applicazione.
     *viene utilizzato un'altro FragmentContainerView perchè appartenente a un'altro layout, che
     *questa volta comprende anche taskbar inferiore e superiore.
     *Metodo creato per facilitare il movimento tra fragment.
     *@param frag: Fragment che si desidera raggiungere
     *@param id: nome con cui si vuole salvare la transazione
     */
    fun realAppNavigateTo(frag: Fragment, id: String){
        var old_id = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1).name
        if (old_id!=id){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView4,frag)
                .addToBackStack(id)
                .commit()
        }
    }
    /**
     *Questo metodo viene invocato ogni qual volta che la funzionalità di un bottone è quella
     *di tornare alla pagina precedentemente salvata nel backstack
     */
    fun back(){
        supportFragmentManager.popBackStack()
    }
    /**
     *Questo metodo viene invocato ogni qual volta che la funzionalità di un bottone è quella
     *di tornare alla pagina precedentemente salvata nel backstack.
     *si specifica il fragent a cui si deve arrivare, permettendo quindi di fare il pop di più
     *fragment contemporaneamente fino a quello desiderato.
     *@param tag: nome della transazione fino alla quale si vuole fare il pop del backstack
     */
    fun backTo(tag: String){
        supportFragmentManager.popBackStack(tag,0)
    }
    /**
     *Verifica le credenziali di accesso dell'utente se sono corrette porta alla home dell'applicazione.
     *@param mail: Indirizzo email dell'utente.
     *@param password: Password dell'utente.
     */
    fun loginCheck(mail: String, password: String) {
        val query = "select * from Utente where mail = '${mail}' and password = '${password}';"
        ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {

                            val userJsonObject = (response.body()?.get("queryset") as JsonArray)[0] as JsonObject

                            setUserId(userJsonObject.get("id_utente").asInt) // Assegna l'ID dell'utente alla variabile userId

                            val userName = userJsonObject.get("nome_utente").asString
                            val surname = userJsonObject.get("cognome").asString
                            val pw = userJsonObject.get("password").asString
                            val  email = userJsonObject.get("mail").asString
                            this@MainActivity.saveUserData(userId, userName, surname, email, pw)

                            realBinding = RealAppBinding.inflate(layoutInflater)
                            supportFragmentManager.popBackStack()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, RealApp())
                                .commit()

                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentContainerView2, TopTaskbar())
                                .commit()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentContainerView3, BottomTaskbar())
                                .commit()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentContainerView4, Home())
                                .addToBackStack("Home")
                                .commit()

                            syncDB()
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
     * Questo metodo si occupa della sincronizzazione del database locale partendo dai dati presenti nel database remoto.
     * Si controlla quali id di abbonamenti e biglietti NON sono presenti nel database locale ma sono presenti nel database remoto, e vengono implementati
     * MANTENENDO GLI ID CONSISTENTI (se un biglietto ha id = x nel database remoto, anche nel database locale tale biglietto presenterà id = x
     */
    @SuppressLint("Range")
    fun syncDB(){
        data class Abbonamento(var id : Int, var teatro: String, var dataInizio: String, var dataFine: String)
        data class Biglietto(var id : Int, var nomeSpettacolo: String, var dataScadenza: String)
        var abbLocaliI  = emptyList<Int>().toMutableList()
        var tickLocaliI  = emptyList<Int>().toMutableList()
        var abb = db.fetchAllAbbonamenti()
        var tick = db.fetchAllBiglietti()
        if (abb.count !=0){
            do{
                abbLocaliI.add(abb.getInt(abb.getColumnIndex(DBHelper._ID_ABBONAMENTO)))
            }while(abb.moveToNext())}
        println("Abbonamenti Locali: "+abbLocaliI) // DEBUG
        if(tick.count !=0){
            do{
                tickLocaliI.add(tick.getInt(tick.getColumnIndex(DBHelper._ID_BIGLIETTO)))
            }while(tick.moveToNext())}
        println("Biglietti Locali: "+tickLocaliI) // DEBUG

        //BIGLIETTI
        var query = "select * " +
                "from Biglietto_singolo, Spettacolo, Rappresentazione " +
                "where id_rappresentazione=ref_rappresentazione_biglietto and " +
                "ref_spettacolo = id_spettacolo and " +
                "ref_utente = ${userId};"
        ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        var risposta = response.body()?.get("queryset") as JsonArray
                        var Biglietti  = emptyList<Biglietto>().toMutableList()
                        for (i in 0 until risposta.count()){
                            var elemento = risposta[i] as JsonObject
                            println("Id biglietto "+i+" database: "+ elemento.get("id_biglietto").asInt)
                            if(!(elemento.get("id_biglietto").asInt in tickLocaliI)){
                                println("c'è un biglietto non trovato, procedo alla sincronizzazione")
                                Biglietti.add(
                                    Biglietto(
                                        elemento.get("id_biglietto").asInt,
                                        elemento.get("nome_spettacolo").asString,
                                        elemento.get("data").asString
                                    )
                                )

                            }else{
                                println("trovato biglietto!")

                            }
                        }
                        for (i in 0 until Biglietti.size){
                            db.insertBiglietto(
                                Biglietti[i].id,
                                Biglietti[i].nomeSpettacolo,
                                Biglietti[i].dataScadenza
                            )
                        }
                    } else {
                        showToast("Richiesta biglietti non andata a buon termine")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showToast("Errore di rete")
                }
            }
        )
        //ABBONAMENTI
        query = "select * " +
                "from Abbonamento, Teatro " +
                "where id_teatro=ref_teatro and " +
                "ref_utente = ${userId};"
        ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        var risposta = response.body()?.get("queryset") as JsonArray
                        var Abbonamenti = emptyList<Abbonamento>().toMutableList()
                        for (i in 0 until risposta.count()) {
                            var elemento = risposta[i] as JsonObject

                            if (!(elemento.get("id_abbonamento").asInt in abbLocaliI)) {
                                println("C'è un abbonamento non trovato, procedo alla sincronizzazione")
                                Abbonamenti.add(
                                    Abbonamento(
                                        elemento.get("id_abbonamento").asInt,
                                        elemento.get("nome_teatro").asString,
                                        LocalDate.now().toString(),
                                        elemento.get("data_scadenza").asString
                                    )
                                )

                            }else{
                                println("trovato abbonamento!")
                            }
                        }
                        for (i in 0 until Abbonamenti.size) {
                            db.insertAbbonamento(
                                Abbonamenti[i].id,
                                Abbonamenti[i].teatro,
                                Abbonamenti[i].dataInizio,
                                Abbonamenti[i].dataFine
                            )
                        }
                    }else {
                        showToast("Richiesta abbonamenti non andata a buon termine")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showToast("Errore di rete")
                }
            }
        )
    }
    /**
     * Questo metodo si occupa di eseguire il logout dell'utente cancellando i suoi dati dalle shared preferences, in modo da non rendere automatico il login all'accesso successivo.
     * Inoltre viene ripulito il database locale, in modo tale da potere i dati su biglietti e abbonamenti separati in caso si loggasse con credenziali diverse
     */
    @SuppressLint("Range")
    fun logout() {
        for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
            supportFragmentManager.popBackStack()
        }
        var abb = db.fetchAllAbbonamenti()
        var tick = db.fetchAllBiglietti()
        if (abb.count !=0){
            do{
                db.deleteAbbonamento(abb.getInt(abb.getColumnIndex(DBHelper._ID_ABBONAMENTO)))
            }while(abb.moveToNext())}
        if(tick.count !=0){
            do{
                db.deleteBiglietto(tick.getInt(tick.getColumnIndex(DBHelper._ID_BIGLIETTO)))
            }while(tick.moveToNext())}
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, Login())
            .commit()
        setUserId(0)
    }

    /**
     * Mostra un Toast con il messaggio specificato.
     *
     * @param message Il messaggio da visualizzare nel Toast.
     */
    fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
    /**Metodo che gestisce tramite override il bottone back di android.
     *Nel caso in cui si arrivi al fragment di home, se non ci sono fragment a cui ritornare,
     *si opta per forzare un ritorno alla schermata di login
     */
    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount ==1){
            finish()
        }
        super.getOnBackPressedDispatcher().onBackPressed()
    }

    /**
     * Questo metodo salva i dati dell'utente nelle shared preferences per potere accedere offline all'applicazione
     */
    private fun saveUserData(userId: Int, userName: String,  surname: String , email: String, password: String) {
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("userId", userId)
        editor.putString("userName", userName)
        editor.putString("surname", surname)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    /**
     * Questo metodo carica i dati dell'utente dalle shared preferences
     */
    private fun loadUserData() {
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("userId", 0)
        val  userName = sharedPreferences.getString("userName", "") ?: ""
        val  email = sharedPreferences.getString("email", "") ?: ""
        val  pw = sharedPreferences.getString("password", "") ?: ""
    }

    /**
     * Pulisce il database eliminando i biglietti scaduti e gli abbonamenti scaduti.
     */
    @SuppressLint("Range")
    fun cleanDB(){
        //BIGLIETTI
        var query = "select * " +
                "from Biglietto_singolo, Spettacolo, Rappresentazione " +
                "where id_rappresentazione=ref_rappresentazione_biglietto and " +
                "ref_spettacolo = id_spettacolo and " +
                "ref_utente = ${userId};"
        ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        var risposta = response.body()?.get("queryset") as JsonArray
                        var id = emptyList<String>().toMutableList()
                        var idtick = emptyList<String>().toMutableList()
                        for (i in 0 until risposta.count()){
                            var elemento = risposta[i] as JsonObject
                            if (LocalDate.parse(elemento.get("data").toString().substring(1,elemento.get("data").toString().length-1)).isBefore(LocalDate.now())){
                                id.add(elemento.get("id_rappresentazione").toString())
                                idtick.add(elemento.get("id_biglietto").toString())
                            }
                        }

                        for (i in 0 until id.size){
                            var query = "delete from Biglietto_singolo where ref_rappresentazione_biglietto = '${id.get(i)}'"
                            ApiService.retrofit.select(query).enqueue(
                                object : Callback <JsonObject> {
                                    override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                                    }
                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        showToast("Errore di rete")
                                    }
                                }
                            )
                            db.deleteBiglietto(idtick.get(i).toInt())
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showToast("Errore di rete")
                }
            }
        )
        //ABBONAMENTI
        query = "delete from Abbonamento where data_scadenza < '${LocalDate.now()}';"
        ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showToast("Errore di rete")
                }
            }
        )
        var abb = db.fetchAllAbbonamenti()
        var id = emptyList<String>().toMutableList()
        if (abb.count !=0){
            do{
                if(LocalDate.parse((abb.getString(abb.getColumnIndex(DBHelper.DATA_FINE)))).isBefore(LocalDate.now()))
                    id.add(abb.getString(abb.getColumnIndex(DBHelper._ID_ABBONAMENTO)))
            }while(abb.moveToNext())}
    }


}