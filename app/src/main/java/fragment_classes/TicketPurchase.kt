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

class TicketPurchase() : Fragment() {
    private lateinit var binding: FragmentBuyTicketsBinding

    private lateinit var id_show: String
    private lateinit var type: String
    private lateinit var  period: String
    private lateinit var  selectedPlace: String
    private var  ticketQuantity: Int = 0
    private lateinit var  textTheatre: String

    constructor( id_show: String,
                 type: String,
                 period: String,
                 selectedPlace: String,
                 ticketQuantity: Int,
                 textTheatre: String): this(){
        this.id_show=id_show
        this.type=type
        this.period= period
        this.selectedPlace= selectedPlace
        this.ticketQuantity=ticketQuantity
        this.textTheatre=textTheatre
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("id_show",id_show)
        outState.putString("type",type)
        outState.putString("period",period)
        outState.putString("selectedPlace",selectedPlace)
        outState.putInt("ticketQuantity",ticketQuantity)
        outState.putString("textTheatre",textTheatre)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState!=null){
            this.id_show=savedInstanceState.getString("id_show").toString()
            this.type=savedInstanceState.getString("type").toString()
            this.period= savedInstanceState.getString("period").toString()
            this.selectedPlace= savedInstanceState.getString("selectedPlace").toString()
            this.ticketQuantity=savedInstanceState.getInt("ticketQuantity")
            this.textTheatre=savedInstanceState.getString("textTheatre").toString()
        }
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentBuyTicketsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Compra Biglietto")

        binding.confirmButton.setOnClickListener{
            var cardNumber = binding.cardField.text.toString()
            var numberCVC = binding.cvcField.text.toString()
            var expireYear = binding.cardExpireYearField.text.toString()
            var expireMonth = binding.cardExpireMonthField.text.toString()
            var isChecked = binding.saveCardBox.isChecked
            var name = binding.cardOwnerField.text.toString()

            var place: Char = choosePlace(textTheatre, selectedPlace)

            if(expireYear.length == 2){    //quindi se non inserisco il "20" prima dell'anno
                expireYear = "20" + expireYear
            }

            if ((cardNumber.length == 16) && (numberCVC.length == 3) && verifyExpire(expireYear, expireMonth)) {
                if(name.isNotEmpty()){
                    /*si dovrebbe anche fare un controllo per vedere se il nome del proprietario della carta corrisponde al numero però non
                    potendoci collegare ai server delle banche omettiamo il passaggio e controlliamo solo che il campo sia riempito*/
                    val utente: Int = MA.getUserId()
                    if(expireMonth.length == 1) {    //quindi se non inserisco il "20" prima dell'anno
                        expireMonth = "0${expireMonth}"
                    }
                    if(isChecked){
                        insertCartaCredito(utente, cardNumber, numberCVC, expireYear, expireMonth)
                    }
                    checkAvailableSeats(place,ticketQuantity,utente,MA)
                    binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
                    MA.syncDB()
                    MA.realAppNavigateTo(PaymentConfirmed("Biglietto"), "ConfirmedPayment")
                }else{
                    MA.showToast("Inserire nome")
                }
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
    /**
     * Inserisce un posto occupato nella tabella Occupazione_posti.
     *
     * @param x       Il numero del posto.
     * @param place   Il carattere che identifica il posto (es. 'A', 'B', 'C').
     * @param idShow  L'ID dello spettacolo correlato.
     */
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
    /**
     * Restituisce il carattere che identifica il posto selezionato in base al teatro e al posto scelto.
     *
     * @param textTheatre     Il testo del teatro selezionato.
     * @param selectedPlace   Il testo del posto selezionato.
     * @return Il carattere che identifica il posto.
     */
    private fun choosePlace(textTheatre: String, selectedPlace: String): Char {
        var place: Char ='A'
        if(textTheatre == "Teatro Massimo"){
            when(selectedPlace){
                "Platea" -> place = 'A'
                "Loggione" -> place = 'B'
                "Piccionaia" -> place = 'C'
            }
        }
        else if(textTheatre == "Teatro Politeama"){
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

    /**
     * Verifica se la data di scadenza è valida.
     *
     * @param year  L'anno di scadenza.
     * @param month Il mese di scadenza.
     * @return True se la data di scadenza è valida, False altrimenti.
     */
    private fun verifyExpire(year: String, month: String): Boolean {
        var expireYear: Int = Integer.parseInt(year)
        var expireMonth: Int = Integer.parseInt(month)
        val current_year = Calendar.getInstance().get(Calendar.YEAR)
        val current_month = Calendar.getInstance().get(Calendar.MONTH)
        if(expireMonth > 12 || expireMonth == 0){
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

    /**
     * Inserisce i dettagli della carta di credito nella tabella Carte_credito.
     *
     * @param utente        L'ID dell'utente associato alla carta di credito.
     * @param cardNumber    Il numero della carta di credito.
     * @param numberCVC     Il numero CVC della carta di credito.
     * @param expireYear    L'anno di scadenza della carta di credito.
     * @param expireMonth   Il mese di scadenza della carta di credito.
     */
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

    /**
     * Inserisce un biglietto singolo nella tabella Biglietto_singolo.
     *
     * @param utente   L'ID dell'utente associato al biglietto.
     * @param id_show  L'ID della rappresentazione associata al biglietto.
     */
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
    /**
     * Verifica la disponibilità dei posti e procede con l'acquisto dei biglietti.
     *
     * @param place  La lettera che identifica la zona dei posti.
     * @param num    Il numero di posti da acquistare.
     * @param utente L'ID dell'utente che effettua l'acquisto.
     * @param MA     L'istanza di MainActivity per mostrare i messaggi di notifica.
     */
    private fun checkAvailableSeats(place: Char, num: Int, utente: Int, MA: MainActivity) {
        val queryOccupiedSeats = "SELECT ref_posto_num " +
                "FROM Occupazione_posti " +
                "WHERE ref_posto_let = '${place}' " +
                "AND ref_rappresentazione_posti = '${id_show}' " +
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

                            for (i in 0 .. availableSeats.size - num) {
                                if (availableSeats[i + num - 1] - availableSeats[i] == num - 1) {
                                    startingSeat = availableSeats[i]
                                    endingSeat = availableSeats[i + num - 1]
                                    break
                                }
                            }

                            if (startingSeat != null && endingSeat != null) {
                                // Posti contigui disponibili
                                // Procedi con l'acquisto
                                for (x in startingSeat..endingSeat) {
                                    insertPosto(x, place, id_show)
                                    insertBigliettoInRemoto(utente, id_show)
                                }
                            } else {
                                // Non ci sono posti contigui
                                MA.showToast("Non ci sono posti contigui disponibili per il posto selezionato.")
                                MA.backTo("ChoosePlace")
                            }
                        } else if (availableSeats.size == 1 && num == 1) {
                            // Single ticket available
                            // Proceed with the ticket purchase
                            val startingSeat = availableSeats.first()
                            insertPosto(startingSeat, place, id_show)
                            insertBigliettoInRemoto(utente, id_show)
                        } else {
                            // Not enough available seats
                            MA.showToast("Non ci sono posti disponibili per il posto selezionato.")
                            MA.backTo("ChoosePlace")
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

}
