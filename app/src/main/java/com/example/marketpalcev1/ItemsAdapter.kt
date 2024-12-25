package com.example.marketpalcev1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(var items: List<Item>, var context: Context) :
    RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.item_list_image)
        //val desc: TextView = view.findViewById(R.id.item_desc)
        //val price: TextView = view.findViewById(R.id.item_price)
        val btn: Button = view.findViewById(R.id.item_list_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val imageId = context.resources.getIdentifier(
            items[position].imageName,
            "drawable",
            context.packageName
        )

        holder.image.setImageResource(imageId)

        holder.btn.setOnClickListener{
            val intent = Intent(context, ItemActivity::class.java)

            //передаем значения по ключу в другой класс
            intent.putExtra("itemDesc", items[position].desc)
            intent.putExtra("itemPrice", items[position].price.toString() + "$")
            intent.putExtra("imageId", imageId)
            context.startActivity(intent)
        }
    }

}