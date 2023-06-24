package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentPosessedTicketsBinding

class PosessedTickets : Fragment(R.layout.fragment_posessed_tickets) {
    private lateinit var binding: FragmentPosessedTicketsBinding
    private lateinit var type: String
    private lateinit var period: String
    private lateinit var id_ticket: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): LinearLayout {
        binding = FragmentPosessedTicketsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        binding.ticketRecycler.layoutManager = LinearLayoutManager(this.context)
        val data = ArrayList<TicketModel>()
        val essence = ArrayList<Boolean>()     //mi indica se lavoro con un biglietto singolo o un abbonamento
        val allId = ArrayList<String>()    //mi crea una lista di id
        //for (i in 1..20) {     //dovrei fare in modo di fare un while per scorrermi tutte le tuple del dbms
        type = "abbonamento"
        period = "6 mesi"
        id_ticket = "xlcsbm"    //devo prendermi dal database anche id del biglietto/abbonamento
        data.add(TicketModel(R.drawable.ticket_icon_white, type, period))
        if("abbonamento" == type){
            essence.add(true)                           //se il biglietto cliccato è un abbonamento allora essence è true
            id_ticket = "abbonamento$id_ticket"
        } else {essence.add(false)}
        allId.add(id_ticket)

        type = "Orlando Innamorato"
        period = "3 mesi"
        id_ticket = "jtrnnyj"
        data.add(TicketModel(R.drawable.ticket_icon_white, type, period))     //type e period sono i valori che dovrebbero essere presi dal dbms
        if("abbonamento" == type){
            essence.add(true)
            id_ticket = "abbonamento$id_ticket"
        } else {essence.add(false)}
        allId.add(id_ticket)

        //}
        val adapter = TicketAdapter(data)                          //importante creare l'adapter dopo gli add sennò viene passato un ArrayList vuoto
        binding.ticketRecycler.adapter = adapter

        adapter.setOnClickListener(object: TicketAdapter.OnClickListener {
            override fun onClick(position: Int, model: TicketModel) {
                MA.realAppNavigateTo(Ticket(essence[position], allId[position]), "ticket")
            }
        })
        return binding.root
    }
}