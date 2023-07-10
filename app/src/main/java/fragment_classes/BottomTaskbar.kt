package fragment_classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import api.ApiService
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentBottomTaskbarBinding
import com.example.progettouni.databinding.FragmentLoginBinding
import com.example.progettouni.databinding.RealAppBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomTaskbar : Fragment() {
    private lateinit var binding: FragmentBottomTaskbarBinding
    private lateinit var last_fragment : Fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentBottomTaskbarBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla main Activity

        /*
        A seconda del bottone premuto si potrà navigare sulle pagine home, spettacoli, biglietti e
        acquisto abbonamenti.
         */

        binding.HomeButton.setOnClickListener(){
            MA.realAppNavigateTo(Home(), "Home")
        }
        /*
        Viene interrogato il databse remoto e viene popolata la recycle view che si occupa degli spettacoli per i quali l'utente può acquistare dei biglietti
         */
        binding.SearchButton.setOnClickListener(){
            if(!MA.getIsOffline()) {
                MA.realAppNavigateTo(Shows(), "Shows")
            }else{
                MA.showToast("SEI OFFLINE")
            }
        }
        binding.TicketButton.setOnClickListener(){
            MA.realAppNavigateTo(PosessedTickets(),"PosessedTickets")
        }
        binding.SubButton.setOnClickListener(){
            MA.realAppNavigateTo(SubscriptionChoice(),"SubscriptionChoice")
        }
        return binding.root
    }

}