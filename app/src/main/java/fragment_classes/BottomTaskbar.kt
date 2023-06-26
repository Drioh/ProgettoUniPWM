package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentBottomTaskbarBinding
import com.example.progettouni.databinding.FragmentLoginBinding
import com.example.progettouni.databinding.RealAppBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomTaskbar : Fragment(R.layout.fragment_bottom_taskbar) {
    private lateinit var binding: FragmentBottomTaskbarBinding
    private lateinit var last_fragment : Fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomTaskbarBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla main Activity

        /*
        A seconda del bottone premuto si potrà navigare sulle pagine home, spettacoli, biglietti e
        acquisto abbonamenti.
         */

        binding.HomeButton.setOnClickListener(){
            MA.realAppNavigateTo(Home(), "Home")
        }
        binding.SearchButton.setOnClickListener(){
            val query = "select  nome_spettacolo, data, nome_teatro, id_spettacolo " +
                    "from Rappresentazione , Spettacolo , Teatro " +
                    "where id_spettacolo=ref_spettacolo and id_teatro = ref_teatro;"
            ApiService.retrofit.select(query).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                        println(response.code())
                        if (response.isSuccessful) {
                            val showsJsonObject = response.body()?.get("queryset") as JsonArray
                            var data: ArrayList<ShowModel> = arrayListOf<ShowModel>()
                            println(showsJsonObject)
                            for (i in 0 .. showsJsonObject.size()-1){
                                val spettacolo = showsJsonObject[i].asJsonObject.get("nome_spettacolo").toString().substring(1,showsJsonObject[i].asJsonObject.get("nome_spettacolo").toString().length-1)
                                val date = showsJsonObject[i].asJsonObject.get("data").toString().substring(1,showsJsonObject[i].asJsonObject.get("data").toString().length-1)
                                val teatro = showsJsonObject[i].asJsonObject.get("nome_teatro").toString().substring(1,showsJsonObject[i].asJsonObject.get("nome_teatro").toString().length-1)
                                val identificativo = showsJsonObject[i].asJsonObject.get("id_spettacolo").toString()
                                data.add(ShowModel(0,spettacolo,date,identificativo,teatro))
                            }
                            MA.realAppNavigateTo(Shows(data),"Shows")
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
        }
        binding.TicketButton.setOnClickListener(){
            MA.realAppNavigateTo(PosessedTickets(),"PosessedTickets")
        }
        binding.SubButton.setOnClickListener(){
            MA.realAppNavigateTo(SubscriptionChoice(),"SubscriptionChoice")
        }

        return binding.root

    }
}