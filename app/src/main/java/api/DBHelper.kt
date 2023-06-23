package api

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * DBHelper è una classe di supporto per la gestione del database SQLite.
 * Estende la classe SQLiteOpenHelper che fornisce metodi per creare e aggiornare il database.
 * Viene utilizzata per creare, gestire e interagire con le tabelle del database.
 *
 * @param context Il contesto dell'applicazione.
 */

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    /**
     * Definizioni costanti per il nome del database, il nome delle tabelle, le colonne e la versione del database.
     */
    companion object {
        // DATABASE NAME
        const val DB_NAME = "Biglietteria"

        // TABLE NAMES
        const val TABLE_BIGLIETTI = "Biglietti"
        const val TABLE_ABBONAMENTO = "Abbonamento"

        // COMMON COLUMNS
        const val _ID = "_id"

        // TABLE BIGLIETTI COLUMNS
        const val NOME_SPETTACOLO = "nome_spettacolo"
        const val DATA_SCADENZA = "data_scadenza"

        // TABLE ABBONAMENTO COLUMNS
        const val TEATRO = "teatro"
        const val DATA_INIZIO = "data_inizio"
        const val DATA_FINE = "data_fine"

        // DATABASE VERSION
        const val DB_VERSION = 1

        // STRING TO CREATE TABLE BIGLIETTI

        private const val SQL_CREATE_TABLE_BIGLIETTI =
            "CREATE TABLE $TABLE_BIGLIETTI (" +
                    "${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${NOME_SPETTACOLO} TEXT NOT NULL," +
                    "${DATA_SCADENZA} TEXT NOT NULL);"

        // STRING TO CREATE TABLE ABBONAMENTO
        private const val SQL_CREATE_TABLE_ABBONAMENTO =
            "CREATE TABLE $TABLE_ABBONAMENTO (" +
                    "${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${TEATRO} TEXT NOT NULL," +
                    "${DATA_INIZIO} TEXT NOT NULL," +
                    "${DATA_FINE} TEXT NOT NULL);"


        // STRING TO DROP TABLE BIGLIETTI
        private const val SQL_DROP_TABLE_BIGLIETTI = "DROP TABLE IF EXISTS $TABLE_BIGLIETTI;"

        // STRING TO DROP TABLE ABBONAMENTO
        private const val SQL_DROP_TABLE_ABBONAMENTO = "DROP TABLE IF EXISTS $TABLE_ABBONAMENTO;"
    }
    /**
     * Viene chiamato quando il database viene creato per la prima volta.
     * Viene eseguito il comando SQL per creare le tabelle.
     *
     * @param db Il database SQLite.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_BIGLIETTI)
        db?.execSQL(SQL_CREATE_TABLE_ABBONAMENTO)
    }

    /**
     * Viene chiamato quando viene rilevata una versione più recente del database.
     * Vengono eliminati le tabelle esistenti e viene eseguito il comando SQL per ricrearle.
     *
     * @param db Il database SQLite.
     * @param oldVersion La vecchia versione del database.
     * @param newVersion La nuova versione del database.
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DROP_TABLE_BIGLIETTI)
        db?.execSQL(SQL_DROP_TABLE_ABBONAMENTO)
        onCreate(db)
    }
}