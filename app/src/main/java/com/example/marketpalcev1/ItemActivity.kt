package com.example.marketpalcev1

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import kotlin.text.substringBefore


class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)                                              {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val image: ImageView = findViewById(R.id.in_item_image)
        val desc: TextView = findViewById(R.id.item_desc)
        val but: Button = findViewById(R.id.activity_item_button)

        desc.text = intent.getStringExtra("itemDesc")
        but.text = intent.getStringExtra("itemPrice")
        val imageId = intent.getIntExtra("imageId",0)
        val imagePath = intent.getStringExtra("imagePath")
        val imageName = intent.getStringExtra("imageName")

        if(imageId != 0){
            image.setImageResource(imageId)
        }
        else if (imagePath != null){
            image.setImageURI(Uri.fromFile(File(imagePath)))
        }

        but.setOnClickListener{
            val itemDesc = intent.getStringExtra("itemDesc")
            val itemPrice = intent.getStringExtra("itemPrice")?.replace("$","")?.toIntOrNull() ?: 0
            val itemName = intent.getStringExtra("imageName")
            val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
            val userLogin = sharedPreferences.getString("user_login", "") ?: ""
            val itemToAdd = Item(0, itemDesc!!, itemPrice, itemName!!, userLogin )
            CartManager.addItem(itemToAdd)
            Toast.makeText(this, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show()
        }
    }


}