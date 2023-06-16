package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentBuyTicketsBinding
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding

class TicketPurchase : Fragment(R.layout.fragment_buy_tickets) {
    private lateinit var binding: FragmentBuyTicketsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyTicketsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        binding.confirmButton.setOnClickListener{
            TODO("Effettuare la query per la carta")
            var isCardValid = binding.cardField.text.length==16
            var isCVCValid = binding.cvcField.text.length==3
            if (isCardValid && isCVCValid) {
                binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
                MA.realAppNavigateTo(PaymentConfirmed(),"ConfirmedPayment")
                TODO("Generare il QR code")
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
}