package uz.muhammadziyo.mydictionary.entity



class Word {

    var id:Int?=null
    var name:String?=null
    var translation:String?=null
    var type:Int?=null
    var category:MyCategory?=null
    var imagePath:String?=null

    constructor(id: Int?, name: String?, translation: String?, type: Int?, category: MyCategory, imagePath:String) {
        this.id = id
        this.name = name
        this.translation = translation
        this.type = type
        this.category = category
        this.imagePath = imagePath
    }

    constructor(name: String?, translation: String?, type: Int?, category: MyCategory, imagePath: String) {
        this.name = name
        this.translation = translation
        this.type = type
        this.category = category
        this.imagePath = imagePath
    }

    constructor()


}