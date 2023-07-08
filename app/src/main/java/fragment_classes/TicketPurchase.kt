package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import java.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentBuyTicketsBinding
import com.example.progettouni.databinding.FragmentTopTaskbarBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class TicketPurchase(
    var id_show: String,
    var type: String,
    var period: String,
    var selectedPlace: String,
    var ticketQuantity: Int,
    var textTheatre: String
) : Fragment(R.layout.fragment_buy_tickets) {
    private lateinit var binding: FragmentBuyTicketsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyTicketsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Compra Biglietto")

        binding.confirmButton.setOnClickListener{
            var cardNumber = binding.cardField.text.toString()
            var numberCVC = binding.cvcField.text.toString()
            var expireYear = binding.cardExpireYearField.text.toString()
            var expireMonth = binding.cardExpireMonthField.text.toString()
            var isChecked = binding.saveCardBox.isChecked

            var place: Char = choosePlace(textTheatre, selectedPlace)

            expireYear = adjustYear(expireYear)
            if ((cardNumber.length == 16) && (numberCVC.length == 3) && verifyExpire(expireYear, expireMonth)) {
                //si dovrebbe anche fare un controllo per vedere se il nome del proprietario della carta corrisponde al numero però non
                //potendoci collegare ai server delle banche omettiamo il passaggio
                val utente: Int = MA.getUserId()
                if(expireMonth.length == 1) {    //quindi se non inserisco il "20" prima dell'anno
                    expireMonth = "0${expireMonth}"
                }
                if(isChecked){
                    insertCartaCredito(utente, cardNumber, numberCVC, expireYear, expireMonth)
                }
                checkAvailableSeats(place,ticketQuantity,utente)
                binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
                MA.syncDB()
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

    private fun insertPosto(x: Int, place: Char, idShow: String) {
        val query = "insert into Occupazione_posti (ref_posto_let, ref_rappresentazione_posti, ref_posto_num) values ('${place}', '${id_show}', '${x}'); "
        ApiService.retrofit.insert(query).enqueue(
            object: Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println(response.code())
                    Log.i("ApiService", "Inserimento avvenuto correttamente!")
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("ApiService", "Pagamento fallito")
                    Log.e("ApiService", t.message.toString())

                }
            }
        )
    }

    private fun choosePlace(textTheatre: String, selectedPlace: String): Char {
        var place: Char ='A'
        if(textTheatre == "Teatro Massimo"){
            when(selectedPlace){
                "Platea" -> place = 'A'
                "Loggione" -> place = 'B'
                "Piccionaia" -> place = 'C'
            }
        }
        else if(textTheatre == "Politeama"){
            when(selectedPlace){
                "Platea" -> place = 'D'
                "Loggione" -> place = 'E'
                "Piccionaia" -> place = 'F'
            }
        }
        else if(textTheatre == "Teatro Biondo"){
            when(selectedPlace){
                "Platea" -> place = 'G'
                "Loggione" -> place = 'H'
                "Piccionaia" -> place = 'I'
            }
        }
        return place
    }

    private fun adjustYear(expireYear: String): String {
        var year: Int = Integer.parseInt(expireYear)
        if(year < 100){    //quindi se non inserisco il "20" prima dell'anno
            year += 2000
        }
        return year.toString()
    }

    private fun verifyExpire(year: String, month: String): Boolean {
        var expireYear: Int = Integer.parseInt(year)
        var expireMonth: Int = Integer.parseInt(month)
        val current_year = Calendar.getInstance().get(Calendar.YEAR)
        val current_month = Calendar.getInstance().get(Calendar.MONTH)
        if(expireMonth > 12){
            return false
        }
        if(expireYear-current_year > 0){
            return true
        }
        else if((expireYear-current_year == 0) && (expireMonth-current_month >= 0)){
            return true
        }
    return false
    }

    private fun insertCartaCredito(utente: Int, cardNumber: String, numberCVC: String, expireYear: String, expireMonth: String) {
        var date = LocalDate.parse("${expireYear}-${expireMonth}-01")
        val query = "insert into Carte_credito (numero, ref_utente, data_scadenza_carta, cvc ) values ('${cardNumber}', '${utente}', '${date}', '${numberCVC}'); "
        ApiService.retrofit.insert(query).enqueue(
            object: Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println(response.code())
                    Log.i("ApiService", "Inserimento avvenuto correttamente!")
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("ApiService", "Inserimento fallito")
                    Log.e("ApiService", t.message.toString())
                }
            }
        )
    }

    private fun insertBigliettoInRemoto(utente: Int, id_show: String) {
        val query = "insert into Biglietto_singolo (ref_utente, ref_rappresentazione_biglietto ) values ('${utente}', '${id_show}'); "
        ApiService.retrofit.insert(query).enqueue(
            object: Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println(response.code())
                    Log.i("ApiService", "Inserimento avvenuto correttamente!")
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("ApiService", "Pagamento fallito")
                    Log.e("ApiService", t.message.toString())

                }
            }
        )
    }
    private fun checkAvailableSeats(place: Char, num: Int, utente: Int) {
        val queryOccupiedSeats = "SELECT ref_posto_num " +
                "FROM Occupazione_posti " +
                "WHERE ref_posto_let = '$place' " +
                "AND ref_rappresentazione_posti = '$id_show' " +
                "ORDER BY ref_posto_num"

        ApiService.retrofit.select(queryOccupiedSeats).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val querySet = response.body()?.getAsJsonArray("queryset")
                        val occupiedSeats = querySet?.map { it.asJsonObject["ref_posto_num"].asInt }?.toMutableList()

                        val availableSeats = mutableListOf<Int>()

                        for (i in 1..40) {
                            if (!occupiedSeats?.contains(i)!!)
                                availableSeats.add(i)
                        }

                        if (availableSeats.size >= num) {
                            var startingSeat: Int? = null
                            var endingSeat: Int? = null

                            for (i in 0 until availableSeats.size - num + 1) {
                                if (availableSeats[i + num - 1] - availableSeats[i] == num - 1) {
                                    startingSeat = availableSeats[i]
                                    endingSeat = availableSeats[i + num - 1]
                                    break
                                }
                            }

                            if (startingSeat != null && endingSeat != null) {
                                // Contiguous seats are available
                                // Proceed with the ticket purchase
                                for (x in startingSeat..endingSeat) {
                                    insertPosto(x, place, id_show)
                                    insertBigliettoInRemoto(utente, id_show)
                                }
                            } else {
                                // No contiguous seats available
                                showToast("Non ci sono posti contigui disponibili per il posto selezionato.")
                            }
                        } else if (availableSeats.size == 1 && num == 1) {
                            // Single ticket available
                            // Proceed with the ticket purchase
                            val startingSeat = availableSeats.first()
                            insertPosto(startingSeat, place, id_show)
                            insertBigliettoInRemoto(utente, id_show)
                        } else {
                            // Not enough available seats
                            showToast("Non ci sono posti disponibili per il posto selezionato.")
                        }
                    } else {
                        Log.i("ApiService", "Errore nella query dei posti occupati")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("ApiService", t.message.toString())
                }
            }
        )
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
