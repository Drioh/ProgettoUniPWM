package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentBottomTaskbarBinding
import com.example.progettouni.databinding.FragmentLoginBinding

class BottomTaskbar : Fragment(R.layout.fragment_bottom_taskbar) {
    private lateinit var binding: FragmentBottomTaskbarBinding
    private lateinit var last_fragment : Fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomTaskbarBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla main Activity

        /*
        A seconda del bottone premuto si potrà navigare sulle pagine home, spettacoli, biglietti e
        acquisto abbonamenti.
         */

        binding.HomeButton.setOnClickListener(){
                MA.realAppNavigateTo(Home(), "Home")
        }
        binding.SearchButton.setOnClickListener(){

                MA.realAppNavigateTo(Shows(),"Show")
        }
        binding.TicketButton.setOnClickListener(){
                MA.realAppNavigateTo(PosessedTickets(),"PosessedTickets")
        }
        binding.SubButton.setOnClickListener(){
                MA.realAppNavigateTo(SubscriptionChoice(),"SubscriptionChoice")
        }

        return binding.root

    }
}