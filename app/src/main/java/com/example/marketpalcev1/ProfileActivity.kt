package com.example.marketpalcev1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var userNameTextView: TextView
    private lateinit var userItemsList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userNameTextView = findViewById(R.id.user_name_profile)
        userItemsList = findViewById(R.id.user_items_list)

        // Получаем имя пользователя из SharedPreferences
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val userLogin = sharedPreferences.getString("user_login", "")

        userNameTextView.text = userLogin


        loadUserItems(userLogin)

    }

    private fun loadUserItems(userLogin: String?){
        if(userLogin == null || userLogin.isEmpty()) return;

        val db = DbHelper(this, null)
        val allItems = db.getItems();
        val userItems = allItems.filter { it.userLogin == userLogin }

        userItemsList.layoutManager = LinearLayoutManager(this)
        userItemsList.adapter = ProfileItemsAdapter(userItems, this){
            reloadItems(userLogin)
        }

    }

    private fun reloadItems(userLogin: String?){
        if(userLogin == null || userLogin.isEmpty()) return;

        val db = DbHelper(this, null)
        val allItems = db.getItems();
        val userItems = allItems.filter { it.userLogin == userLogin }

        (userItemsList.adapter as ProfileItemsAdapter).updateItems(userItems)
    }
}