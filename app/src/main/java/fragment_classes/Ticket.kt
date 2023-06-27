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
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentTicketBinding
import java.util.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

class Ticket(private val isAbbonamento: Boolean, id: String): Fragment(R.layout.fragment_ticket) {
    private lateinit var binding: FragmentTicketBinding
    private lateinit var button: Button
    private lateinit var text: TextView
    private lateinit var image: ImageView
    private var id = id
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTicketBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        if(isAbbonamento){
            binding.showInfoButton.setText(R.string.infoMembership)
            binding.textView.setText(R.string.myMembership)

            id = id+"ABBONAMENTO"
        }
        else{
            if(!isAbbonamento){
                binding.showInfoButton.setText(R.string.infoTicket)
                binding.textView.setText(R.string.myTicket)
                id = id+"BIGLIETTO"
            }
        }
        image = binding.imageView
        generateQRCode(id)
        return binding.root
    }

    private fun generateQRCode(data: String) {
        try {
            // Set QR code parameters
            val hints = Hashtable<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.MARGIN] = 2

            // Create QR code writer and encode the data
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 512, 512, hints)

            // Convert bit matrix to bitmap
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