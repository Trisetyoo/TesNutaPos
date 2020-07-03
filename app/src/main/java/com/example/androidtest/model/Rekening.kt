package com.example.androidtest.model

data class Rekening(
    var namaBank: String,
    var nomorRekening: String,
    var atasNama: String
) {
    constructor() : this("", "", "")
}