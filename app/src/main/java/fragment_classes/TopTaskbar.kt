package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentBottomTaskbarBinding
import com.example.progettouni.databinding.FragmentTopTaskbarBinding

class TopTaskbar : Fragment(R.layout.fragment_top_taskbar) {
    private lateinit var binding: FragmentTopTaskbarBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopTaskbarBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!

        binding.backButton.setOnClickListener(){
            MA.onBackPressed()
        }
        binding.profileButton.setOnClickListener(){

            MA.realAppNavigateTo(Shows(),"Show")
        }

        return binding.root
    }
}