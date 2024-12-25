package com.example.marketpalcev1

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ProfileItemsAdapter(var items: List<Item>, var context: Context, private val reloadItems: () -> Unit) :
    RecyclerView.Adapter<ProfileItemsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val deleteButton: Button = view.findViewById(R.id.delete_item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_profile_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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


        holder.deleteButton.setOnClickListener{
            val db = DbHelper(context, null)
            db.deleteItem(item.id)
            reloadItems()
            Toast.makeText(context, "Товар удален", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateItems(newItems: List<Item>){
        items = newItems
        notifyDataSetChanged()
    }
}