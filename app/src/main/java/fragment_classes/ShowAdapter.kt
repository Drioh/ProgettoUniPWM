package fragment_classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import api.ApiService
import com.example.progettouni.databinding.FragmentShowCardBinding
import com.example.progettouni.databinding.FragmentTicketCardBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        getImageSpettacolo(singleShowModel.image,holder.imageView)
        holder.textName.text = singleShowModel.textName
        holder.textDate.text = singleShowModel.textDate
        holder.textTheatre.text = singleShowModel.textTheatre


        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, singleShowModel)
        }
    }
    /**
     * Ottiene un'immagine dal server tramite l'URL specificato e la imposta in un ImageView.
     *
     * @param url L'URL dell'immagine da ottenere.
     * @param IV L'istanza dell'ImageView in cui impostare l'immagine ottenuta.
     * @return L'oggetto Bitmap dell'immagine ottenuta, o null se si è verificato un errore.
     */
    private fun getImageSpettacolo(url: String, IV: ImageView): Bitmap?{
        var image: Bitmap? = null
        ApiService.retrofit.image(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        if (response.body()!=null) {
                            image = BitmapFactory.decodeStream(response.body()?.byteStream())
                            IV.setImageBitmap(image)

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    Log.e("ApiService", t.message.toString())
                }

            }
        )
        return image
    }
    interface OnClickListener {
        fun onClick(position: Int, model: ShowModel)
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}