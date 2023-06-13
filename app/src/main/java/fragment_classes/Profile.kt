package fragment_classes

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
        var MA = (activity as MainActivity?)!!


        binding.mailButton.setOnClickListener(){
            MA.back()
            //effettuare un test
            MA.showToast("Password modificata correttamente")
        }
        binding.passwordButton.setOnClickListener(){
            MA.back()
        }
        binding.confirmButton.setOnClickListener(){
            MA.back()
        }
        binding.cancelButton.setOnClickListener(){
            MA.back()
        }

        return binding.root
    }
}