package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.ActivityMainBinding
import com.example.progettouni.databinding.FragmentPosessedTicketsBinding
import com.example.progettouni.databinding.RealAppBinding

class RealApp: Fragment() {
    private lateinit var binding: RealAppBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RealAppBinding.inflate(inflater)
        println("creata Realapp")
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        return binding.root
    }
}
