package fragment_classes

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api.DBHelper
import com.example.progettouni.databinding.FragmentTicketCardBinding

class TicketAdapter(private val cursorB: Cursor, private val cursorA: Cursor) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    class ViewHolder(binding: FragmentTicketCardBinding) : RecyclerView.ViewHolder(binding.root) {   //creo le caselle nel mio holder
        val textType = binding.ticketType
        val textPeriod = binding.ticketPeriod
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //qui avviene l'inflate del layout, dando così uno stile alla singola riga
        val view = FragmentTicketCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    /**
     *  @return numero totale degli oggetti nella recycler view
     */
    override fun getItemCount(): Int {
        return cursorA.count+cursorB.count
    }
    fun getAbbonamentiCount(): Int {
        return cursorA.count
    }
    fun getBigliettiCount(): Int {
        return cursorA.count
    }


    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Vengono assegnati i valori a ogni singola card nel momento in cui viene creata dall'adapter
        if (cursorB.moveToPosition(position)){

            println(cursorB.getString(cursorB.getColumnIndex(DBHelper.NOME_SPETTACOLO)))
            var id = cursorB.getString(cursorB.getColumnIndex(DBHelper._ID_BIGLIETTO))
            var nomeSpettacolo = cursorB.getString(cursorB.getColumnIndex(DBHelper.NOME_SPETTACOLO))
            var dataScadenza = cursorB.getString(cursorB.getColumnIndex(DBHelper.DATA_SCADENZA))
            holder.textType.text = nomeSpettacolo
            holder.textPeriod.text = dataScadenza

            holder.itemView.setOnClickListener{
                onClickListener?.onClick(position, TicketModel(nomeSpettacolo,dataScadenza,false, id))
            }

        }else{
            if (cursorA.moveToPosition(position-cursorB.count)){
                println(cursorA.getString(cursorA.getColumnIndex(DBHelper.TEATRO)))
                var id = cursorA.getString(cursorA.getColumnIndex(DBHelper._ID_ABBONAMENTO))
                var nomeTeatro = cursorA.getString(cursorA.getColumnIndex(DBHelper.TEATRO))
                var periodo = cursorA.getString(cursorA.getColumnIndex(DBHelper.DATA_INIZIO)) + "-"+cursorA.getString(cursorA.getColumnIndex(DBHelper.DATA_FINE))
                holder.textType.text = nomeTeatro
                holder.textPeriod.text = periodo

                holder.itemView.setOnClickListener {
                    onClickListener?.onClick(position, TicketModel(nomeTeatro, periodo, true,id))
                }
            }
        }
    }
    interface OnClickListener {
        fun onClick(position: Int, model: TicketModel)
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}