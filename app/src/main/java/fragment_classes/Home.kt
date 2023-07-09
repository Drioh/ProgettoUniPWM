package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentEditPasswordBinding
import com.example.progettouni.databinding.FragmentHomeBinding

class Home: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Home")
        binding.SearchText.visibility=View.GONE
        binding.TicketText.visibility=View.GONE
        binding.SubText.visibility=View.GONE
        binding.textView9.text=("Bentornato ${MA.getSharedPreferences().getString("userName", "")} ${MA.getSharedPreferences().getString("surname", "")}")

        binding.SearchView.setOnClickListener(){
            if (binding.SearchText.visibility!=View.VISIBLE)
            binding.SearchText.visibility=View.VISIBLE
            else{
                binding.SearchText.visibility=View.GONE
            }
        }
        binding.searchButton.setOnClickListener{
            MA.realAppNavigateTo(Shows(),"Shows")
        }
        binding.TicketView.setOnClickListener(){
            if (binding.TicketText.visibility!=View.VISIBLE)
                binding.TicketText.visibility=View.VISIBLE
            else{
                binding.TicketText.visibility=View.GONE
            }

        }
        binding.TicketButton.setOnClickListener{
            MA.realAppNavigateTo(PosessedTickets(),"PosessedTickets")
        }
        binding.SubView.setOnClickListener(){
            if (binding.SubText.visibility!=View.VISIBLE)
                binding.SubText.visibility=View.VISIBLE
            else{
                binding.SubText.visibility=View.GONE
            }

        }
        binding.subButton.setOnClickListener{
            MA.realAppNavigateTo(SubscriptionChoice(),"SubscriptionChoice")
        }

        return binding.root
    }
}