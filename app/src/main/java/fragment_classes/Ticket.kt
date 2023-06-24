package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentTicketBinding

class Ticket(val essence: ArrayList<Boolean>, position: Int): Fragment(R.layout.fragment_ticket) {
    private lateinit var binding: FragmentTicketBinding
    private lateinit var button: Button
    private var position: Int = position
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        button = binding.showInfoButton

        if(essence.get(position)){
            button.setText("visualizza info abbonamento")
        }


        return binding.root
    }
}