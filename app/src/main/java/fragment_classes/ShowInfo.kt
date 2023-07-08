package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentShowInfoBinding

class ShowInfo(var id: String, var name: String, var date: String, var textTheatre: String): Fragment(R.layout.fragment_show_info) {
    private lateinit var binding: FragmentShowInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowInfoBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Informazioni Spettacolo")

/*      INIZIALIZZAZIONE DATI
        binding.directorText.setText()
        binding.FirstText.setText()
        binding.CompanyText.setText()
        binding.ShowDesc.setText()
        binding.DateText.setText()
        binding.theatreButton.setText()
*/
        binding.buyTicketButton.setOnClickListener{
            binding.buyTicketButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(ChoosePlace(id, name, date, textTheatre), "ChoosePlace") //da mettere qui o dopo il navigate per ChoosePlace
        }



        return binding.root
    }
}