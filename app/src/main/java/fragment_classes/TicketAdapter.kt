package fragment_classes

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api.DBHelper
import com.example.progettouni.databinding.FragmentTicketCardBinding

class TicketAdapter(private val _cursor: Cursor) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {
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
        return _cursor.count
    }


    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Vengono assegnati i valori a ogni singola card nel momento in cui viene creata dall'adapter
        if (_cursor.moveToPosition(position)) {
            holder.textType.text =
                _cursor.getString(_cursor.getColumnIndex(DBHelper._ID_BIGLIETTO))
            holder.textPeriod.text =
                _cursor.getString(_cursor.getColumnIndex(DBHelper.DATA_SCADENZA))
        }

        holder.itemView.setOnClickListener {
     //       onClickListener?.onClick(position, singleTicketModel)
        }
    }
    interface OnClickListener {
        fun onClick(position: Int, model: TicketModel)
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}