package fragment_classes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentUserOrAdminBinding

class UserOrAdmin : Fragment(R.layout.fragment_user_or_admin) {
    private lateinit var binding: FragmentUserOrAdminBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserOrAdminBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!!

        binding.userLoginButton.setOnClickListener{
            binding.userLoginButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.navigateTo(Login(),"LogFrag")
        }
        binding.adminLoginButton.setOnClickListener{
            binding.adminLoginButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.navigateTo(Login(),"LogFrag")

        }
        return binding.root
    }



}