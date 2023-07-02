package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentChoosePlaceBinding


class ChoosePlace(var id: String, var type: String, var period: String ): Fragment(R.layout.fragment_choose_place) {
    private lateinit var binding: FragmentChoosePlaceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoosePlaceBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        binding.confirmButton.setOnClickListener{
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            val selectedPlace = binding.placeSpinner.selectedItem.toString()
            val ticketQuantity = binding.ticketQuantity.text.toString().toInt()
            MA.realAppNavigateTo(TicketPurchase(id, type, period), "TicketPurchase")

        }


        return binding.root
    }
}