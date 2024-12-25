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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val userLogin: EditText = findViewById(R.id.user_login);
        val userPass: EditText = findViewById(R.id.user_pass);
        val button: Button = findViewById(R.id.button_reg);
        val userbutton: TextView = findViewById(R.id.user_reg);
        val linkToAuth: TextView = findViewById(R.id.text_reg)
        //нашли все поля (куда пользователь вводит какие-либо данные / нажимает на кнопку)

        linkToAuth.setOnClickListener{
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener{
            val login = userLogin.text.toString().trim();
            val pass = userPass.text.toString().trim(); // trim удаляет пробелы
            var isError: Boolean = false
            if(login == "" || pass == ""){
                Toast.makeText(this, "Ошибка: пустое поле", Toast.LENGTH_SHORT).show()
                isError = true
            }
            if(!isError){
                val db2 = DbHelper(this, null)
                if (db2.loginIsUsed(login)){
                    isError = true
                    Toast.makeText(this, "Ошибка: логин занят", Toast.LENGTH_SHORT).show()
                }
            }
            if(!isError){
                val user = User(login, pass)
                val db = DbHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Успешная регистрация!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ItemsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}