package com.rafahcf.comicapp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rafahcf.comicapp.Models.ItemCredits
import com.rafahcf.comicapp.R
import com.squareup.picasso.Picasso

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var itemCredits: MutableList<ItemCredits>  = ArrayList()
    lateinit var context: Context

    fun RecyclerAdapter(itemCredits : MutableList<ItemCredits>, context: Context){
        this.itemCredits = itemCredits
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemCredits.get(position)
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_credits_list, parent, false))
    }

    override fun getItemCount(): Int {
        return itemCredits.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //getting imgageview and textview of item
        val imageViewItemCreditImage = view.findViewById(R.id.imageViewItemCreditImage) as ImageView
        val textViewItemCreditName = view.findViewById(R.id.textViewItemCreditName) as TextView

        fun bind(itemCredits: ItemCredits, context: Context){
            imageViewItemCreditImage.loadUrl(itemCredits.image)
            textViewItemCreditName.text = itemCredits.name
            itemView.setOnClickListener(View.OnClickListener {
                //do something when click on it
            })
        }

        fun ImageView.loadUrl(url: String) {
            //loading image from internet
            Picasso.get().load(url).into(this)
        }
    }
}