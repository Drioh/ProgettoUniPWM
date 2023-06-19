package fragment_classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentProfileBinding
import com.example.progettouni.databinding.FragmentRetrievePasswordBinding
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        getUrlbyID(MA.getUserId())

        binding.mailButton.setOnClickListener(){
            binding.mailButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(EditMail(),"EditMail")
        }
        binding.passwordButton.setOnClickListener(){
            binding.passwordButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(EditPassword(),"EditPassword")

        }
        binding.confirmButton.setOnClickListener(){
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            TODO("impostare Nome e Cognome se diversi da quelli salvati in precedenza")
            MA.back()
        }
        binding.cancelButton.setOnClickListener(){
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()
        }

        return binding.root
    }
    fun getUrlbyID(id: Int) {
        val query = "SELECT propic FROM Utente WHERE id_utente = '${id}';"
        ApiService.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonArray = response.body()?.getAsJsonArray("queryset")
                    if (jsonArray?.size() == 1) {
                        val propic = jsonArray[0].asJsonObject["propic"].asString
                        getImageProfilo(propic)

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
    private fun getImageProfilo(url: String){

        ApiService.retrofit.image(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        var image: Bitmap? = null
                        if (response.body()!=null) {
                            image = BitmapFactory.decodeStream(response.body()?.byteStream())
                            binding.propicButton.setImageBitmap(image)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    Log.e("ApiService", t.message.toString())
                }

            }
        )
    }

}