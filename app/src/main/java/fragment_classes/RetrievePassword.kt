package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentRetrievePasswordBinding
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding

class RetrievePassword: Fragment(R.layout.fragment_retrieve_password){
    private lateinit var binding: FragmentRetrievePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRetrievePasswordBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity


        binding.confirmButton.setOnClickListener(){
            MA.back()
            TODO("Interrogare il database e modificare la password")
        }
        binding.cancelButton.setOnClickListener(){
            MA.back()
        }

        return binding.root
    }
}