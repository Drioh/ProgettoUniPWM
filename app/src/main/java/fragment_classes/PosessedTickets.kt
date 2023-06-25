package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import api.ApiService
import api.DBHelper
import api.DBManager
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentPosessedTicketsBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.database.Cursor

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
        val data : Cursor
        val essence = ArrayList<Boolean>()     //booleani utili a capire se l'oggetto nel posto i-esimo è un abbonamento o un biglietto
        val allId = ArrayList<String>()        //mi crea una lista di id

        queryMembership(data, essence, allId)
        queryTicket(data, essence, allId)

        val adapter = TicketAdapter(data)            //importante creare l'adapter dopo gli add sennò viene passato un ArrayList vuoto
        binding.ticketRecycler.adapter = adapter

        adapter.setOnClickListener(object: TicketAdapter.OnClickListener {
            override fun onClick(position: Int, model: TicketModel) {
                MA.realAppNavigateTo(Ticket(essence[position], allId[position]), "ticket")
            }
        })
        return binding.root
    }

    private fun queryTicket(data: Cursor, essence: ArrayList<Boolean>, allId: ArrayList<String>) {
        //la funzione serve a popolare i 3 arraylist tramite delle query al dbms locale


    }

    private fun queryMembership(data: Cursor, essence: ArrayList<Boolean>, allId: ArrayList<String>) {
        //la funzione serve a popolare i 3 arraylist tramite delle query al dbms locale



    }
}