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
import androidx.fragment.app.setFragmentResultListener
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

class Ticket(): Fragment() {
    private lateinit var binding: FragmentTicketBinding
    private lateinit var button: Button
    private lateinit var text: TextView
    private lateinit var image: ImageView
    private var isAbbonamento: Boolean = false
    private lateinit var id: String
    private lateinit var  teatro: String

    constructor(isAbbonamento: Boolean, id: String, teatro: String): this(){
        this.isAbbonamento=isAbbonamento
        this.id = id
        this.teatro = teatro
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(this.isAdded) {
            outState.putBoolean("isAbbonamento", isAbbonamento)
            outState.putString("id", id)
            outState.putString("teatro", teatro)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            this.isAbbonamento = savedInstanceState.getBoolean("isAbbonamento")
            this.id = savedInstanceState.getString("id").toString()
            this.teatro = savedInstanceState.getString("teatro").toString()
        }

        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTicketBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        if(MA.getIsOffline() && !isAbbonamento){
            binding.showInfoButton.visibility=View.GONE
        }
        MA.changeTitle("Il mio acquisto")
        image = binding.imageView
        if (isAbbonamento) {
            binding.showInfoButton.setText(R.string.infoMembership)
            MA.changeTitle("Il mio abbonamento")
            // modifica della stringa dell'id per aggiungere varietà in modo tale da garantire l'unicità del codice QR
            id = id + "ABBONAMENTO"
            binding.showInfoButton.setOnClickListener() {
                MA.realAppNavigateTo(TheatreInfo(teatro, false), "TheatreInfo")
            }
        } else {
            if (!isAbbonamento) {
                binding.showInfoButton.setText(R.string.infoTicket)
                MA.changeTitle("Il mio biglietto")
                // modifica della stringa dell'id per aggiungere varietà in modo tale da garantire l'unicità del codice QR
                id = id + "BIGLIETTO"
                binding.showInfoButton.setOnClickListener() {
                    MA.realAppNavigateTo(TheatreInfo(teatro, false), "TheatreInfo")

                }
            }
        }
        generateQRCode(id)
        return binding.root
    }

    /**
     * Questa funzione genera il codice qr da mostrare all'utente
     * @param data stringa che viene trattata per creare il codice QR
     */
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