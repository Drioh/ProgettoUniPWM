package fragment_classes

import android.Manifest
import android.app.AlertDialog
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
import androidx.activity.result.contract.ActivityResultContracts

class TheatreInfo (val idTeatro: String, val purchase: Boolean): Fragment(R.layout.fragment_show_info), OnMapReadyCallback {
    private lateinit var binding: FragmentTheatreInfoBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var posizioneCorrente: LatLng
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val teatri = listOf(
        Teatro("Teatro Massimo", 38.12029014707951, 13.357262849337985),
        Teatro("Teatro Politeama", 38.12508909561492, 13.356498048923825),
        Teatro("Teatro Biondo", 38.11783642954718, 13.36291279682799)
    )

    data class Teatro(val nome: String, val latitudine: Double, val longitudine: Double)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTheatreInfoBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! // reference alla MainActivity
        MA.changeTitle("Informazioni")
        binding.info.visibility = View.INVISIBLE
        println("------------------------------------------------------")
        println(idTeatro)
        println(idTeatro)
        println(idTeatro)
        println(idTeatro)



        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        when (idTeatro) {
            "1" -> {
                println("Questo è il teatro Massimo")
                binding.TheatreName.setText((R.string.TMassimo))
                binding.DescText.setText(R.string.MassimoDesription)
                binding.theatreImage.setImageResource(R.drawable.teatro_massimo)
                binding.DYKText.setText(R.string.MassimoDYK)
                binding.wikiButton.setOnClickListener {
                    val url = "https://it.wikipedia.org/wiki/Teatro_Massimo_Vittorio_Emanuele"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            "2" -> {
                println("Questo è il teatro Politeama")
                binding.TheatreName.setText(R.string.TPoliteama)
                binding.DescText.setText(R.string.PoliteamaDescription)
                binding.theatreImage.setImageResource(R.drawable.teatro_politeama)
                binding.DYKText.setText(R.string.PoliteamaDYK)

                binding.wikiButton.setOnClickListener {
                    val url = "https://it.wikipedia.org/wiki/Teatro_Politeama_(Palermo)"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            "3" -> {
                println("Questo è il teatro Biondo")
                binding.TheatreName.setText(R.string.TBiondo)
                binding.DescText.setText(R.string.BiondoDescription)
                binding.theatreImage.setImageResource(R.drawable.teatro_biondo)
                binding.DYKText.setText(R.string.BiondoDYK)

                binding.wikiButton.setOnClickListener {
                    val url = "https://it.wikipedia.org/wiki/Teatro_Biondo"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            else -> println("Errore")
        }

        if(!purchase){
            binding.buySubscriptionButton.visibility = View.INVISIBLE
        }
        binding.buySubscriptionButton.setOnClickListener{
            MA.realAppNavigateTo(SubscriptionPurchase(idTeatro),"SubscriptionPurchase")
        }
        binding.closestButton.setOnClickListener{
            MA.showToast(R.string.closestTheatre as String)
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        showInContextUI()
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
    // Funzione per mostrare un'interfaccia utente informativa all'utente

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Il permesso di posizione è stato concesso
                // Effettua le operazioni desiderate dopo l'ottenimento del permesso
                showToast("Permesso di posizione concesso")
                mapView.visibility = View.VISIBLE

                binding.info.visibility = View.GONE
                showMapWithCoordinates()

            } else {
                // Il permesso di posizione è stato negato
                showToast("È necessario concedere il permesso di posizione per utilizzare questa funzionalità.")
                mapView.visibility = View.GONE
                binding.info.visibility = View.VISIBLE
                binding.info.text = "Permesso negato"


            }
        }
    }

    private fun showMapWithCoordinates() {
        mapView.visibility = View.VISIBLE
        binding.info.visibility = View.GONE

        mapView.getMapAsync { googleMap ->
            val teatro: Teatro
            val posizioneTeatro: LatLng

            when (idTeatro) {
                "1" -> {
                    teatro = teatri[0]
                    posizioneTeatro = LatLng(teatro.latitudine, teatro.longitudine)
                }
                "2" -> {
                    teatro = teatri[1]
                    posizioneTeatro = LatLng(teatro.latitudine, teatro.longitudine)
                }
                "3" -> {
                    teatro = teatri[2]
                    posizioneTeatro = LatLng(teatro.latitudine, teatro.longitudine)
                }
                else -> {
                    println("Errore: teatro non trovato")
                    return@getMapAsync
                }
            }



            googleMap.addMarker(
                MarkerOptions().position(posizioneTeatro).title(teatro.nome)
            )

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneTeatro, 15f))

            googleMap.setOnMapClickListener { clickedPosition ->
                val label = teatro.nome
                val uri = "geo:${clickedPosition.latitude},${clickedPosition.longitude}?q=${clickedPosition.latitude},${clickedPosition.longitude}($label)"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
    }






    private fun showInContextUI() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            // Il permesso è stato già concesso in precedenza
            Toast.makeText(requireContext(), "Permesso di posizione già concesso", Toast.LENGTH_SHORT).show()
        } else {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setTitle("Permesso di posizione richiesto")
            dialogBuilder.setMessage("L'app richiede l'accesso alla tua posizione per mostrare la mappa del teatro più vicino. Concedi il permesso per continuare.")
            dialogBuilder.setPositiveButton("Concedi") { _, _ ->
                // Richiedi il permesso di posizione

                requestPermissions(
                    arrayOf(permission),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            dialogBuilder.setNegativeButton("Annulla") { _, _ ->
                // L'utente ha scelto di non concedere il permesso
                showToast("È necessario concedere il permesso di posizione per utilizzare questa funzionalità.")
                mapView.visibility = View.GONE
                binding.info.visibility = View.VISIBLE
                binding.info.text = "Permesso negato"
                // Disabilita la funzionalità di Google Maps
                googleMap?.isMyLocationEnabled = false
            }
            dialogBuilder.setOnCancelListener {
                // L'utente ha annullato il dialogo
                // Puoi gestire questa situazione come desideri, ad esempio mostrando un messaggio o disabilitando funzionalità specifiche.
                showToast("È necessario concedere il permesso di posizione per utilizzare questa funzionalità.")
                mapView.visibility = View.GONE
                binding.info.visibility = View.VISIBLE
                binding.info.text = "Permission denied"
                googleMap?.isMyLocationEnabled = false
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }
    // Funzione per mostrare un messaggio all'utente
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map


        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true


            when (idTeatro) {
                "1" -> {
                    val posizioneTeatro = LatLng(teatri[0].latitudine, teatri[0].longitudine)
                    googleMap.addMarker(
                        MarkerOptions().position(posizioneTeatro).title(teatri[0].nome)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneTeatro, 15f))
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
                }

                "2" -> {
                    val posizioneTeatro = LatLng(teatri[1].latitudine, teatri[1].longitudine)
                    googleMap.addMarker(
                        MarkerOptions().position(posizioneTeatro).title(teatri[1].nome)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneTeatro, 15f))
                    googleMap.setOnMapClickListener {


                        val latitude = teatri[1].latitudine
                        val longitude = teatri[1].longitudine // Longitudine della posizione desiderata
                        val label = teatri[1].nome
                        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        intent.setPackage("com.google.android.apps.maps") // Specifica che l'intent dovrebbe essere gestito da Google Maps
                        if (intent.resolveActivity(requireActivity().packageManager) != null) {
                            startActivity(intent)
                        }

                    }
                }


                "3" -> {
                    val posizioneTeatro = LatLng(teatri[2].latitudine, teatri[2].longitudine)
                    googleMap.addMarker(
                        MarkerOptions().position(posizioneTeatro).title(teatri[2].nome)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneTeatro, 15f))
                    googleMap.setOnMapClickListener {


                        val latitude = teatri[2].latitudine
                        val longitude = teatri[2].longitudine // Longitudine della posizione desiderata
                        val label = teatri[2].nome
                        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        intent.setPackage("com.google.android.apps.maps") // Specifica che l'intent dovrebbe essere gestito da Google Maps
                        if (intent.resolveActivity(requireActivity().packageManager) != null) {
                            startActivity(intent)
                        }

                    }
                }

                else -> println("Errore")
            }
/*
            var location = fusedLocationProviderClient.lastLocation
            location.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentLocation = task.result
                    if (currentLocation != null) {
                        println("bravissimo")
                        val tuaLatitudine = currentLocation.latitude
                        val tuaLongitudine = currentLocation.longitude
                        posizioneCorrente = LatLng(tuaLatitudine, tuaLongitudine)
                        println(posizioneCorrente.toString())
                        googleMap.addMarker(
                            MarkerOptions().position(posizioneCorrente).title("La Tua Posizione")
                        )
                        val teatrovicino = teatri.minByOrNull { teatro ->
                            calcolaDistanza(
                                posizioneCorrente,
                                LatLng(teatro.latitudine, teatro.longitudine)
                            )
                        }

                        if (teatrovicino != null && teatrovicino.nome == "TEATRO "+idTeatro.uppercase()) {

                                binding.closestButton.visibility = View.VISIBLE

                        }else{
                            println("Teatro più vicino non trovato")
                        }

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneCorrente, 13f))
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSION_REQUEST_CODE
                        )
                    }
                    } else {
                        println("erroraccio")
                    }

                }*/
        }


    }

}
