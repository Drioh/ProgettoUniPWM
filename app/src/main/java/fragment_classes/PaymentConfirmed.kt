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

class PaymentConfirmed () : Fragment() {
    private lateinit var binding: FragmentConfirmedPaymentBinding
    private lateinit var type : String
    constructor(type: String): this(){
        this.type = type
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("type",type)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState!=null){
            this.type = savedInstanceState.getString("type").toString()
        }
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentConfirmedPaymentBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Pagamento Confermato")
        when(type){
            "Biglietto"->{
                binding.textView6.setText(R.string.CorrPay)
            }
            "Abbonamento"->{
                binding.textView6.setText(R.string.CorrSubPay)

            }
            else->{
                println("errore")
            }
        }
        binding.button.setOnClickListener(){
            binding.button.setBackgroundColor(Color.parseColor("#F44336"))
            MA.backTo("Home")
        }


        return binding.root
    }
}