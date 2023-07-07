package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import api.DBManager
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentBuyTicketsBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class TicketPurchase (var id: String, var type: String, var period: String) : Fragment(R.layout.fragment_buy_tickets) {
    private lateinit var binding: FragmentBuyTicketsBinding
    private lateinit var dbManager: DBManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyTicketsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        binding.confirmButton.setOnClickListener{
            dbManager = DBManager(requireContext())
            //inserire il biglietto nella posizione di id remoto
            var cardNumber = binding.cardField.toString()
            var numberCVC = binding.cvcField.toString()
            var expireYear = binding.cardExpireYearField.toString()
            var expireMonth = binding.cardExpireMonthField.toString()
            if ((cardNumber.length == 16) && (numberCVC.length == 3) && ) {       //inserire controllo per la data di scadenza
                val utente: Int = MA.getUserId()
                insertCartaCredito(utente, cardNumber, numberCVC, expireYear)
                binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
                insertBigliettoInRemoto(utente, id)
                insertBigliettoInLocale(utente, id, dbManager, type, period)
                MA.realAppNavigateTo(PaymentConfirmed("Biglietto"), "ConfirmedPayment")

            }else{
                MA.showToast("Carta non valida")
            }
        }
        binding.cancelButton.setOnClickListener{
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.backTo("Show")
        }


        return binding.root
    }

    private fun insertCartaCredito(utente: Int, cardNumber: String, numberCVC: String, expireDate: String) {


    }

    private fun insertBigliettoInLocale(
        userId: Int,
        ticket_id: String,
        dbManager: DBManager,
        name: String,
        date: String
    ) {
        val query = "select id_biglietto from Biglietto_singolo where ref_utente = '${userId}' and ref_rappresentazione_biglietto = '${ticket_id}';"
        ApiService.retrofit.select(query).enqueue(
            object : Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            val userJsonObject = (response.body()?.get("queryset") as JsonArray)[0] as JsonObject
                            val position = userJsonObject.get("id_biglietto").asInt
                            dbManager.insertBiglietto(position, name, date)
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("ApiService", "Connessione assente")
                    Log.e("ApiService", t.message.toString())
                }
            }
        )
    }

    private fun insertBigliettoInRemoto(utente: Int, id: String) {
        val query = "insert into Biglietto_singolo (ref_utente, ref_rappresentazione_biglietto ) values ('${utente}', '${id}'); "
        ApiService.retrofit.insert(query).enqueue(
            object: Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    //println(response.code())
                    //Log.i("ApiService", "Registrazione avvenuta correttamente!"
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("ApiService", "Pagamento fallito")
                    Log.e("ApiService", t.message.toString())

                }
            }
        )
    }
}