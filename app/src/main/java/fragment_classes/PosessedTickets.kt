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
    private lateinit var dbManager: DBManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): LinearLayout {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPosessedTicketsBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("I miei acquisti")
        binding.ticketRecycler.layoutManager = LinearLayoutManager(context)
        dbManager = DBManager(requireContext())
        dbManager.open()
        //dbManager.insertAbbonamento(1,"Massimo", "11/12/2001", "05/05/2002")
        //dbManager.insertBiglietto(1,"Pinocchio", "02/02/2010")
        //dbManager.insertAbbonamento(2,"Politeama", "11/11/2011", "04/04/2022")
        //dbManager.insertBiglietto(2,"Massimo Boldi - Il film", "20/08/2021")
        //dbManager.insertAbbonamento(3,"Biondo", "09/09/2009", "07/07/2012")
        //dbManager.insertBiglietto(3,"Truce Baldazzi Live Show", "10/06/2023")

        val cursorBiglietti = dbManager.fetchAllBiglietti()
        val cursorAbbonamenti = dbManager.fetchAllAbbonamenti()
        println(cursorBiglietti.count+cursorAbbonamenti.count)
        val adapter = TicketAdapter(cursorBiglietti,cursorAbbonamenti)
        binding.ticketRecycler.adapter = adapter    //importante creare l'adapter dopo gli add sennò viene passato un ArrayList vuoto


        //queryMembership(data, essence, allId)
        //queryTicket(data, essence, allId)


        adapter.setOnClickListener(object: TicketAdapter.OnClickListener {
            override fun onClick(position: Int, model: TicketModel) {
                MA.realAppNavigateTo(Ticket(model.isAbbonamento,model.id,model.teatro),"TicketInfo")
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