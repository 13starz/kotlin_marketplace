package com.example.marketpalcev1

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class CartAdapter(private var items: List<Item>, private val context: Context, private val reloadItems: () -> Unit) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val desc: TextView = view.findViewById(R.id.item_desc)
        val price: TextView = view.findViewById(R.id.item_price)
        val deleteButton: Button = view.findViewById(R.id.delete_item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_cart_list, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        val imageId = context.resources.getIdentifier(
            item.imageName.substringBefore("."),
            "drawable",
            context.packageName
        )

        if(imageId != 0){
            holder.image.setImageResource(imageId)
        }
        else {
            val imagePath = context.filesDir.absolutePath + "/" + item.imageName;
            holder.image.setImageURI(Uri.fromFile(File(imagePath)));
        }
        holder.desc.text = item.desc
        holder.price.text = item.price.toString() + "$"
        holder.deleteButton.setOnClickListener {

            reloadItems()
            Toast.makeText(context, "Товар удален из корзины", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun updateItems(newItems: List<Item>){
        items = newItems
        notifyDataSetChanged()
    }
}