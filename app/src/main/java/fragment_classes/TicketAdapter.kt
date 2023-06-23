package fragment_classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettouni.databinding.FragmentTicketCardBinding

class TicketAdapter(private val myList: List<TicketModel>) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {
    class ViewHolder(binding: FragmentTicketCardBinding) : RecyclerView.ViewHolder(binding.root) {   //creo le caselle nel mio holder
        val imageView = binding.imageView2
        val textType = binding.ticketType
        val textPeriod = binding.ticketPeriod
        private var onClickListener: View.OnClickListener? = null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //qui avviene l'inflate del layout, dando così uno stile alla singola riga
        val view = FragmentTicketCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {   //indica il numero totale di oggetti
        return myList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //assegno i valori a ogni singola card quando vengono create dall'adapter
        val singleTicketModel = myList[position]
        holder.imageView.setImageResource(singleTicketModel.image)
        holder.textType.text = singleTicketModel.textType
        holder.textPeriod.text = singleTicketModel.textPeriod

        holder.itemView.setOnClickListener {               //da qui parte il click e io non so più nulla
            onClickListener?.onClick(position, TicketModel)
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: TicketModel)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}