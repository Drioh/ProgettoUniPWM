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
import java.time.LocalDate
import java.time.Period

class SubscriptionPurchase(var theatre: String) : Fragment(R.layout.fragment_subscription_purchase) {
    private lateinit var binding: FragmentSubscriptionPurchaseBinding
    private var selectedbutton: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubscriptionPurchaseBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
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
            if(selectedbutton!=null) {
                var currentDate = LocalDate.now()
                var subLength = Period.of(0, period, 0)
                var expireDate = currentDate.plus(subLength)
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