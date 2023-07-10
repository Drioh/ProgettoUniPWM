package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentChoosePlaceBinding


class ChoosePlace(): Fragment() {
    private lateinit var binding: FragmentChoosePlaceBinding
    lateinit var id: String
    lateinit var type: String
    lateinit var period: String
    lateinit var textTheatre: String
    constructor(id: String, type: String,  period: String, textTheatre: String): this(){
        this.id=id
        this.type = type
        this.period = period
        this.textTheatre = textTheatre
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("id",id)
        outState.putString("type",type)
        outState.putString("period",period)
        outState.putString("textTheatre",textTheatre)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState!=null){
            this.id=savedInstanceState.getString("id").toString()
            this.type = savedInstanceState.getString("type").toString()
            this.period = savedInstanceState.getString("period").toString()
            this.textTheatre = savedInstanceState.getString("textTheatre").toString()

        }
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentChoosePlaceBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Scegli i posti")
        binding.confirmButton.setOnClickListener{
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            val selectedPlace = binding.placeSpinner.selectedItem.toString()
            val ticketQuantity = binding.ticketQuantity.text.toString().toInt()
            if(ticketQuantity != 0){
                MA.realAppNavigateTo(TicketPurchase(id, type, period, selectedPlace, ticketQuantity, textTheatre), "TicketPurchase")
            }
            else{
                MA.showToast("numero di biglietti non valido")
            }
        }


        return binding.root
    }




}