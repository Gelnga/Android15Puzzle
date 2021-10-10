package com.example.a15puzzle

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class GameRepository(private val context: Context) {
    private lateinit var dbHelper: GameDbHelper
    private lateinit var db: SQLiteDatabase

    fun open(): GameRepository {
        dbHelper = GameDbHelper(context)
        db = dbHelper.writableDatabase
        return this
    }

    fun close() {
        dbHelper.close()
    }

    fun deleteSaves() {
        dbHelper.deleteSaves(db)
    }

    fun saveGame(gameName: String, gameState: String) {
        val contentValues = ContentValues()
        val stf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH)

        contentValues.put(GameDbHelper.SAVED_STATE, gameState)
        contentValues.put(GameDbHelper.SAVE_DATE, stf.format(Calendar.getInstance().time).toString())
        contentValues.put(GameDbHelper.GAME_NAME, gameName)
        db.insert(GameDbHelper.GAME_SAVES_TABLE_NAME, null, contentValues)
    }

    fun getSavedGames(): Map<String, String> {
        val cursor = db.query(GameDbHelper.GAME_SAVES_TABLE_NAME,
            arrayOf(GameDbHelper.SAVED_STATE, GameDbHelper.GAME_NAME, GameDbHelper.SAVE_DATE),
        null, null, null, null, GameDbHelper.SAVED_GAME_ID)

        val saves = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            saves["Save name : " + cursor.getString(1) + "\n" + "Date added: " + cursor.getString(2)] =
                cursor.getString(0) + ";" + cursor.getString(1) + ";" + cursor.getString(2)
        }

        cursor.close()

        return saves
    }

}