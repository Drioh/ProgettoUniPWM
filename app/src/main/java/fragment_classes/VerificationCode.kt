package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentRegisterBinding
import com.example.progettouni.databinding.FragmentVerificationCodeBinding
import com.example.progettouni.databinding.FragmentVoidBinding

class VerificationCode(val otp: String): Fragment(R.layout.fragment_verification_code) {
    private lateinit var binding: FragmentVerificationCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        binding = FragmentVerificationCodeBinding.inflate(inflater)
        binding.codeText.text=otp
        binding.confirm.setOnClickListener{
            MA.back()
            MA.back()
        }
        return binding.root
    }
}