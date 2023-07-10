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

class VerificationCode(): Fragment() {
    private lateinit var binding: FragmentVerificationCodeBinding

    private lateinit var otp: String

    constructor( otp: String): this(){
        this.otp = otp
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("otp",otp)
        super.onSaveInstanceState(outState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState!=null){
            this.otp=savedInstanceState.getString("otp").toString()

        }
        super.onCreateView(inflater, container, savedInstanceState)
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