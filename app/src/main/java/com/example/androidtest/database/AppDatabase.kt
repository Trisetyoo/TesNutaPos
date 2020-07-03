package com.example.androidtest.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.androidtest.database.version.DatabaseVersionUpdateV2
import com.example.androidtest.database.version.DatabaseVersionUpdateV3
import com.example.androidtest.model.Rekening
import com.example.androidtest.model.UangMasuk
import java.util.*

class AppDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = DatabaseVersionUpdateV3.MAINTAINED_VERSION
        private val DATABASE_NAME = "main.db"

        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = AppDatabase(context)
                }
            }
            return INSTANCE
        }

        fun clear() {
            INSTANCE = null
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TableHelper.UangMasuk.TABLE_NAME + "(" + TableHelper.UangMasuk.COLUMN_UANG_MASUK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TableHelper.UangMasuk.COLUMN_TERIMA_DARI + " TEXT NOT NULL, "
                    + TableHelper.UangMasuk.COLUMN_KETERANGAN + " TEXT, "
                    + TableHelper.UangMasuk.COLUMN_JUMLAH + " INTEGER, "
                    + TableHelper.UangMasuk.COLUMN_TANGGAL + " TEXT, "
                    + TableHelper.UangMasuk.COLUMN_NOMOR + " TEXT) "
                    + TableHelper.UangMasuk.COLUMN_REKENING_ID + " INTEGER)"
                    + "FOREIGN KEY (" + TableHelper.UangMasuk.COLUMN_REKENING_ID + ") "
                    + "REFERENCES " + TableHelper.Rekening.TABLE_NAME
                    + " (" + TableHelper.Rekening.COLUMN_REKENING_ID + ")"
                    + ")"
        )

        db.execSQL(
            "CREATE TABLE " + TableHelper.Rekening.TABLE_NAME + "(" + TableHelper.Rekening.COLUMN_REKENING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TableHelper.Rekening.COLUMN_NAMA_BANK + " TEXT NOT NULL, "
                    + TableHelper.Rekening.COLUMN_NOMOR_REKENING + " TEXT NOT NULL, "
                    + TableHelper.Rekening.COLUMN_ATAS_NAMA + " TEXT) "
        )
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        db?.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == newVersion) {
            return
        }
        runUpdater(db, oldVersion, newVersion)
    }

    private fun runUpdater(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val databaseMaintenance = DatabaseMaintenance()
        val startUpdateVersion = oldVersion + 1

        for (i in startUpdateVersion..newVersion) {
            val sqlList: List<String> =
                databaseMaintenance.getVersionUpdater(i)!!.getSqlUpdate()
            doUpdate(db, sqlList)
        }
    }

    private fun doUpdate(db: SQLiteDatabase, sqlList: List<String>) {
        for (sql in sqlList) {
            db.execSQL(sql)
        }
    }

    fun insertUangMasuk(uangMasuk: UangMasuk) {
        val values = ContentValues()
        values.put(TableHelper.UangMasuk.COLUMN_TERIMA_DARI, uangMasuk.terimaDari)
        values.put(TableHelper.UangMasuk.COLUMN_KETERANGAN, uangMasuk.keterangan)
        values.put(TableHelper.UangMasuk.COLUMN_JUMLAH, uangMasuk.jumlah)
        values.put(TableHelper.UangMasuk.COLUMN_TANGGAL, uangMasuk.tanggal)
        values.put(TableHelper.UangMasuk.COLUMN_NOMOR, uangMasuk.nomor)
        values.put(TableHelper.UangMasuk.COLUMN_REKENING_ID, uangMasuk.rekeningId)

        writableDatabase.insertOrThrow(TableHelper.UangMasuk.TABLE_NAME, null, values)
    }

    fun insertRekening(rekening: Rekening): Boolean {
        val values = ContentValues()
        values.put(TableHelper.Rekening.COLUMN_NAMA_BANK, rekening.namaBank)
        values.put(TableHelper.Rekening.COLUMN_NOMOR_REKENING, rekening.nomorRekening)
        values.put(TableHelper.Rekening.COLUMN_ATAS_NAMA, rekening.atasNama)

        return writableDatabase.insert(TableHelper.Rekening.TABLE_NAME, null, values) > 0
    }

    fun getSemuaUangMasuk(): MutableList<UangMasuk>? {
        val cursor = readableDatabase.rawQuery("SELECT * FROM " + TableHelper.UangMasuk.TABLE_NAME, null)

        val result: MutableList<UangMasuk> = ArrayList()

        val rowExist = cursor.moveToFirst()
        if (rowExist) {
            do {
                result.add(
                    buildUangMasukFromCursor(cursor)
                )
            } while (cursor.moveToNext())
        }
        return result
    }

    fun getUangMasukTerakhir(): UangMasuk? {
        val query = "SELECT * FROM " + TableHelper.UangMasuk.TABLE_NAME + " ORDER BY " + TableHelper.UangMasuk.COLUMN_UANG_MASUK_ID + " DESC LIMIT 1"

        val cursor = readableDatabase.rawQuery(query, null)


        return try {
            if (cursor.moveToFirst()) {
                buildUangMasukFromCursor(cursor)
            } else {
                null
            }
        } finally {
            cursor.close()
        }
    }

    private fun buildUangMasukFromCursor(cursor: Cursor?): UangMasuk {
        val terimaDari = cursor!!.getString(cursor.getColumnIndexOrThrow(TableHelper.UangMasuk.COLUMN_TERIMA_DARI))
        val keterangan = cursor.getString(cursor.getColumnIndexOrThrow(TableHelper.UangMasuk.COLUMN_KETERANGAN))
        val jumlah = cursor.getInt(cursor.getColumnIndexOrThrow(TableHelper.UangMasuk.COLUMN_JUMLAH))
        val tanggal = cursor.getString(cursor.getColumnIndexOrThrow(TableHelper.UangMasuk.COLUMN_TANGGAL))
        val nomor = cursor.getString(cursor.getColumnIndexOrThrow(TableHelper.UangMasuk.COLUMN_NOMOR))
        val rekeningId = cursor.getInt(cursor.getColumnIndexOrThrow(TableHelper.UangMasuk.COLUMN_REKENING_ID))

        return UangMasuk(terimaDari, keterangan, jumlah, tanggal, nomor, rekeningId)
    }

}