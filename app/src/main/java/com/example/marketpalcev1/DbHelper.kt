package com.example.marketpalcev1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLXML
// .CursorFactory (т.е курсор) нужен для выполнения команд в SQLite
class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 1){
    //factory - класс-помощник для подключения к ДБ (? нужен тк может быть null)
    override fun onCreate(db: SQLiteDatabase?) {
        // в onCreate "создаём" всю базу данных
        val query = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT NULL, login TEXT, pass TEXT)"
        // AUTOINCREMENT  - значение столбца будет автоматически увеличиваться при добавлении новой строки
        // PRIMARY KEY задает первичный ключ таблицы. Первичный ключ уникально идентифицирует строку в таблице
        // То есть у нас не может быть в таблице users более одной строки, где в столбце id было бы одно и то же значение.
        // DEFAULT определяет значение по умолчанию для столбца
        // NULL допускает значение 0 для PRIMARY KEY
        // exec - execute. Выполнение(обращение) к БД
        db!!.execSQL(query)
        val createItemsTableQuery =
            "CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, user_login TEXT, desc TEXT, price INTEGER, imageName TEXT)"
        db?.execSQL(createItemsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // в onUpgrade "удаляем" всю БД и повторно ее создаем (пересоздание БД)
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS items")
        // удаляем таблицу если существует таблица users
        onCreate(db)
        //повторно создаем таблицу(теперь пустую)
    }

    // Методы для работы с пользователем
    fun addUser(user: User){
        val values = ContentValues() // объект, в который запишем значения, которые потом добавим в таблицу БД
        values.put("login", user.login) // в ключ "логин" подставляем введенный логин
        values.put("pass", user.pass)

        val db = this.writableDatabase // обращаемся к нашей ДБ (writable - можем чтото записать)
        db.insert("users", null, values)

        db.close()
    }

    fun getUser(login: String, pass: String): Boolean{
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM users WHERE login = ? AND pass = ?", arrayOf(login, pass))
        val userExists = cursor.moveToFirst()
        cursor.close()
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        return userExists
    }

    fun loginIsUsed(login: String): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE login=?", arrayOf(login))
        val loginExists = cursor.moveToFirst()
        cursor.close()
        return loginExists
    }

    // Методы для работы с товарами

    fun addItem(item: Item, userLogin: String) {
        val values = ContentValues()
        values.put("user_login", userLogin)
        values.put("desc", item.desc)
        values.put("price", item.price)
        values.put("imageName", item.imageName)
        val db = this.writableDatabase
        db.insert("items", null, values)
        db.close()
    }

    fun getItems(): ArrayList<Item> {
        val itemsList = arrayListOf<Item>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM items", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val userLogin = cursor.getString(1)
                val desc = cursor.getString(2)
                val price = cursor.getInt(3)
                val imageName = cursor.getString(4)
                val item = Item(id, desc, price, imageName, userLogin)
                itemsList.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemsList
    }
}