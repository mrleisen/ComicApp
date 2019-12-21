package com.rafahcf.comicapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.rafahcf.comicapp.Models.Comic
import com.rafahcf.comicapp.R
import com.squareup.picasso.Picasso

public class GridViewAdapter(context: Context, resource: Int, objects: List<Comic>) :
    ArrayAdapter<Comic>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var v = convertView

        if( v == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.grid_item, null)
        }

        val comic = getItem(position)
        val imageView_comicImage = v!!.findViewById<ImageView>(R.id.imageView_comicImage)
        val textView_dateAdded = v.findViewById<TextView>(R.id.textView_dateAdded)
        val textView_nameIssueNumber = v.findViewById<TextView>(R.id.textView_nameIssueNumber)

        if(comic!!.name == "null"){
            comic.name = "untitled"
        }
        Picasso.get().load(comic.image).into(imageView_comicImage)
        textView_nameIssueNumber.text = "${comic.name} #${comic.issue_number}"
        textView_dateAdded.text = comic.date_added_refactor()

        return v
    }

}