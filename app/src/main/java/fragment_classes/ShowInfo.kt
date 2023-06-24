package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentShowInfoBinding

class ShowInfo (val id: String): Fragment(R.layout.fragment_show_info) {
    private lateinit var binding: FragmentShowInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowInfoBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        binding.buyTicketButton.setOnClickListener{
            binding.buyTicketButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(TicketPurchase(), "TicketPurchase")
        }



        return binding.root
    }
}