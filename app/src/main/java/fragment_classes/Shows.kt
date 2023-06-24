package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentLoginBinding
import com.example.progettouni.databinding.FragmentShowsBinding

class Shows (val data: ArrayList<ShowModel>) : Fragment(R.layout.fragment_shows) {
    private lateinit var binding: FragmentShowsBinding
    private lateinit var name: String
    private lateinit var date: String
    private lateinit var id: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        binding.showsRecycler.layoutManager = LinearLayoutManager(this.context)

        for (i in 1..20) {     //dovrei fare in modo di fare un while per scorrermi tutte le tuple del dbms
            name = "spettacolo" +i
            date = "3 mesi"
            id = "show "+i
            data.add(ShowModel(R.drawable.teatro_placeholder, name, date,id))     //type e period sono i valori che dovrebbero essere presi dal dbms
        }
        val adapter = ShowAdapter(data)                          //importante creare l'adapter dopo gli add sennò viene passato un ArrayList vuoto
        binding.showsRecycler.adapter = adapter

        adapter.setOnClickListener(object: ShowAdapter.OnClickListener {
            override fun onClick(position: Int, model: ShowModel) {
            MA.realAppNavigateTo(ShowInfo(model.id),"ShowInfo")
            }
        })
        return binding.root
    }
}