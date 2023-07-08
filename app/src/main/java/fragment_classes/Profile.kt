package fragment_classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import api.ApiService
import api.DBHelper
import api.DBManager
import com.example.progettouni.MainActivity
import com.example.progettouni.R
import com.example.progettouni.databinding.FragmentProfileBinding
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class Profile : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var dbManager: DBManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        var MA = (activity as MainActivity?)!! //reference alla Main Activity
        MA.changeTitle("Profilo")
        val sharedPreferences = MA.getSharedPreferences()
        val userName = sharedPreferences.getString("userName", "")
        val surname = sharedPreferences.getString("surname", "")
        if (!userName.isNullOrEmpty() && !surname.isNullOrEmpty()) {
            binding.nameField.setText(userName)
            binding.surnameField.setText(surname)
        }
        val propic = sharedPreferences.getString("propic", "")
        if (!propic.isNullOrEmpty()) {
            val decodedImage = decodeBase64ToBitmap(propic)
            binding.propicImage.setImageBitmap(decodedImage)
        }
        getUrlbyID(MA.getUserId())

        binding.mailButton.setOnClickListener(){
            binding.mailButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(EditMail(), "EditMail")
        }
        binding.passwordButton.setOnClickListener(){
            binding.passwordButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.realAppNavigateTo(EditPassword(), "EditPassword")

        }
        binding.verificationButton.setOnClickListener{
            binding.verificationButton.setBackgroundColor(Color.parseColor("#F44336"))
            val id = MA.getUserId()
            val query = "SELECT * FROM Utente WHERE id_utente = '${id}';"
            ApiService.retrofit.select(query).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonArray = response.body()?.getAsJsonArray("queryset")
                        if (jsonArray?.size() == 1) {
                            if ((jsonArray[0] as JsonObject).get("verificato").asInt==0) {
                                MA.realAppNavigateTo(Verify(), "Verify")
                            }
                            else{
                                MA.showToast("Utente già registrato")
                            }

                        } else {
                            Log.i("ApiService","n/a")
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Errore di rete",t.message.toString())
                }
            })

        }
        binding.confirmButton.setOnClickListener(){
            binding.confirmButton.setBackgroundColor(Color.parseColor("#F44336"))
            changeNameAndSurname(MA.getUserId(),binding.nameField.text.toString(),binding.surnameField.text.toString())
            //MA.back()

            val editor = sharedPreferences.edit()
            editor.putString("userName", binding.nameField.text.toString())
            editor.putString("surname", binding.surnameField.text.toString())
            editor.apply()
        }
        binding.cancelButton.setOnClickListener(){
            binding.cancelButton.setBackgroundColor(Color.parseColor("#F44336"))
            MA.back()
        }
        binding.logoutButton.setOnClickListener(){
            binding.logoutButton.setBackgroundColor(Color.parseColor("#F44336"))
            // Svuota le SharedPreferences
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Torna al fragment di login
            MA.logout()
        }

        return binding.root
    }



    //TEST
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inizializza il DBManager
        dbManager = DBManager(requireContext())

        // Esegui operazioni di test
        //performDatabaseTest()


    }
/*                  DEBUG
    private fun performDatabaseTest() {
        // Apri il database
        dbManager.open()

        // Esegui operazioni di test
        insertData()
        updateData()
        deleteData()
        fetchData()


        // Chiudi il database
        dbManager.close()
    }

    private fun insertData() {
        dbManager.insertBiglietto("Spettacolo 1", "2023-06-30")
        dbManager.insertBiglietto("Spettacolo 2", "2023-07-15")
        Log.i("Profile", "Dati inseriti nel database.")
    }

    private fun updateData() {
        val updatedRows = dbManager.updateBiglietto(1, "Spettacolo aggiornato", "2023-07-31")
        Log.i("Profile", "Numero di righe aggiornate: $updatedRows")
    }

    private fun deleteData() {
        dbManager.deleteBiglietto(2)
        Log.i("Profile", "Dati eliminati dal database.")
    }
    private fun fetchData() {
        val cursor = dbManager.fetchAllBiglietti()
        cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper._ID_BIGLIETTO))
                    val nomeSpettacolo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NOME_SPETTACOLO))
                    val dataScadenza = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DATA_SCADENZA))

                    Log.i("Profile", "ID: $id, Nome Spettacolo: $nomeSpettacolo, Data Scadenza: $dataScadenza")
                } while (cursor.moveToNext())
            }
        }
    }*/



    fun getUrlbyID(id: Int) {
        val query = "SELECT propic FROM Utente WHERE id_utente = '${id}';"
        ApiService.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonArray = response.body()?.getAsJsonArray("queryset")
                    if (jsonArray?.size() == 1) {
                        val propic = jsonArray[0].asJsonObject["propic"].asString
                        getImageProfilo(propic)

                    } else {
                        Log.i("ApiService","n/a")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("Errore di rete",t.message.toString())
            }
        })
    }
    private fun getImageProfilo(url: String){

        ApiService.retrofit.image(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        var image: Bitmap? = null
                        if (response.body()!=null) {
                            image = BitmapFactory.decodeStream(response.body()?.byteStream())
                            binding.propicImage.setImageBitmap(image)

                            //SharedPreferences
                            var MA = (activity as MainActivity?)!!
                            val sharedPreferences = MA.getSharedPreferences()
                            val editor = sharedPreferences.edit()
                            val encodedImage = encodeBitmapToBase64(image)
                            editor.putString("propic", encodedImage)
                            editor.apply()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    Log.e("ApiService", t.message.toString())
                }

            }
        )
    }
    private fun decodeBase64ToBitmap(encodedImage: String): Bitmap {
        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
    fun changeNameAndSurname(userId: Int, name: String, surname: String) {
        val query = "UPDATE Utente SET nome_utente = '${name}', cognome = '${surname}' WHERE id_utente = ${userId};"

        ApiService.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.i("ApiService", "Success")

                    } else {
                        println(response.code())
                        Log.e("ApiService", "Failed")
                        Log.e("ApiService", response.message().toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("ApiService", t.message.toString())
                }
            }
        )
    }


}