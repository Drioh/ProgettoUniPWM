package api

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Classe DBManager che gestisce le operazioni sul database utilizzando un DBHelper.
 *
 * @param context Il contesto dell'applicazione.
 */
class DBManager(val context: Context) {
    private lateinit var helper: DBHelper
    private lateinit var db: SQLiteDatabase

    /**
     * Apre il database in modalità scrittura.
     *
     * @return Istanza del DBManager corrente.
     */
    fun open(): DBManager {
        helper = DBHelper(context)
        db = helper.writableDatabase
        return this
    }

    /**
     * Chiude il database.
     */
    fun close() {
        helper.close()
    }

    /**
     * Inserisce un nuovo record nella tabella "Biglietti" del database.
     *
     * @param nomeSpettacolo Il nome dello spettacolo del biglietto.
     * @param dataScadenza La data di scadenza del biglietto.
     */
    fun insertBiglietto(nomeSpettacolo: String, dataScadenza: String) {
        val values = ContentValues().apply {
            put(DBHelper.NOME_SPETTACOLO, nomeSpettacolo)
            put(DBHelper.DATA_SCADENZA, dataScadenza)
        }
        val newRowId = db.insert(DBHelper.TABLE_BIGLIETTI, null, values)
    }

    /**
     * Inserisce un nuovo record nella tabella "Abbonamento" del database.
     *
     * @param teatro Il nome del teatro per l'abbonamento.
     * @param dataInizio La data di inizio dell'abbonamento.
     * @param dataFine La data di fine dell'abbonamento.
     */
    fun insertAbbonamento(teatro: String, dataInizio: String, dataFine: String) {
        val values = ContentValues().apply {
            put(DBHelper.TEATRO, teatro)
            put(DBHelper.DATA_INIZIO, dataInizio)
            put(DBHelper.DATA_FINE, dataFine)
        }
        val newRowId = db.insert(DBHelper.TABLE_ABBONAMENTO, null, values)
    }

    /**
     * Aggiorna un record esistente nella tabella "Biglietti" del database.
     *
     * @param id_biglietto L'ID del record da aggiornare.
     * @param nomeSpettacolo Il nuovo nome dello spettacolo del biglietto.
     * @param dataScadenza La nuova data di scadenza del biglietto.
     * @return Il numero di righe aggiornate nel database.
     */
    fun updateBiglietto(id_biglietto: Long, nomeSpettacolo: String, dataScadenza: String): Int {
        val selection = "${DBHelper._ID_BIGLIETTO} = ?"
        val selectionArgs = arrayOf(id_biglietto.toString())

        val values = ContentValues().apply {
            put(DBHelper.NOME_SPETTACOLO, nomeSpettacolo)
            put(DBHelper.DATA_SCADENZA, dataScadenza)
        }
        val n = db.update(DBHelper.TABLE_BIGLIETTI, values, selection, selectionArgs)
        return n
    }

    /**
     * Aggiorna un record esistente nella tabella "Abbonamento" del database.
     *
     * @param id_abbonamento L'ID del record da aggiornare.
     * @param teatro Il nuovo nome del teatro per l'abbonamento.
     * @param dataInizio La nuova data di inizio dell'abbonamento.
     * @param dataFine La nuova data di fine dell'abbonamento.
     * @return Il numero di righe aggiornate nel database.
     */
    fun updateAbbonamento(id_abbonamento: Long, teatro: String, dataInizio: String, dataFine: String): Int {
        val selection = "${DBHelper._ID_ABBONAMENTO} = ?"
        val selectionArgs = arrayOf(id_abbonamento.toString())

        val values = ContentValues().apply {
            put(DBHelper.TEATRO, teatro)
            put(DBHelper.DATA_INIZIO, dataInizio)
            put(DBHelper.DATA_FINE, dataFine)
        }
        val n = db.update(DBHelper.TABLE_ABBONAMENTO, values, selection, selectionArgs)
        return n
    }

    /**
     * Elimina un record dalla tabella "Biglietti" del database.
     *
     * @param id_biglietto L'ID del record da eliminare.
     */
    fun deleteBiglietto(id_biglietto: Long) {
        val selection = "${DBHelper._ID_BIGLIETTO} = ?"
        val selectionArgs = arrayOf(id_biglietto.toString())
        db.delete(DBHelper.TABLE_BIGLIETTI, selection, selectionArgs)
    }
    /**
     * Elimina un record dalla tabella "Abbonamento" del database.
     *
     * @param id_abbonamento L'ID del record da eliminare.
     */
    fun deleteAbbonamento(id_abbonamento: Long) {
        val selection = "${DBHelper._ID_ABBONAMENTO} = ?"
        val selectionArgs = arrayOf(id_abbonamento.toString())
        db.delete(DBHelper.TABLE_ABBONAMENTO, selection, selectionArgs)
    }

    /**
     * Recupera tutti i record dalla tabella "Biglietti" del database.
     *
     * @return Oggetto Cursor che rappresenta i record nel database.
     */
    fun fetchAllBiglietti(): Cursor {
        val projection = arrayOf(DBHelper._ID_BIGLIETTO, DBHelper.NOME_SPETTACOLO, DBHelper.DATA_SCADENZA)
        val cursor = db.query(
            DBHelper.TABLE_BIGLIETTI,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }

    /**
     * Recupera tutti i record dalla tabella "Abbonamento" del database.
     *
     * @return Oggetto Cursor che rappresenta i record nel database.
     */
    fun fetchAllAbbonamenti(): Cursor {
        val projection = arrayOf(DBHelper._ID_ABBONAMENTO, DBHelper.TEATRO, DBHelper.DATA_INIZIO, DBHelper.DATA_FINE)
        val cursor = db.query(
            DBHelper.TABLE_ABBONAMENTO,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }
}
