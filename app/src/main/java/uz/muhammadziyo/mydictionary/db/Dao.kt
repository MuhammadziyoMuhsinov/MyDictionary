package uz.muhammadziyo.mydictionary.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.muhammadziyo.mydictionary.entity.MyCategory
import uz.muhammadziyo.mydictionary.entity.Word
import java.util.*


interface Dao {


    fun addCategory(myCategory: MyCategory)
    fun addWord(word: Word)

    fun getAllCategory():List<MyCategory>
    fun getAllWord():List<Word>

    fun deleteCategory(myCategory: MyCategory)
    fun deleteWord(word: Word)

    fun updateCategory(myCategory: MyCategory) : Int
    fun updateWord(word: Word) : Int

    fun getCategoryById(id:Int): MyCategory

}