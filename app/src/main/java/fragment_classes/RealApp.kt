package fragment_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.progettouni.R
import com.example.progettouni.databinding.ActivityMainBinding
import com.example.progettouni.databinding.RealAppBinding

class RealApp: Fragment(R.layout.real_app) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
