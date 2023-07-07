package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding

class SubscriptionPurchase(teatro: String) : Fragment(R.layout.fragment_subscription_purchase) {
    private lateinit var binding: FragmentSubscriptionPurchaseBinding
    private var selectedbutton: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubscriptionPurchaseBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        binding.oneMonthButton.setOnClickListener{
            selectButton(binding.oneMonthButton)
        }
        binding.threeMonthButton.setOnClickListener{
            selectButton(binding.threeMonthButton)
        }
        binding.sixMonthButton.setOnClickListener{
            selectButton(binding.sixMonthButton)
        }
        binding.oneYearButton.setOnClickListener{
            selectButton(binding.oneYearButton)
        }
        binding.confirmButton.setOnClickListener(){
            if(selectedbutton!=null) {
                MA.realAppNavigateTo(PaymentConfirmed("Abbonamento"), "ConfirmedPayment")
            }else{
                MA.showToast("Selezionare prima un periodo di validità")
            }
        }
        binding.cancelButton.setOnClickListener(){
            MA.backTo("SubscriptionChoice")
        }

        return binding.root
    }
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
}