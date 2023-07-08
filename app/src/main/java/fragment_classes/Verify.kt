package fragment_classes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentRegisterBinding
import com.example.progettouni.databinding.FragmentVerifyBinding
import com.example.progettouni.databinding.FragmentVoidBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Verify: Fragment(R.layout.fragment_verify) {
    private lateinit var binding: FragmentVerifyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity


        binding.confirm.setOnClickListener{
            val code = binding.codeText.text.toString()
            val id = MA.getUserId().toString()
            println("utente: "+id+", codice: "+code)
            val query = "SELECT * FROM Utente WHERE id_utente = '${id}' and cod_ver = '${code}';"
            ApiService.retrofit.select(query).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonArray = response.body()?.getAsJsonArray("queryset")
                        if (jsonArray?.size() == 1) {
                            verifyUser(id)
                            MA.back()

                        } else {
                            Log.i("ApiService","n/a")
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Errore di rete",t.message.toString())
                }
            })
        }
        return binding.root
    }

    fun verifyUser(id:String){
        val query = "UPDATE Utente SET verificato = 1 WHERE id_utente = ${id};"
        ApiService.retrofit.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i("ApiService","verifica inserita nel database correttamente")
                } else {
                    Log.i("ApiService","verifica non inserita nel database correttamente")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("Errore di rete", t.message.toString())
            }
        })
    }
}