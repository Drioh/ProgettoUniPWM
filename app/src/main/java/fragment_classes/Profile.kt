package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentProfileBinding
import com.example.progettouni.databinding.FragmentRetrievePasswordBinding

class Profile : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity


        binding.mailButton.setOnClickListener(){
            binding.mailButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(EditMail(),"EditMail")
        }
        binding.passwordButton.setOnClickListener(){
            binding.passwordButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(EditPassword(),"EditMail")

        }
        binding.confirmButton.setOnClickListener(){
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            TODO("impostare Nome e Cognome se diversi da quelli salvati in precedenza")
            MA.back()
        }
        binding.cancelButton.setOnClickListener(){
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()
        }

        return binding.root
    }
}