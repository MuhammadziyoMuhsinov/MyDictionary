package uz.muhammadziyo.mydictionary.entity

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper


class MyCategory {
    var id: Int? = null
    var name: String? = null

    constructor(id: Int?, name: String?) {
        this.id = id
        this.name = name
    }

    constructor(name: String?) {
        this.name = name
    }

    constructor()


}