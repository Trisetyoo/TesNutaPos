package com.example.androidtest.database.version

import com.example.androidtest.database.TableHelper
import java.util.*

class DatabaseVersionUpdateV3: DatabaseVersionUpdate {

    companion object {
        const val MAINTAINED_VERSION = 3
    }

    override fun getSqlUpdate(): List<String> {
        val sqlList: MutableList<String> =
            ArrayList()

        sqlList.add("CREATE TABLE " + TableHelper.Rekening.TABLE_NAME + "(" + TableHelper.Rekening.COLUMN_REKENING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TableHelper.Rekening.COLUMN_NAMA_BANK + " TEXT NOT NULL, "
                + TableHelper.Rekening.COLUMN_NOMOR_REKENING + " TEXT NOT NULL, "
                + TableHelper.Rekening.COLUMN_ATAS_NAMA + " TEXT) ")

        sqlList.add("ALTER TABLE " + TableHelper.UangMasuk.TABLE_NAME + " ADD COLUMN " + TableHelper.UangMasuk.COLUMN_REKENING_ID + " INTEGER REFERENCES "
                + TableHelper.Rekening.TABLE_NAME + "(" + TableHelper.Rekening.COLUMN_REKENING_ID + ")")

        return sqlList
    }

}