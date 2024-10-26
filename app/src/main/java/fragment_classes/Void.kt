package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentRegisterBinding
import com.example.progettouni.databinding.FragmentVoidBinding

class Void: Fragment(R.layout.fragment_void) {
    private lateinit var binding: FragmentVoidBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVoidBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        return binding.root
    }
}