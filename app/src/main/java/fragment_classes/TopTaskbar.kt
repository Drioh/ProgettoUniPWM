package fragment_classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentTopTaskbarBinding
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class TopTaskbar : Fragment(R.layout.fragment_top_taskbar) {
    lateinit var binding: FragmentTopTaskbarBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopTaskbarBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity

        val sharedPreferences = MA.getSharedPreferences()

        val propic = sharedPreferences.getString("propic", "")
        if (!propic.isNullOrEmpty()) {
            val decodedImage = decodeBase64ToBitmap(propic)
            binding.profileButton.setImageBitmap(decodedImage)
        }

        getUrlbyID(MA.getUserId())

        binding.profileButton.setOnClickListener(){

            MA.realAppNavigateTo(Profile(), "Profile")
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
                            binding.profileButton.setImageBitmap(image)
                            //SharedPreferences
                            var MA = (activity as MainActivity?)!!
                            val sharedPreferences = MA.getSharedPreferences()
                            val editor = sharedPreferences.edit()
                            val encodedImage = encodeBitmapToBase64(image)
                            editor.putString("propic", encodedImage)
                            editor.apply()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    Log.e("ApiService", t.message.toString())
                }

            }
        )
    }
    private fun decodeBase64ToBitmap(encodedImage: String): Bitmap {
        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}