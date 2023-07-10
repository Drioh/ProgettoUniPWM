package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

class SubscriptionPurchase() : Fragment() {
    private lateinit var binding: FragmentSubscriptionPurchaseBinding
    private var selectedbutton: Button? = null


    lateinit var theatre: String
    constructor(Theatre: String): this(){

        this.theatre = Theatre
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("Theatre",theatre)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState!=null){

            this.theatre = savedInstanceState.getString("Theatre").toString()

        }
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSubscriptionPurchaseBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Compra Abbonamento")
        var period: Int = 0

        binding.oneMonthButton.setOnClickListener{
            selectButton(binding.oneMonthButton)
            period = 1
        }
        binding.threeMonthButton.setOnClickListener{
            selectButton(binding.threeMonthButton)
            period = 3
        }
        binding.sixMonthButton.setOnClickListener{
            selectButton(binding.sixMonthButton)
            period = 6
        }
        binding.oneYearButton.setOnClickListener{
            selectButton(binding.oneYearButton)
            period = 12
        }
        binding.confirmButton.setOnClickListener(){

            var cardNumber = binding.cardField.text.toString()
            var numberCVC = binding.cvcField.text.toString()
            var expireYear = binding.cardExpireYearField.text.toString()
            var expireMonth = binding.cardExpireMonthField.text.toString()
            var isChecked = binding.saveCardBox.isChecked
            var name = binding.cardOwnerField.text.toString()

            if(expireYear.length == 2){    //quindi se non inserisco il "20" prima dell'anno
                expireYear = "20" + expireYear
            }

            var ref_theatre = getRefTheatre(theatre)

            //scelgo di non accorpare gli if perché voglio prima verificare il selectedButton e in caso passare al suo else e poi andare con l'altro
            if(selectedbutton!=null) {
                if((cardNumber.length == 16) && (numberCVC.length == 3) && verifyExpire(expireYear, expireMonth)){
                    if(name.isNotEmpty()){
                        //si dovrebbe anche fare un controllo per vedere se il nome del proprietario della carta corrisponde al numero però non
                        //potendoci collegare ai server delle banche omettiamo il passaggio e controlliamo solo che il campo sia riempito

                        var utente = MA.getUserId()

                        if(expireMonth.length == 1) {    //quindi se non inserisco il "20" prima dell'anno
                            expireMonth = "0${expireMonth}"
                        }
                        insertAbbonamentoInRemoto(ref_theatre, utente, period)
                        if(isChecked){
                            insertCartaCredito(utente, cardNumber, numberCVC, expireYear, expireMonth)
                        }
                        MA.syncDB()
                        MA.realAppNavigateTo(PaymentConfirmed("Abbonamento"), "ConfirmedPayment")
                    }
                    else{
                        MA.showToast("Inserire nome")
                    }
                }
                else {
                    MA.showToast("Carta non valida")
                }
            }else {
                MA.showToast("Selezionare prima un periodo di validità")
            }
        }
        binding.cancelButton.setOnClickListener(){
            MA.backTo("SubscriptionChoice")
        }

        return binding.root
    }
    /**
     * Restituisce il riferimento numerico corrispondente al teatro specificato.
     *
     * @param theatre Il nome del teatro.
     * @return Il riferimento numerico corrispondente al teatro. Se il teatro non è trovato, restituisce 0.
     */
    private fun getRefTheatre(theatre: String): Int {
        var teatro: Int = 0
        when (theatre) {
            "Massimo" -> {
                teatro = 1
            }
            "Politeama" -> {
                teatro = 2
            }
            "Biondo" -> {
                teatro = 3
            }
            else -> {
                println("Errore: teatro non trovato")

            }
        }
        return teatro
    }
    /**
     * Inserisce un abbonamento nel database remoto.
     *
     * @param theatre Il riferimento numerico del teatro.
     * @param utente Il riferimento numerico dell'utente.
     * @param period La durata dell'abbonamento in mesi.
     */
    private fun insertAbbonamentoInRemoto(theatre: Int, utente: Int, period: Int) {
        var currentDate = LocalDate.now()
        var subLength = Period.of(0, period, 0)   //inserisco 1, 3, 6 o 12 mesi al giorno corrente
        var lastMembershipDate = currentDate.plus(subLength)
        println(lastMembershipDate)
        val query = "insert into Abbonamento (ref_teatro, ref_utente, durata_mesi, data_scadenza ) values ('${theatre}', '${utente}', '${period}', '${lastMembershipDate}'); "
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
     * Seleziona un pulsante e modifica il suo stato visuale.
     *
     * @param button Il pulsante da selezionare.
     */
    private fun selectButton(button: Button){
        if(selectedbutton == null){
            button.setBackgroundColor(Color.parseColor("#F44336"))
            selectedbutton=button
        } else{
            selectedbutton!!.setBackgroundColor(Color.parseColor("#000000"))
            selectedbutton=button
            button.setBackgroundColor(Color.parseColor("#F44336"))
        }
    }

    /**
     * Verifica la validità della data di scadenza della carta di credito.
     *
     * @param year L'anno di scadenza della carta di credito.
     * @param month Il mese di scadenza della carta di credito.
     * @return true se la data di scadenza è valida, altrimenti false.
     */
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
    /**
     * Inserisce i dettagli della carta di credito nel database.
     *
     * @param utente Il riferimento numerico dell'utente.
     * @param cardNumber Il numero della carta di credito.
     * @param numberCVC Il numero CVC della carta di credito.
     * @param expireYear L'anno di scadenza della carta di credito.
     * @param expireMonth Il mese di scadenza della carta di credito.
     */
    private fun insertCartaCredito(utente: Int, cardNumber: String, numberCVC: String, expireYear: String, expireMonth: String) {
        var date = LocalDate.parse("${expireYear}-${expireMonth}-01")
        val query = "insert into Carte_credito (numero, ref_utente, data_scadenza_carta, cvc ) values ('${cardNumber}', '${utente}', '${date}', '${numberCVC}'); "
        ApiService.retrofit.insert(query).enqueue(
            object: Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println("prova: ${response.code()}")
                    Log.i("ApiService", "Inserimento avvenuto correttamente!")
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("ApiService", "Inserimento fallito")
                    Log.e("ApiService", t.message.toString())

                }
            }
        )
    }

}