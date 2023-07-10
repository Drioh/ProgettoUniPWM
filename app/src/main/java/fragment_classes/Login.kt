package fragment_classes

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentLoginBinding
import com.google.gson.JsonObject
import retrofit2.Callback


class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var userApi: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentLoginBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity


        if (MA.getIsOffline()) {
            binding.registerButton.visibility = View.GONE
            binding.retrievePasswordButton.visibility = View.GONE
        }

        binding.confirmButton.setOnClickListener {
            if(MA.getIsOffline()){
                MA.showToast("SEI OFFLINE")
            }
            else {
                binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
                //MA.loginCheck(binding.mailField.text.toString(),binding.pwField.text.toString() )
                // Controllo se ci sono credenziali salvate nelle SharedPreferences

                MA.loginCheck(binding.mailField.text.toString(), binding.pwField.text.toString())
            }
        }
        binding.registerButton.setOnClickListener {

            binding.registerButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.navigateTo(Register(),"Register")

        }
        binding.retrievePasswordButton.setOnClickListener {

            binding.retrievePasswordButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.navigateTo(RetrievePassword(),"RetrievePassword")
        }

        return binding.root
    }


}