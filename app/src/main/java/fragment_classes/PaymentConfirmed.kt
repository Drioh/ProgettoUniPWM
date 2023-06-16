package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentBuyTicketsBinding
import com.example.progettouni.databinding.FragmentConfirmedPaymentBinding

class PaymentConfirmed : Fragment(R.layout.fragment_confirmed_payment) {
    private lateinit var binding: FragmentConfirmedPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmedPaymentBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        binding.button.setOnClickListener(){
            binding.button.setBackgroundColor(Color.parseColor("#F44336"))
            MA.backTo("Home")
        }


        return binding.root
    }
}