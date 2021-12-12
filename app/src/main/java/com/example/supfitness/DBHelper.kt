package com.example.supfitness;

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.supfitness.data.WeightsData
import com.example.supfitness.data.TracksData
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val weightsQuery = ("CREATE TABLE " + TABLE_WEIGHTS_NAME + " ("
                + WEIGHTS_ID_COL + " INTEGER PRIMARY KEY, " +
                WEIGHTS_WEIGHT_COl + " TEXT," +
                WEIGHTS_DATE_COL + " TEXT" + ")")

        val tracksQuery = ("CREATE TABLE " + TABLE_TRACKS_NAME + " ("
                + TRACKS_ID_COL + " INTEGER PRIMARY KEY, " +
                 TRACKS_LATITUDE_COL + " TEXT," +
                TRACKS_LONGITUDE_COL + " TEXT," +
                TRACKS_RATE_COL + " TEXT," +
                TRACKS_DATE_COL + " TEXT" + ")")

        db.execSQL(weightsQuery)
        db.execSQL(tracksQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHTS_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS_NAME)
        onCreate(db)
    }

    fun addTrack(longitude: String, latitude: String, rate: String) {
        val values = ContentValues()
        val data = getLastTrack()

        if (!data.isEmpty()) {
            val first = data.first()

            if (first.latitude == latitude && first.longitude == longitude) {
                return
            }
        }

        values.put(TRACKS_LATITUDE_COL, latitude)
        values.put(TRACKS_LONGITUDE_COL, longitude)
        values.put(TRACKS_RATE_COL, rate)
        values.put(TRACKS_DATE_COL, getDateTime())

        val db = this.writableDatabase

        db.insert(TABLE_TRACKS_NAME, null, values)
    }

    @SuppressLint("Range")
    fun getLastTrack(): ArrayList<TracksData> {
        val db = this.readableDatabase

        val c = db.rawQuery("SELECT * FROM " + TABLE_TRACKS_NAME + " ORDER BY "+ TRACKS_ID_COL + " DESC" + " LIMIT 1", null)

        val data = ArrayList<TracksData>()
        if (c.moveToFirst()) {
            do {
                data.add(
                    TracksData(
                    c.getInt(c.getColumnIndex(TRACKS_ID_COL)),
                    c.getString(c.getColumnIndex(TRACKS_LONGITUDE_COL)),
                    c.getString(c.getColumnIndex(TRACKS_LATITUDE_COL)),
                    c.getString(c.getColumnIndex(TRACKS_RATE_COL)),
                    c.getString(c.getColumnIndex(TRACKS_DATE_COL))
                )
                )
            } while (c.moveToNext())
        }

        return data
    }

    @SuppressLint("Range")
    fun getTracks(): ArrayList<TracksData> {
        val db = this.readableDatabase

        val c = db.rawQuery("SELECT * FROM " + TABLE_TRACKS_NAME + " ORDER BY "+ TRACKS_ID_COL + " DESC", null)

        val data = ArrayList<TracksData>()
        if (c.moveToFirst()) {
            do {
                data.add(
                    TracksData(
                    c.getInt(c.getColumnIndex(TRACKS_ID_COL)),
                    c.getString(c.getColumnIndex(TRACKS_LONGITUDE_COL)),
                    c.getString(c.getColumnIndex(TRACKS_LATITUDE_COL)),
                    c.getString(c.getColumnIndex(TRACKS_RATE_COL)),
                    c.getString(c.getColumnIndex(TRACKS_DATE_COL))
                )
                )
            } while (c.moveToNext())
        }

        return data
    }

    fun addWeight(weight: String) {
        val values = ContentValues()

        values.put(WEIGHTS_WEIGHT_COl, weight)
        values.put(WEIGHTS_DATE_COL, getDateTime())

        val db = this.writableDatabase

        db.insert(TABLE_WEIGHTS_NAME, null, values)
    }

    @SuppressLint("Range")
    fun getWeights(direction: String): ArrayList<WeightsData> {
        val db = this.readableDatabase

        val c = db.rawQuery("SELECT * FROM " + TABLE_WEIGHTS_NAME + " ORDER BY "+ WEIGHTS_ID_COL + " " + direction, null)

        val data = ArrayList<WeightsData>()
        if (c.moveToFirst()) {
            do {
                data.add(
                    WeightsData(
                    c.getInt(c.getColumnIndex(WEIGHTS_ID_COL)),
                    c.getString(c.getColumnIndex(WEIGHTS_WEIGHT_COl)),
                    c.getString(c.getColumnIndex(WEIGHTS_DATE_COL))
                )
                )
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
            "yyyy-MM-dd", Locale.getDefault() // default yyyy-MM-dd HH:mm:ss
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

        val TABLE_TRACKS_NAME = "tracks"
        val TRACKS_ID_COL = "id"
        val TRACKS_LONGITUDE_COL = "longitude"
        val TRACKS_LATITUDE_COL = "latitude"
        val TRACKS_RATE_COL = "rate"
        val TRACKS_DATE_COL = "date"
    }
}