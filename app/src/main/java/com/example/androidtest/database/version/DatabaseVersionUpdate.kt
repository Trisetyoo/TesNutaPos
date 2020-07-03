package com.example.androidtest.database.version

interface DatabaseVersionUpdate {
    fun getSqlUpdate(): List<String>
}