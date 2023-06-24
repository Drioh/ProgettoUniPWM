package fragment_classes

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity

import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentShowInfoBinding
import com.example.progettouni.databinding.FragmentShowsBinding
import com.example.progettouni.databinding.FragmentTheatreInfoBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Math.sqrt
import kotlin.math.pow
import android.Manifest
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.OnMapReadyCallback

class TheatreInfo : Fragment(R.layout.fragment_show_info), OnMapReadyCallback {
    private lateinit var binding: FragmentTheatreInfoBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val teatri = listOf(
        Teatro("Teatro Massimo", 38.1197, 13.3550),
        Teatro("Teatro Biondo", 38.1244, 13.3657),
        Teatro("Politeama", 38.1228, 13.3654)
    )
    data class Teatro(val nome: String, val latitudine: Double, val longitudine: Double)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTheatreInfoBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! // reference alla MainActivity

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }


    private fun calcolaDistanza(posizione1: LatLng, posizione2: LatLng): Double {
        val diffLat = posizione2.latitude - posizione1.latitude
        val diffLng = posizione2.longitude - posizione1.longitude
        return sqrt(diffLat.pow(2) + diffLng.pow(2)) * RAGGIO_TERRA_KM
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val RAGGIO_TERRA_KM = 6371.0
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }



    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true

            val tuaLatitudine = 38.1157
            val tuaLongitudine = 13.3613
            val posizioneCorrente = LatLng(tuaLatitudine, tuaLongitudine)

            teatri.forEach { teatro ->
                val posizioneTeatro = LatLng(teatro.latitudine, teatro.longitudine)
                googleMap.addMarker(MarkerOptions().position(posizioneTeatro).title(teatro.nome))
            }

            googleMap.addMarker(MarkerOptions().position(posizioneCorrente).title("La Tua Posizione"))

            val teatroPiùVicino = teatri.minByOrNull { teatro ->
                calcolaDistanza(posizioneCorrente, LatLng(teatro.latitudine, teatro.longitudine))
            }

            teatroPiùVicino?.let { teatro ->
                val distanza = calcolaDistanza(posizioneCorrente, LatLng(teatro.latitudine, teatro.longitudine))
                //binding.distanzaTextView.text = String.format("Distanza: %.2f km", distanza)
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneCorrente, 12f))
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
}
