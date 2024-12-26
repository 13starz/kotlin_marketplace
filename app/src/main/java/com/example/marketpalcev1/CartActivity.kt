package com.example.marketpalcev1

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalPriceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        totalPriceTextView = findViewById(R.id.cartTotalPrice)
        loadCartItems()
    }

    private fun loadCartItems() {
        val cartItems = CartManager.cartItems.toMutableList()
        cartAdapter = CartAdapter(cartItems, this){
            reloadItems()
        }
        cartRecyclerView.adapter = cartAdapter
        updateTotalPrice()
    }

    private fun reloadItems() {
        val cartItems = CartManager.cartItems
        cartAdapter.updateItems(cartItems)
        updateTotalPrice()
    }
    private fun calculateTotalPrice(): Int {
        var totalPrice = 0
        for (cartItem in CartManager.cartItems) {
            totalPrice += cartItem.price
        }
        return totalPrice
    }
    private fun updateTotalPrice() {
        val totalPrice = calculateTotalPrice()
        totalPriceTextView.text = "Total: ${totalPrice}$"
    }
}