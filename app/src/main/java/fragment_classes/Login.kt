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

        binding.cancelButton.setOnClickListener{
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()

        }
        binding.confirmButton.setOnClickListener{
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.loginCheck(binding.mailField.text.toString(),binding.pwField.text.toString())

        }
        binding.registerButton.setOnClickListener {
            binding.registerButton.setBackgroundColor(Color.parseColor("#F44336"))
            println("registrati")
        }
        return binding.root
    }


}
