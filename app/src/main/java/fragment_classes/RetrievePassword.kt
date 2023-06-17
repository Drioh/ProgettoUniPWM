package fragment_classes

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
import com.example.progettouni.databinding.FragmentRetrievePasswordBinding
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class RetrievePassword: Fragment(R.layout.fragment_retrieve_password){
    private lateinit var binding: FragmentRetrievePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRetrievePasswordBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity


        binding.confirmButton.setOnClickListener(){
            MA.back()

            changePassword(binding.cardField.text.toString(),binding.cardOwnerField.text.toString())

        }
        binding.cancelButton.setOnClickListener(){
            MA.back()
        }

        return binding.root
    }



    fun changePassword(mail: String, password: String) {
        val query = "UPDATE Utente SET password = '${password}' WHERE mail = '${mail}';"

        ApiService.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful) {
                        Log.i("ApiService", "Success")

                    } else {
                        println(response.code())
                        Log.e("ApiService", "Failed")
                        Log.e("ApiService", response.message().toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("ApiService", t.message.toString())
                }
            }
        )
    }
}