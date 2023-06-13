package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentLoginBinding
import com.google.gson.JsonObject
import okhttp3.Response
import javax.security.auth.callback.Callback


class Login : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var userApi: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!

        binding.cancelButton.setOnClickListener {
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()

        }
        binding.confirmButton.setOnClickListener {
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.loginCheck(binding.mailField.text.toString(), binding.pwField.text.toString())

        }
        binding.registerButton.setOnClickListener {
            binding.registerButton.setBackgroundColor(Color.parseColor("#F44336"))
            println("registrati")
        }
        return binding.root
    }


}