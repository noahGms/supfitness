package com.example.supfitness;

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_WEIGHTS_NAME + " ("
                + WEIGHTS_ID_COL + " INTEGER PRIMARY KEY, " +
                WEIGHTS_WEIGHT_COl + " TEXT," +
                WEIGHTS_DATE_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHTS_NAME)
        onCreate(db)
    }

    fun addWeight(weight: String) {
        val values = ContentValues()

        values.put(WEIGHTS_WEIGHT_COl, weight)
        values.put(WEIGHTS_DATE_COL, getDateTime())

        val db = this.writableDatabase

        db.insert(TABLE_WEIGHTS_NAME, null, values)
    }

    @SuppressLint("Range")
    fun getWeights(): ArrayList<ItemsViewModel> {
        val db = this.readableDatabase

        val c = db.rawQuery("SELECT * FROM " + TABLE_WEIGHTS_NAME + " ORDER BY "+ WEIGHTS_ID_COL + " DESC", null)

        val data = ArrayList<ItemsViewModel>()
        if (c.moveToFirst()) {
            do {
                data.add(ItemsViewModel(
                    c.getInt(c.getColumnIndex(WEIGHTS_ID_COL)),
                    c.getString(c.getColumnIndex(WEIGHTS_WEIGHT_COl)),
                    c.getString(c.getColumnIndex(WEIGHTS_DATE_COL))
                ))
            } while (c.moveToNext())
        }

        return data
    }

    fun deleteWeight(id: Int) {
        val db = this.readableDatabase
        var idStr:String = id.toString()

        db.delete(TABLE_WEIGHTS_NAME, WEIGHTS_ID_COL + " =?", arrayOf(idStr))
    }

    fun closeDB() {
        val db = this.readableDatabase
        if (db != null && db.isOpen) db.close()
    }

    private fun getDateTime(): String? {
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
        )
        val date = Date()
        return dateFormat.format(date)
    }

    companion object {
        private val DATABASE_NAME = "supfitness"
        private val DATABASE_VERSION = 1
        val TABLE_WEIGHTS_NAME = "weights"
        val WEIGHTS_ID_COL = "id"
        val WEIGHTS_WEIGHT_COl = "weight"
        val WEIGHTS_DATE_COL = "date"
    }
}