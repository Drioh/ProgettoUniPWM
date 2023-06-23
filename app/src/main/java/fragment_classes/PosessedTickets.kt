package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentHomeBinding
import com.example.progettouni.databinding.FragmentPosessedTicketsBinding
import com.example.progettouni.databinding.RealAppBinding

class PosessedTickets : Fragment(R.layout.fragment_posessed_tickets) {
    private lateinit var binding: FragmentPosessedTicketsBinding
    private lateinit var type: String
    private lateinit var period: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): LinearLayout {
        binding = FragmentPosessedTicketsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        binding.ticketRecycler.layoutManager = LinearLayoutManager(this.context)
        val data = ArrayList<TicketModel>()
        //for (i in 1..20) {     //dovrei fare in modo di fare un while per scorrermi tutte le tuple del dbms
        type = "spettacolo"
        period = "3 mesi"
        data.add(TicketModel(R.drawable.ticket_icon_white, type, period))     //type e period sono i valori che dovrebbero essere presi dal dbms
        type = "abbonamento"
        period = "6 mesi"
        data.add(TicketModel(R.drawable.ticket_icon_white, type, period))
        //}
        val adapter = TicketAdapter(data)                          //importante creare l'adapter dopo gli add sennò viene passato un ArrayList vuoto
        binding.ticketRecycler.adapter = adapter

        adapter.setOnClickListener(object: TicketAdapter.OnClickListener {
            override fun onClick(position: Int, model: TicketModel) {
                TODO("inserire schermata biglietto")
            }
        })
        return binding.root
    }
}