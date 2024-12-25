package com.example.marketpalcev1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        val userLogin: EditText = findViewById(R.id.user_login_auth);
        val userPass: EditText = findViewById(R.id.user_pass_auth);
        val button: Button = findViewById(R.id.button_auth);
        val linkToReg: TextView = findViewById(R.id.text_auth)

        linkToReg.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java  )
            startActivity(intent)
        }

        button.setOnClickListener{
            val login = userLogin.text.toString().trim();
            val pass = userPass.text.toString().trim(); // trim удаляет пробелы

            if(login == "" || pass == "")
                Toast.makeText(this, "Ошибка: пустое поле", Toast.LENGTH_SHORT).show()
            else{
                val db = DbHelper(this, null)
                val isAuth: Boolean = db.getUser(login, pass)

                if(isAuth){
                    Toast.makeText(this, "Вход совершён !", Toast.LENGTH_SHORT).show()

                    val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE) // сохранение логина авторизовавшегося пользователя
                    val editor = sharedPreferences.edit()
                    editor.putString("user_login", login)
                    editor.apply()

                    val intent = Intent(this, ItemsActivity::class.java)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this, "Ошибка: пользователь не найден / неверный пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
}