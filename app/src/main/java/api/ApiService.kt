package api

import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET






interface ApiService {

    @GET("path")
    suspend fun getUtenti(): Response<List<UtenteJSON>>


    fun parseJSON() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service= retrofit.create(ApiService::class.java)


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getUtenti()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful()) {
                        val items = response.body()

                        if (items != null) {
                            for (i in 0 until items.count()) {
                                val id = items[i].id
                                val mail = items[i].mail
                                val nome = items[i].nome
                                val cognome = items[i].cognome
                                val password = items[i].password
                                val propic = items[i].propic
                                val codVer = items[i].cod_ver
                                val verificato = items[i].verificato

                                Log.d("Utente:", "ID: $id, Mail: $mail, Nome: $nome, Cognome: $cognome, Password: $password, Propic: $propic, Cod Ver: $codVer, Verificato: $verificato")
                            }
                        } else {

                        }
                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            } catch (e: Exception) {
                Log.e("RETROFIT_ERROR", e.message.toString())
            }
        }
    }



}


