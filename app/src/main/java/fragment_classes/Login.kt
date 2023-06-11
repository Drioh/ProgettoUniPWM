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

class Login : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!


        binding.button2.setOnClickListener{
            binding.button2.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()

        }
        binding.button3.setOnClickListener{
            binding.button3.setBackgroundColor(Color.parseColor("#F44336"))
            MA.loginCheck(binding.mailfieldId.text.toString(),binding.pwfieldId.text.toString())

        }
        binding.button9.setOnClickListener {
            binding.button9.setBackgroundColor(Color.parseColor("#F44336"))
            println("registrati")
        }
        return binding.root
    }


}
