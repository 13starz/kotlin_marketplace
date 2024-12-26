package com.example.marketpalcev1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemsActivity : AppCompatActivity() {
    private lateinit var itemsList: RecyclerView
    private lateinit var addButton: Button
    private lateinit var profileButton: Button
    private lateinit var cartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_items)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets

        itemsList = findViewById(R.id.itemsList)
        addButton = findViewById(R.id.add_item_button);
        profileButton = findViewById(R.id.button_3)
        cartButton = findViewById(R.id.button_1)

        addButton.setOnClickListener{
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        cartButton.setOnClickListener{
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        loadItems()
    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }

    private fun loadItems(){
        val db = DbHelper(this, null)
        val items = db.getItems()

        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = ItemsAdapter(items, this)

        updateCartButton()
    }

    private fun updateCartButton(){
        val cartItemCount = CartManager.getCartItemCount()
        cartButton.text = if (cartItemCount > 0) "Корзина ($cartItemCount)" else "Корзина"
    }
}