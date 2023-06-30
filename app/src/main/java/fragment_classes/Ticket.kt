package fragment_classes

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentTicketBinding
import com.example.progettouni.databinding.RealAppBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.util.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Ticket(private val isAbbonamento: Boolean, var id: String, val teatro: String): Fragment(R.layout.fragment_ticket) {
    private lateinit var binding: FragmentTicketBinding
    private lateinit var button: Button
    private lateinit var text: TextView
    private lateinit var image: ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTicketBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        image = binding.imageView
        if(isAbbonamento){
            binding.showInfoButton.setText(R.string.infoMembership)
            binding.textView.setText(R.string.myMembership)
            // modifica della stringa dell'id per aggiungere varietà in modo tale da garantire l'unicità del codice QR
            id = id+"ABBONAMENTO"
            binding.showInfoButton.setOnClickListener(){
                println("Teatrazzo")
                MA.realAppNavigateTo(TheatreInfo(teatro, false),"TheatreInfo")
            }
        }
        else{
            if(!isAbbonamento){
                /*binding.showInfoButton.setText(R.string.infoTicket)
                binding.textView.setText(R.string.myTicket)
                // modifica della stringa dell'id per aggiungere varietà in modo tale da garantire l'unicità del codice QR
                binding.showInfoButton.setOnClickListener(){
                    val query = "select * from Spettacolo, Rappresentazione where id_spettacolo=ref_spettacolo and id_spettacolo=${id};"
                    ApiService.retrofit.select(query).enqueue(
                        object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                                        val userJsonObject = (response.body()?.get("queryset") as JsonArray)[0] as JsonObject
                                        userId = userJsonObject.get("id_utente").asInt // Assegna l'ID dell'utente alla variabile userId
                                        val userName = userJsonObject.get("nome_utente").asString
                                        val pw = userJsonObject.get("password").asString
                                        val  email = userJsonObject.get("mail").asString
                                        this@MainActivity.saveUserData(userId, userName, email, pw)
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
                }*/
                id = id+"BIGLIETTO"
            }
        }
        generateQRCode(id)
        return binding.root
    }

    private fun generateQRCode(data: String) {
        try {
            // Setting dei parametri
            val hints = Hashtable<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.MARGIN] = 2

            // Crea il codice QR ed effettua l'encode dei dati
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 512, 512, hints)

            // Conversione dalla matrice di bit a una bitmap
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            image.setImageBitmap(bmp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}