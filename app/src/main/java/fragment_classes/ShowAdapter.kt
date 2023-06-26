package fragment_classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettouni.databinding.FragmentShowCardBinding
import com.example.progettouni.databinding.FragmentTicketCardBinding

class ShowAdapter(private val myList: List<ShowModel>) : RecyclerView.Adapter<ShowAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    class ViewHolder(binding: FragmentShowCardBinding) : RecyclerView.ViewHolder(binding.root) {   //creo le caselle nel mio holder
        val imageView = binding.imageView2
        val textName = binding.ShowName
        val textDate = binding.ShowDate
        val textTheatre = binding.showTheatre
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //qui avviene l'inflate del layout, dando così uno stile alla singola riga
        val view = FragmentShowCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    /**
     *  @return numero totale degli oggetti nella recycler view
     */
    override fun getItemCount(): Int {
        return myList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Vengono assegnati i valori a ogni singola card nel momento in cui viene creata dall'adapter
        val singleShowModel = myList[position]
        holder.imageView.setImageResource(singleShowModel.image)
        holder.textName.text = singleShowModel.textName
        holder.textDate.text = singleShowModel.textDate
        holder.textTheatre.text = singleShowModel.textTheatre


        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, singleShowModel)
        }
    }
    interface OnClickListener {
        fun onClick(position: Int, model: ShowModel)
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}