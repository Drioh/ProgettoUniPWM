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
import com.example.progettouni.databinding.FragmentShowsBinding

class ShowInfo : Fragment(R.layout.fragment_show_info) {
    private lateinit var binding: FragmentShowInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowInfoBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!

        binding.buyTicketButton.setOnClickListener{
            binding.buyTicketButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.backTo("Home")
        }

        return binding.root
    }
}