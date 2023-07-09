package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentEditMailBinding
import com.example.progettouni.databinding.FragmentEditPasswordBinding
import com.example.progettouni.databinding.FragmentRetrievePasswordBinding
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class EditMail: Fragment(){
    private lateinit var binding: FragmentEditMailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditMailBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Modifica")

        binding.confirmButton.setOnClickListener(){
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            /*viene effettutato un test sulla mail, se la vecchia mail corrisponde con quella
            presente nel database allora verrà permesso all'utente di modificarla*/

            changeEmail(binding.cardField.text.toString(),binding.cardOwnerField.text.toString())
            MA.showToast("Mail modificata correttamente")
            MA.back()
        }
        binding.cancelButton.setOnClickListener(){
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()
        }

        return binding.root
    }


    fun changeEmail(oldEmail: String, newEmail: String) {
        val query = "SELECT * FROM Utente WHERE mail = '${oldEmail}';"
        ApiService.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonArray = response.body()?.getAsJsonArray("queryset")
                    if (jsonArray?.size() == 1) {
                        val utenteId = jsonArray[0].asJsonObject["id_utente"].asInt
                        updateEmailInDatabase(utenteId, newEmail)
                    } else {
                        Log.i("ApiService","Vecchia email non valida")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("Errore di rete",t.message.toString())
            }
        })
    }

    fun updateEmailInDatabase(utenteId: Int?, newEmail: String) {
        if (utenteId != null) {
            val query = "UPDATE Utente SET mail = '${newEmail}' WHERE id_utente = ${utenteId};"
            ApiService.retrofit.update(query).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.i("ApiService","Email modificata correttamente")
                    } else {
                        Log.i("ApiService","Errore durante la modifica dell'email")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Errore di rete", t.message.toString())
                }
            })
        }
    }



}