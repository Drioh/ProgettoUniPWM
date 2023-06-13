package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentEditMailBinding
import com.example.progettouni.databinding.FragmentEditPasswordBinding
import com.example.progettouni.databinding.FragmentRetrievePasswordBinding
import com.example.progettouni.databinding.FragmentSubscriptionPurchaseBinding

class EditMail: Fragment(R.layout.fragment_retrieve_password){
    private lateinit var binding: FragmentEditMailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditMailBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!


        binding.confirmButton.setOnClickListener(){
            //effettuare un test sulla mail
            MA.showToast("Mail modificata correttamente")
            MA.back()
        }
        binding.cancelButton.setOnClickListener(){
            MA.back()
        }

        return binding.root
    }
}