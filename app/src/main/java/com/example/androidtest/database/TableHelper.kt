package com.example.androidtest.database

import android.provider.BaseColumns

class TableHelper {
    object UangMasuk {
        const val TABLE_NAME = "uang_masuk"
        const val COLUMN_UANG_MASUK_ID = "uang_masuk_id"
        const val COLUMN_TERIMA_DARI = "terima_dari"
        const val COLUMN_KETERANGAN = "keterangan"
        const val COLUMN_JUMLAH = "jumlah"
        const val COLUMN_TANGGAL = "tanggal"
        const val COLUMN_NOMOR = "nomor"
        const val COLUMN_REKENING_ID = "rekening_id"
    }

    object Rekening {
        const val TABLE_NAME = "rekening"
        const val COLUMN_REKENING_ID = "rekening_id"
        const val COLUMN_NAMA_BANK = "nama_bank"
        const val COLUMN_NOMOR_REKENING = "no_rekening"
        const val COLUMN_ATAS_NAMA = "atas_nama"
    }
}