package com.example.androidtest.activity

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtest.R
import com.example.androidtest.database.AppDatabase
import com.example.androidtest.model.Rekening
import com.example.androidtest.model.UangMasuk
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appDatabase = AppDatabase.getInstance(this)!!

        button_insert_rekening.setOnClickListener {
            val bool = appDatabase.insertRekening(Rekening(text_nama_bank.text.toString(), text_no_rek.text.toString(), text_atas_nama.text.toString()))

            Toast.makeText(this, "Berhasil : $bool", Toast.LENGTH_SHORT).show()

        }

        button_insert_uang.setOnClickListener { insertTable(text_pengirim.text.toString(), text_keterangan.text.toString(), text_jumlah.text.toString().toInt(), text_no_rek_2.text.toString().toInt()) }
    }

    private fun insertTable(terimaDari: String, keterangan: String, jumlah: Int, rekeningId: Int) {
        var nomor = ""
        val uangMasukTerakhir = appDatabase.getUangMasukTerakhir()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val currentDate = dateFormat.format(date)

        nomor = getNomorUangMasuk(currentDate, uangMasukTerakhir)

        try {
            appDatabase.insertUangMasuk(UangMasuk(terimaDari, keterangan, jumlah, currentDate, nomor, rekeningId))
        } catch (ex : SQLiteConstraintException) {
            Toast.makeText(this, "Rekening tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        val latest = appDatabase.getUangMasukTerakhir()
        if (latest != null) {
            Toast.makeText(this, "data : " + latest.tanggal + ", " + latest.nomor, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNomorUangMasuk(currentDate: String, uangMasukTerakhir: UangMasuk?): String {
        val template = "UM/$1/1"
        var nomorFinal = ""

        val dateFormat = SimpleDateFormat("yyMMdd")
        val date = Date()

        nomorFinal = template.replace("$1", dateFormat.format(date))

        if (uangMasukTerakhir != null) {
            if (currentDate == uangMasukTerakhir.tanggal) {
                val letters: String = uangMasukTerakhir.nomor.substring(0, 10)
                val str = uangMasukTerakhir.nomor.split("/")
                val nomor = str[2].toInt()
                nomorFinal = letters + (nomor + 1)
            }
        }

        return nomorFinal
    }
}
