package fragment_classes

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentChoosePlaceBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChoosePlace(): Fragment() {
    private lateinit var binding: FragmentChoosePlaceBinding
    lateinit var id: String
    lateinit var type: String
    lateinit var period: String
    lateinit var textTheatre: String
    constructor(id: String, type: String,  period: String, textTheatre: String): this(){
        this.id=id
        this.type = type
        this.period = period
        this.textTheatre = textTheatre
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(this.isAdded) {
            outState.putString("id", id)
            outState.putString("type", type)
            outState.putString("period", period)
            outState.putString("textTheatre", textTheatre)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState!=null){
            this.id=savedInstanceState.getString("id").toString()
            this.type = savedInstanceState.getString("type").toString()
            this.period = savedInstanceState.getString("period").toString()
            this.textTheatre = savedInstanceState.getString("textTheatre").toString()

        }
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentChoosePlaceBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Scegli i posti")
        val places = getPlaces(textTheatre)
        getAvailableSeats(places,id)
        binding.confirmButton.setOnClickListener{
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            val selectedPlace = binding.placeSpinner.selectedItem.toString()
            val ticketQuantity = binding.ticketQuantity.text.toString().toInt()
            println("-----------------------------------")
            if(ticketQuantity != 0){
                MA.realAppNavigateTo(TicketPurchase(id, type, period, selectedPlace, ticketQuantity, textTheatre), "TicketPurchase")
            }
            else{
                MA.showToast("numero di biglietti non valido")
            }
        }


        return binding.root
    }

    private fun getAvailableSeats(places: Triple<Char, Char, Char>, idShow: String) {
        val place1 = places.component1() // Primo place
        val place2 = places.component2() // Secondo place
        val place3 = places.component3() // Terzo place
        var loggioneCount=0
        var plateaCount=0
        var piccionaiaCount=0
        val plateaQuery = "SELECT COUNT(*) AS platea_count " +
                "FROM Occupazione_posti " +
                "WHERE ref_posto_let = '${place1}' " +
                "AND ref_rappresentazione_posti = '${idShow}'"

        val piccionaiaQuery = "SELECT COUNT(*) AS piccionaia_count " +
                "FROM Occupazione_posti " +
                "WHERE ref_posto_let = '${place2}' " +
                "AND ref_rappresentazione_posti = '${idShow}'"

        val loggioneQuery = "SELECT COUNT(*) AS loggione_count " +
                "FROM Occupazione_posti " +
                "WHERE ref_posto_let = '${place3}' " +
                "AND ref_rappresentazione_posti = '${idShow}'"

        ApiService.retrofit.select(plateaQuery).enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val queryset = responseBody.getAsJsonArray("queryset")
                    if (queryset != null && queryset.size() > 0) {
                        val userJsonObject = queryset[0] as JsonObject
                        plateaCount = userJsonObject.get("platea_count")?.asInt ?: 0
                        val p=40-plateaCount
                        binding.plateaText.text = "Posti disponibili in platea: " + p.toString()
                        println(p)
                    } else {
                        // Nessun risultato trovato
                        println("Nessun risultato trovato")
                    }
                } else {
                    // Errore nella risposta
                    println("Errore nella risposta")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Gestione dell'errore di chiamata
                println("Errore nella chiamata")
            }
        })


        ApiService.retrofit.select(piccionaiaQuery).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val queryset = responseBody.getAsJsonArray("queryset")
                    if (queryset != null && queryset.size() > 0) {
                        val userJsonObject = queryset[0] as JsonObject
                         piccionaiaCount = userJsonObject.get("piccionaia_count")?.asInt ?: 0
                        val pi=40-piccionaiaCount
                        binding.piccionaiaText.text = "Posti disponibili in piccionaia: " + pi.toString()
                    } else {
                        // Nessun risultato trovato
                        println("Nessun risultato trovato")
                    }
                } else {
                    // Errore nella risposta
                    println("Errore nella risposta")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Gestione dell'errore di chiamata
                println("Errore nella chiamata")
            }
        })

        ApiService.retrofit.select(loggioneQuery).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val queryset = responseBody.getAsJsonArray("queryset")
                    if (queryset != null && queryset.size() > 0) {
                        val userJsonObject = queryset[0] as JsonObject
                         loggioneCount = userJsonObject.get("loggione_count")?.asInt ?: 0
                        val l=40-loggioneCount
                        binding.loggioneText.text = "Posti disponibili in loggione: " + l.toString()

                    } else {
                        // Nessun risultato trovato
                        println("Nessun risultato trovato")
                    }
                } else {
                    // Errore nella risposta
                    println("Errore nella risposta")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Gestione dell'errore di chiamata
                println("Errore nella chiamata")
            }
        })

    }

    private fun getPlaces(textTheatre: String): Triple<Char, Char, Char> {
        val places: Triple<Char, Char, Char> = when (textTheatre) {
            "Teatro Massimo" -> Triple('A', 'B', 'C')
            "Teatro Politeama" -> Triple('D', 'E', 'F')
            "Teatro Biondo" -> Triple('G', 'H', 'I')
            else -> Triple('-', '-', '-') // Valori di default nel caso in cui il teatro non sia valido
        }

        return places
    }






}