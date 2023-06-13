package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentShowsBinding
import com.example.progettouni.databinding.FragmentSubscriptionChoiceBinding

class SubscriptionChoice : Fragment(R.layout.fragment_subscription_choice) {
    private lateinit var binding: FragmentSubscriptionChoiceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubscriptionChoiceBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!

        binding.biondoButton.setOnClickListener{
            MA.realAppNavigateTo(ShowInfo(),"Biondo")
        }
        binding.massimoButton.setOnClickListener{
            MA.realAppNavigateTo(ShowInfo(),"Massimo")
        }
        binding.politeamaButton.setOnClickListener{
            MA.realAppNavigateTo(ShowInfo(),"Politeama")
        }

        return binding.root
    }
}