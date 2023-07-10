package fragment_classes

import android.annotation.SuppressLint
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
import java.time.LocalDate

class PosessedTickets : Fragment() {
    private lateinit var binding: FragmentPosessedTicketsBinding
    private lateinit var type: String
    private lateinit var period: String
    private lateinit var id_ticket: String
    private lateinit var dbManager: DBManager

    @SuppressLint("Range")
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

        val cursorBiglietti = dbManager.fetchAllBiglietti()

        do {
            if(LocalDate.now().equals(LocalDate.parse((cursorBiglietti.getString(cursorBiglietti.getColumnIndex(DBHelper.DATA_SCADENZA)))))){

            }
        }while (cursorBiglietti.moveToNext())

        val cursorAbbonamenti = dbManager.fetchAllAbbonamenti()
        val adapter = TicketAdapter(cursorBiglietti,cursorAbbonamenti,MA)
        binding.ticketRecycler.adapter = adapter    //importante creare l'adapter dopo gli add sennò viene passato un ArrayList vuoto

        adapter.setOnClickListener(object: TicketAdapter.OnClickListener {
            override fun onClick(position: Int, model: TicketModel) {
                MA.realAppNavigateTo(Ticket(model.isAbbonamento,model.id,model.teatro),"TicketInfo")
            }
        })
        return binding.root
    }


}