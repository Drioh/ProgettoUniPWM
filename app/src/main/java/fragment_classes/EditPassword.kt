package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentEditPasswordBinding
import com.example.progettouni.databinding.FragmentRetrievePasswordBinding
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPassword: Fragment(R.layout.fragment_retrieve_password){
    private lateinit var binding: FragmentEditPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPasswordBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        binding.confirmButton.setOnClickListener(){
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            /*viene effettutato un test sulla mail, se la mail corrisponde con quella nel database
            allora verrà permesso all'utente di modificare la password*/
            changePassword(binding.cardField.text.toString(), binding.cardOwnerField.text.toString())
            MA.showToast("Password modificata correttamente")
            MA.back()
        }
        binding.cancelButton.setOnClickListener(){
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()
        }

        return binding.root
    }


    fun changePassword(password: String, newPassword: String) {
        val query = "SELECT * FROM Utente WHERE password = '${password}';"
        ApiService.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonArray = response.body()?.getAsJsonArray("queryset")
                    if (jsonArray?.size() == 1) {
                        val utenteId = jsonArray[0].asJsonObject["id_utente"].asInt
                        updatePasswordInDatabase(utenteId, newPassword)
                    } else {
                        Log.i("ApiService"," password non valida")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("Errore di rete",t.message.toString())
            }
        })
    }
    fun updatePasswordInDatabase(utenteId: Int?, newPassword: String) {
        if (utenteId != null) {
            val query = "UPDATE Utente SET password = '${newPassword}' WHERE id_utente = ${utenteId};"
            ApiService.retrofit.update(query).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.i("ApiService","Password modificata correttamente")
                    } else {
                        Log.i("ApiService","Errore durante la modifica della password")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Errore di rete", t.message.toString())
                }
            })
        }
    }

}