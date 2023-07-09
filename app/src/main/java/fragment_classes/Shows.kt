package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentShowsBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class Shows: Fragment() {
    private lateinit var binding: FragmentShowsBinding
    private lateinit var name: String
    private lateinit var date: String
    private lateinit var id: String
    private lateinit var theatre: String
    private lateinit var data : ArrayList<ShowModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentShowsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Spettacoli")

        binding.showsRecycler.layoutManager = LinearLayoutManager(this.context)

        val query = "select  nome_spettacolo, data, nome_teatro, id_spettacolo, foto_spettacolo " +
                "from Rappresentazione , Spettacolo , Teatro " +
                "where id_spettacolo=ref_spettacolo and id_teatro = ref_teatro and data > '${LocalDate.now()}';"
        ApiService.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    println(response.code())
                    if (response.isSuccessful) {
                        val showsJsonObject = response.body()?.get("queryset") as JsonArray
                        var data: ArrayList<ShowModel> = arrayListOf<ShowModel>()
                        println(showsJsonObject) //DEBUG
                        for (i in 0 .. showsJsonObject.size()-1){
                            val spettacolo = showsJsonObject[i].asJsonObject.get("nome_spettacolo").toString().substring(1,showsJsonObject[i].asJsonObject.get("nome_spettacolo").toString().length-1)
                            val date = showsJsonObject[i].asJsonObject.get("data").toString().substring(1,showsJsonObject[i].asJsonObject.get("data").toString().length-1)
                            val teatro = showsJsonObject[i].asJsonObject.get("nome_teatro").toString().substring(1,showsJsonObject[i].asJsonObject.get("nome_teatro").toString().length-1)
                            val identificativo = showsJsonObject[i].asJsonObject.get("id_spettacolo").toString()
                            val imageURL = showsJsonObject[i].asJsonObject.get("foto_spettacolo").toString().substring(1,showsJsonObject[i].asJsonObject.get("foto_spettacolo").toString().length-1)
                            data.add(
                                ShowModel(
                                    imageURL,
                                    spettacolo,
                                    date,
                                    identificativo,
                                    teatro
                                )
                            )
                            val adapter = ShowAdapter(data)                          //importante creare l'adapter dopo gli add sennò viene passato un ArrayList vuoto
                            binding.showsRecycler.adapter = adapter

                            adapter.setOnClickListener(object: ShowAdapter.OnClickListener {
                                override fun onClick(position: Int, model: ShowModel) {
                                    MA.realAppNavigateTo(ShowInfo(model.id, model.textName, model.textDate, model.textTheatre),"ShowInfo")
                                }
                            })
                        }
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


        return binding.root
    }




}