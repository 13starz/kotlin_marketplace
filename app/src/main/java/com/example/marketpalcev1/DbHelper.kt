package com.example.marketpalcev1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLXML
// .CursorFactory (т.е курсор) нужен для выполнения команд в SQLite
class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 1){
    //factory - класс-помощник для подключения к ДБ (? нужен тк может быть null)
    override fun onCreate(db: SQLiteDatabase?) {
        // в onCreate "создаём" всю базу данных
        val query = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT NULL, " +
                "login TEXT, " +
                "email TEXT, " +
                "pass TEXT)"
        // AUTOINCREMENT  - значение столбца будет автоматически увеличиваться при добавлении новой строки
        // PRIMARY KEY задает первичный ключ таблицы. Первичный ключ уникально идентифицирует строку в таблице
        // То есть у нас не может быть в таблице users более одной строки, где в столбце id было бы одно и то же значение.
        // DEFAULT определяет значение по умолчанию для столбца
        // NULL допускает значение 0 для PRIMARY KEY
        // exec - execute. Выполнение(обращение) к БД
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // в onUpgrade "удаляем" всю БД и повторно ее создаем (пересоздание БД)
        db!!.execSQL("DROP TABLE IF EXISTS users")
        // удаляем таблицу если существует таблица users
        onCreate(db)
        //повторно создаем таблицу(теперь пустую)
    }

    fun addUser(user: User){
        val values = ContentValues() // объект, в который запишем значения, которые потом добавим в таблицу БД
        values.put("login", user.login) // в ключ "логин" подставляем введенный логин
        values.put("email", user.email)
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

    fun emailIsUsed(email: String): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email=?", arrayOf(email))
        val emailExists = cursor.moveToFirst()
        cursor.close()
        return emailExists
    }

    fun loginIsUsed(login: String): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE login=?", arrayOf(login))
        val loginExists = cursor.moveToFirst()
        cursor.close()
        return loginExists
    }

}