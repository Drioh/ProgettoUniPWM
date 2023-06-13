package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentHomeBinding
import com.example.progettouni.databinding.FragmentPosessedTicketsBinding
import com.example.progettouni.databinding.RealAppBinding

class PosessedTickets : Fragment(R.layout.fragment_posessed_tickets) {
    private lateinit var binding: FragmentPosessedTicketsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPosessedTicketsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!

        return binding.root
    }
}