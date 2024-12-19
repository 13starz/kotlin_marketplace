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
//        val userEmail: EditText = findViewById(R.id.user_email);
        val userPass: EditText = findViewById(R.id.user_pass);
        val button: Button = findViewById(R.id.button_reg);
        val linkToAuth: TextView = findViewById(R.id.text_reg)
        //нашли все поля (куда пользователь вводит какие-либо данные / нажимает на кнопку)

        linkToAuth.setOnClickListener{
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener{
            val login = userLogin.text.toString().trim();
//            val email = userEmail.text.toString().trim();
            val pass = userPass.text.toString().trim(); // trim удаляет пробелы
            var mistakesCounter: Int = 0
//            if(login == "" || email == "" || pass == ""){
            if(login == "" || pass == ""){
                Toast.makeText(this, "Ошибка: пустое поле", Toast.LENGTH_SHORT).show()
                mistakesCounter++
            }
            if(mistakesCounter == 0){
//                val db1 = DbHelper(this, null)
//                if (db1.emailIsUsed(email)){
//                    mistakesCounter=1
//                    Toast.makeText(this, "Ошибка: email занят", Toast.LENGTH_SHORT).show()
//                }
                val db2 = DbHelper(this, null)
                if (db2.loginIsUsed(login)){
                    mistakesCounter=1
                    Toast.makeText(this, "Ошибка: логин занят", Toast.LENGTH_SHORT).show()
                }
            }
            if(mistakesCounter == 0){
//              val user = User(login, email, pass)
                val user = User(login,"nonemail", pass)

                val db = DbHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Успешная регистрация!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ItemsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}