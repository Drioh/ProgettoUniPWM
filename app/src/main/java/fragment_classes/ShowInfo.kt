package fragment_classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentShowInfoBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowInfo(var id: String, var name: String, var date: String, var textTheatre: String): Fragment(R.layout.fragment_show_info) {
    private lateinit var binding: FragmentShowInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowInfoBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Informazioni Spettacolo")
        val query = "select  * " +
                "from Rappresentazione , Spettacolo , Teatro " +
                "where id_spettacolo=ref_spettacolo and id_teatro = ref_teatro and id_spettacolo = '${id}';"
        ApiService.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    println(response.code())
                    if (response.isSuccessful) {
                        val show = (response.body()?.get("queryset") as JsonArray)[0] as JsonObject
                        binding.Show.text=show.get("nome_spettacolo").asString
                        binding.directorText.text=show.get("regista_direttore").asString
                        binding.FirstText.text=show.get("protagonista_primo_strumento").asString
                        binding.CompanyText.text=show.get("compagnia_orchestra").asString
                        binding.ShowDesc.text=show.get("Info").asString
                        binding.DateText.text=show.get("data").asString
                        binding.theatreButton.text=show.get("nome_teatro").asString



                        var image: Bitmap? = null
                        ApiService.retrofit.image(show.get("foto_spettacolo").asString).enqueue(
                            object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    if(response.isSuccessful) {
                                        if (response.body()!=null) {
                                            image = BitmapFactory.decodeStream(response.body()?.byteStream())
                                            binding.imageView3.setImageBitmap(image)

                                        }
                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                                    Log.e("ApiService", t.message.toString())
                                }

                            }
                        )



                    }else
                    {
                        println("esito negativo")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    MA.showToast("Errore di rete")
                }
            }
        )
        binding.buyTicketButton.setOnClickListener{
            binding.buyTicketButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(ChoosePlace(id, name, date, textTheatre), "ChoosePlace") //da mettere qui o dopo il navigate per ChoosePlace
        }



        return binding.root
    }
    private fun getImageSpettacolo(url: String, IV: ImageView): Bitmap?{
        var image: Bitmap? = null
        ApiService.retrofit.image(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        if (response.body()!=null) {
                            image = BitmapFactory.decodeStream(response.body()?.byteStream())
                            IV.setImageBitmap(image)

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    Log.e("ApiService", t.message.toString())
                }

            }
        )
        return image
    }
}