package com.example.androidtest.database

import com.example.androidtest.database.version.DatabaseVersionUpdate
import com.example.androidtest.database.version.DatabaseVersionUpdateV2
import com.example.androidtest.database.version.DatabaseVersionUpdateV3
import java.util.*

class DatabaseMaintenance() {
    private val versionMaintainer: MutableMap<Int, DatabaseVersionUpdate> =
        HashMap()

    fun getVersionUpdater(version: Int): DatabaseVersionUpdate? {
        return versionMaintainer[version]
    }

    init {
        versionMaintainer[DatabaseVersionUpdateV2.MAINTAINED_VERSION] = DatabaseVersionUpdateV2()
        versionMaintainer[DatabaseVersionUpdateV3.MAINTAINED_VERSION] = DatabaseVersionUpdateV3()
    }
}