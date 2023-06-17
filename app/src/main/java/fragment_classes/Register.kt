package fragment_classes

import android.graphics.Color
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentPosessedTicketsBinding
import com.example.progettouni.databinding.FragmentRegisterBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class Register : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        binding.propicButton.setOnClickListener(){
            TODO("Capire se implementare o no l'immagine profilo")
        }
        binding.confirmButton.setOnClickListener {
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))

            val nome = binding.nameField.text.toString()
            val cognome = binding.surnameField.text.toString()
            val mail = binding.mailField.text.toString()
            val password = binding.pwField.text.toString()
            RegistrazioneDB(nome,cognome,mail,password)


        }
        binding.cancelButton.setOnClickListener(){
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()
        }
        return binding.root
    }

    //String.format("%06d", (Math.random() * 1000000))
    fun RegistrazioneDB(nome: String, cognome: String, mail: String, password: String ){
        val f=0
        val s="miao"
        val otp = "1234"
        val query = "insert into Utente (mail, nome, cognome , password, propic , cod_ver, verificato ) values ('${mail}', '${nome}', '${cognome}', '${password}','${s}','${otp}','${f}'); "


       ApiService.retrofit.insert(query).enqueue(
            object: Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println(response.body())
                    Log.i("ApiService", "Registration successful!")

                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("ApiService", "Registration failed!")
                    Log.e("ApiService", t.message.toString())

                }
            }
        )
    }

}