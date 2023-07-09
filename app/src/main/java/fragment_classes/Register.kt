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
class Register : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    var MA = (activity as MainActivity?)!!//reference alla Main Activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)



        binding.confirmButton.setOnClickListener {
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))

            val nome = binding.nameField.text.toString()
            val cognome = binding.surnameField.text.toString()
            val mail = binding.mailField.text.toString()
            val password = binding.pwField.text.toString()
           if(controllaValiditaPwd(password)) {

               RegistrazioneDB(nome, cognome, mail, password)
           }
            else{
                MA.showToast("La password deve avere almeno 8 caratteri, contenere almeno una lettera maiuscola, una lettera minuscola e un numero.")
           }
        }
        binding.cancelButton.setOnClickListener(){
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()
        }
        return binding.root
    }


    fun RegistrazioneDB(nome: String, cognome: String, mail: String, password: String){
        val f=0 // da implementare verifica account
        val propicPath = binding.propicImage.tag?.toString() // Ottieni il percorso dell'immagine caricata
        val otp = String.format("%06d", (Math.random() * 1000000).toInt())
        val query = "insert into Utente (mail, nome_utente, cognome , password, propic , cod_ver, verificato ) values ('${mail}', '${nome}', '${cognome}', '${password}','${propicPath}','${otp}','${f}'); "


       ApiService.retrofit.insert(query).enqueue(
            object: Callback <JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println(response.code())
                    Log.i("ApiService", "Registrazione avvenuta correttamente!")
                    MA.showToast("Registrazione avvenuta correttamente!")
                    MA.navigateTo(VerificationCode(otp),"VerShow")

                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("ApiService", "Registration fallita")
                    Log.e("ApiService", t.message.toString())
                    MA.showToast("Registrazione fallita")
                }
            }
        )
    }
    /**
     *  Verifica la validità della password dell'utente, la password deve avere 8 caratteri, contenere una maiuscola e un numero.
     *
     *  @param password La password dell'utente da controllare.
     *  @return True se la password è valida, altrimenti False.
     */
    fun controllaValiditaPwd(password: String): Boolean {
        var isValid = password.length <= 20 && password.length >= 8
        val upperCaseChars = "(.*[A-Z].*)".toRegex()
        if (!password.matches(upperCaseChars)) isValid = false
        val lowerCaseChars = "(.*[a-z].*)".toRegex()
        if (!password.matches(lowerCaseChars)) isValid = false
        val numbers = "(.*[0-9].*)".toRegex()
        if (!password.matches(numbers)) isValid = false
        return isValid
    }
}