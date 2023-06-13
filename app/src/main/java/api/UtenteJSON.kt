package api

import com.google.gson.annotations.SerializedName

data class UtenteJSON(
    @SerializedName("id_utente")
    val id: Int,
    @SerializedName("mail")
    val mail: String,
    @SerializedName("nome")
    val nome: String,
    @SerializedName("cognome")
    val cognome: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("propic")
    val propic: String,
    @SerializedName("cod_ver")
    val cod_ver: String,
    @SerializedName("verificato")
    val verificato: Boolean
)
