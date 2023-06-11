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

        binding.button2.setOnClickListener{
            binding.button2.setBackgroundColor(Color.parseColor("#F44336"))
            MA.navigateTo(Login())
        }
        binding.button3.setOnClickListener{
            binding.button3.setBackgroundColor(Color.parseColor("#F44336"))
            MA.navigateTo(Login())

        }
        return binding.root
    }



}