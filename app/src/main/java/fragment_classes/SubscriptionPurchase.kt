package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentSubscriptionChoiceBinding
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding

class SubscriptionPurchase(teatro: String) : Fragment(R.layout.fragment_subscription_purchase) {
    private lateinit var binding: FragmentSubscriptionPurchaseBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubscriptionPurchaseBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!

        binding.oneMonthButton.setOnClickListener{
            MA.realAppNavigateTo(ShowInfo(),"Biondo")
        }
        binding.threeMonthButton.setOnClickListener{
            MA.realAppNavigateTo(ShowInfo(),"Massimo")
        }
        binding.sixMonthButton.setOnClickListener{
            MA.realAppNavigateTo(ShowInfo(),"Politeama")
        }
        binding.oneYearMonth.setOnClickListener{
            MA.realAppNavigateTo(ShowInfo(),"Politeama")
        }

        return binding.root
    }
}