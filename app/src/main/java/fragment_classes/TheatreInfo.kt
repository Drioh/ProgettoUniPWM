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
import com.example.progettouni.databinding.FragmentTheatreInfoBinding

class TheatreInfo : Fragment(R.layout.fragment_show_info) {
    private lateinit var binding: FragmentTheatreInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTheatreInfoBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity



        return binding.root
    }
}