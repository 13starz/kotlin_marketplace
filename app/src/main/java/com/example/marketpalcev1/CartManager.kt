package com.example.marketpalcev1

object CartManager {
    private val _cartItems = mutableListOf<Item>()
    val cartItems: List<Item>
        get() = _cartItems.toList() // Возвращаем копию для избежания изменений извне

    fun addItem(item: Item) {
        _cartItems.add(item)
    }

    fun removeItem(item: Item) {
        _cartItems.remove(item)
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun getCartItemCount(): Int{
        return _cartItems.count()
    }
}