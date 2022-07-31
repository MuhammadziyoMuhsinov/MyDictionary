package uz.muhammadziyo.mydictionary.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.*
import androidx.room.Dao
import androidx.sqlite.db.SupportSQLiteOpenHelper
import uz.muhammadziyo.mydictionary.entity.MyCategory
import uz.muhammadziyo.mydictionary.entity.Word

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1),
    uz.muhammadziyo.mydictionary.db.Dao {

    companion object {

        const val DB_NAME = "my_dictionary"

        const val TABLE_CATEGORY = "category"
        const val ID_CATEGORY = "id"
        const val NAME_CATEGORY = "name"

        const val TABLE_WORD = "word"
        const val ID_WORD = "id"
        const val NAME_WORD = "word"
        const val TRANSLATION_WORD = "translation"
        const val TYPE_WORD = "type"
        const val CATEGORY_ID = "category_id"
        const val IMAGE_PATH_WORD = "image_path"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createCategory =
            "CREATE TABLE $TABLE_CATEGORY ($ID_CATEGORY INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,$NAME_CATEGORY TEXT NOT NULL)"

        val createWord =
            "CREATE TABLE $TABLE_WORD ($ID_WORD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,$NAME_WORD TEXT NOT NULL, $TRANSLATION_WORD TEXT NOT NULL, $TYPE_WORD INTEGER NOT NULL, $CATEGORY_ID INTEGER NOT NULL,$IMAGE_PATH_WORD TEXT NOT NULL, FOREIGN KEY ($CATEGORY_ID) REFERENCES $TABLE_CATEGORY ($ID_CATEGORY) )"


        db?.execSQL(createCategory)
        db?.execSQL(createWord)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun addCategory(myCategory: MyCategory) {
        val database = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(NAME_CATEGORY, myCategory.name)
        database.insert(TABLE_CATEGORY, null, contentValue)
        database.close()
    }

    override fun addWord(word: Word) {
        val database = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(NAME_WORD, word.name)
        contentValue.put(TRANSLATION_WORD, word.translation)
        contentValue.put(TYPE_WORD, word.type)
        contentValue.put(CATEGORY_ID, word.category?.id)
        contentValue.put(IMAGE_PATH_WORD,word.imagePath)
        database.insert(TABLE_WORD, null, contentValue)
        database.close()
    }

    override fun getAllCategory(): List<MyCategory> {
        val list = ArrayList<MyCategory>()
        val query = "select * from $TABLE_CATEGORY"
        val database = this.readableDatabase
        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val category = MyCategory(
                    cursor.getInt(0),
                    cursor.getString(1)
                )
                list.add(category)
            } while (cursor.moveToNext())
        }

        return list
    }

    override fun getAllWord(): List<Word> {
        val list = ArrayList<Word>()
        val query = "select * from $TABLE_WORD"
        val database = this.readableDatabase
        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val word = Word(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    getCategoryById(cursor.getInt(4)),
                    cursor.getString(5)
                )
                list.add(word)
            } while (cursor.moveToNext())
        }

        return list
    }

    override fun deleteCategory(myCategory: MyCategory) {
        val database = this.writableDatabase
        database.delete(TABLE_CATEGORY, "$ID_CATEGORY = ?", arrayOf(myCategory.id.toString()))
        database.close()
    }

    override fun deleteWord(word: Word) {
        val database = this.writableDatabase
        database.delete(TABLE_WORD, "$ID_WORD = ?", arrayOf(word.id.toString()))
        database.close()
    }

    override fun updateCategory(myCategory: MyCategory): Int {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID_CATEGORY,myCategory.id)
        contentValues.put(NAME_CATEGORY, myCategory.name)

        return database.update(TABLE_CATEGORY, contentValues, "$ID_CATEGORY = ?", arrayOf(myCategory.id.toString()))
    }

    override fun updateWord(word: Word): Int {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID_WORD,word.id)
        contentValues.put(NAME_WORD, word.name)
        contentValues.put(TRANSLATION_WORD, word.translation)
        contentValues.put(TYPE_WORD, word.type)
        contentValues.put(CATEGORY_ID, word.category?.id)
        contentValues.put(IMAGE_PATH_WORD, word.imagePath)
        return database.update(TABLE_WORD, contentValues, "$ID_WORD = ?", arrayOf(word.id.toString()))
    }

    override fun getCategoryById(id: Int): MyCategory {
        val database = this.readableDatabase
        val cursor = database.query(
            TABLE_CATEGORY,
            arrayOf(ID_CATEGORY, NAME_CATEGORY),
            "$ID_CATEGORY = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        cursor.moveToFirst()
        val category = MyCategory(
            cursor.getInt(0),
            cursor.getString(1)
        )

        return category
    }

}