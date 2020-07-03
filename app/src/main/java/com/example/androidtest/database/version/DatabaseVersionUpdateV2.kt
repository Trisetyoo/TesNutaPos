package com.example.androidtest.database.version

import com.example.androidtest.database.TableHelper
import java.util.*

class DatabaseVersionUpdateV2: DatabaseVersionUpdate {

    companion object {
        const val MAINTAINED_VERSION = 2
    }

    override fun getSqlUpdate(): List<String> {
        val sqlList: MutableList<String> =
            ArrayList()
        sqlList.add(addColumnTanggal())
        sqlList.add(addColumnNomor())
        return sqlList
    }

    private fun addColumnTanggal(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("ALTER TABLE ")
            .append(TableHelper.UangMasuk.TABLE_NAME)
            .append(" ADD COLUMN ")
            .append(TableHelper.UangMasuk.COLUMN_TANGGAL)
            .append(" TEXT;")
        return stringBuilder.toString()
    }

    private fun addColumnNomor(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("ALTER TABLE ")
            .append(TableHelper.UangMasuk.TABLE_NAME)
            .append(" ADD COLUMN ")
            .append(TableHelper.UangMasuk.COLUMN_NOMOR)
            .append(" TEXT;")
        return stringBuilder.toString()
    }
}