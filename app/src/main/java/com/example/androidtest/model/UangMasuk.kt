package com.example.androidtest.model

data class UangMasuk(
    var terimaDari: String,
    var keterangan: String,
    var jumlah: Int,
    var tanggal: String,
    var nomor: String,
    var rekeningId: Int
) {
    constructor() : this("", "", 0, "", "", 0)
}