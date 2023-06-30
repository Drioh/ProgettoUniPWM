package fragment_classes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentTheatreInfoBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import java.lang.Math.sqrt
import java.util.*
import kotlin.math.pow

class TheatreInfo (val idTeatro: String): Fragment(R.layout.fragment_show_info), OnMapReadyCallback {
    private lateinit var binding: FragmentTheatreInfoBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var posizioneCorrente: LatLng
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val teatri = listOf(
        Teatro("Teatro Massimo", 38.12029014707951, 13.357262849337985),
        Teatro("Teatro Biondo", 38.12508909561492, 13.356498048923825),
        Teatro("Politeama", 38.11783642954718, 13.36291279682799)
    )

    data class Teatro(val nome: String, val latitudine: Double, val longitudine: Double)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTheatreInfoBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! // reference alla MainActivity
        println(idTeatro)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        when (idTeatro) {
            "Massimo" -> {
                println("Questo è il teatro Massimo")
                binding.TheatreName.setText(R.string.TMassimo)
                binding.DescText.setText(R.string.MassimoDesription)
                binding.theatreImage.setImageResource(R.drawable.teatro_massimo)
                binding.wikiButton.setOnClickListener {
                    val url = "https://it.wikipedia.org/wiki/Teatro_Massimo_Vittorio_Emanuele"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            "Politeama" -> {
                println("Questo è il teatro Politeama")
                binding.TheatreName.setText(R.string.TPoliteama)
                binding.DescText.setText(R.string.PoliteamaDescription)
                binding.theatreImage.setImageResource(R.drawable.teatro_politeama)
                binding.wikiButton.setOnClickListener {
                    val url = "https://it.wikipedia.org/wiki/Teatro_Politeama_(Palermo)"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }

            }
            "Biondo" -> {
                println("Questo è il teatro Biondo")
                binding.TheatreName.setText(R.string.TBiondo)
                binding.DescText.setText(R.string.BiondoDescription)
                binding.theatreImage.setImageResource(R.drawable.teatro_biondo)
                binding.wikiButton.setOnClickListener {
                    val url = "https://it.wikipedia.org/wiki/Teatro_Biondo"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }

            }
            else -> println("Errore")
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())



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
        googleMap.setOnMapClickListener {

            val latitude = teatri[0].latitudine
            val longitude = teatri[0].longitudine // Longitudine della posizione desiderata
            val label = teatri[0].nome
            val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps") // Specifica che l'intent dovrebbe essere gestito da Google Maps
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }

        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true


            when (idTeatro) {
                "Massimo" -> {
                    val posizioneTeatro = LatLng(teatri[0].latitudine, teatri[0].longitudine)
                    googleMap.addMarker(
                        MarkerOptions().position(posizioneTeatro).title(teatri[0].nome)
                    )
                }

                "Politeama" -> {
                    val posizioneTeatro = LatLng(teatri[1].latitudine, teatri[1].longitudine)
                    googleMap.addMarker(
                        MarkerOptions().position(posizioneTeatro).title(teatri[1].nome)
                    )
                }


                "Biondo" -> {
                    val posizioneTeatro = LatLng(teatri[2].latitudine, teatri[2].longitudine)
                    googleMap.addMarker(
                        MarkerOptions().position(posizioneTeatro).title(teatri[2].nome)
                    )
                }

                else -> println("Errore")
            }
            var location = fusedLocationProviderClient.lastLocation
            location.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentLocation = task.result
                    if (currentLocation != null) {
                        val tuaLatitudine = currentLocation.latitude
                        val tuaLongitudine = currentLocation.longitude
                        posizioneCorrente = LatLng(tuaLatitudine, tuaLongitudine)
                        println(posizioneCorrente.toString())
                    } else {
                        println("erroraccio")
                    }

                }
            }

            googleMap.addMarker(
                MarkerOptions().position(posizioneCorrente).title("La Tua Posizione")
            )

            val teatroPiùVicino = teatri.minByOrNull { teatro ->
                calcolaDistanza(
                    posizioneCorrente,
                    LatLng(teatro.latitudine, teatro.longitudine)
                )
            }

            teatroPiùVicino?.let { teatro ->
                val distanza = calcolaDistanza(
                    posizioneCorrente,
                    LatLng(teatro.latitudine, teatro.longitudine)
                )
                //binding.distanzaTextView.text = String.format("Distanza: %.2f km", distanza)
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneCorrente, 13f))
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

}
