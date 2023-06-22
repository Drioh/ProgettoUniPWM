package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentTicketCardBinding

class TicketCard: Fragment(R.layout.fragment_ticket_card) {
    private lateinit var binding: FragmentTicketCardBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketCardBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        return binding.root
    }
}