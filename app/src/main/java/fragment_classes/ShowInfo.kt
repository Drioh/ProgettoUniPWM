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

class ShowInfo(): Fragment(R.layout.fragment_show_info) {
    private lateinit var binding: FragmentShowInfoBinding

    lateinit var id: String
    lateinit var name: String
    lateinit var date: String
    lateinit var textTheatre: String
    constructor(id: String, name: String,  date: String, textTheatre: String): this(){
        this.id=id
        this.name = name
        this.date = date
        this.textTheatre = textTheatre
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(this.isAdded) {
            outState.putString("id", id)
            outState.putString("name", name)
            outState.putString("date", date)
            outState.putString("textTheatre", textTheatre)
        }
        super.onSaveInstanceState(outState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState!=null){
            this.id=savedInstanceState.getString("id").toString()
            this.name = savedInstanceState.getString("name").toString()
            this.date = savedInstanceState.getString("date").toString()
            this.textTheatre = savedInstanceState.getString("textTheatre").toString()

        }
        super.onCreateView(inflater, container, savedInstanceState)
        println("Teatro: ${textTheatre}")
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

                        if (show.get("tipologia").asString == "Teatro"){
                            binding.directorHeading.text = "Regista"
                            binding.FirstHeading.text = "Protagonista"
                            binding.CompanyHeading.text = "Compagnia"
                        }else if (show.get("tipologia").asString == "Concerto"){
                            binding.directorHeading.text = "Direttore"
                            binding.FirstHeading.text = "Primo Strumento"
                            binding.CompanyHeading.text = "Orchestra"
                        }

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
        binding.theatreButton.setOnClickListener{
            MA.realAppNavigateTo(TheatreInfo(textTheatre.substring(7),false),"Theatre Info")
        }
        binding.buyTicketButton.setOnClickListener{
            binding.buyTicketButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(ChoosePlace(id, name, date, textTheatre), "ChoosePlace") //da mettere qui o dopo il navigate per ChoosePlace
        }



        return binding.root
    }

}