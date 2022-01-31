package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CatAdapter(private val cats: List<Cat>) : RecyclerView.Adapter<CatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val rootView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.cat_item, parent, false)
        return CatViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(cats.get(position))
    }
}

class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.textView)

    fun bind(cat: Cat){
        textView.text = cat.text
    }
}