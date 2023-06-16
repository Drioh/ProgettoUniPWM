package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentLoginBinding
import com.example.progettouni.databinding.FragmentShowsBinding

class Shows : Fragment(R.layout.fragment_shows) {
    private lateinit var binding: FragmentShowsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        TODO("Implementare la pagina degli spettacoli")
        binding.button2.setOnClickListener{
            binding.button2.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(ShowInfo(),"null")
        }

        return binding.root
    }
}