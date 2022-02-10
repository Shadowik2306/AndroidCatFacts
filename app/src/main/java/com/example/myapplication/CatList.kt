package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.Button
import android.widget.ImageView
import android.widget.ListPopupWindow
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailActivity.Companion.CAT_FACT_TEXT_TAG
import io.realm.Realm
import io.realm.RealmConfiguration


class CatAdapter(private val cats: List<Cat>, private val windowId: Int) : RecyclerView.Adapter<CatViewHolder>() {
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
        holder.bind(cats.get(position), windowId)
    }
}

class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.textView)
    private val favorite: Button = itemView.findViewById(R.id.FavoriteId)

    fun bind(cat: Cat, windowId: Int){
        textView.text = cat.text
        textView.setOnClickListener() {
            openDetailActivity(itemView.context, cat)
        }
        if (cat.favorite == "true") {
            favorite.text = "Удалить из избранного"
        }else {
            favorite.text = "Добавить в избранное"
        }
        favorite.setOnClickListener() {
            initRealm()
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val change = realm.where(Cat::class.java).equalTo("text", textView.text as String).findFirst()
            if (change != null) {
                Log.i("debug", change.favorite)
                if (change.favorite == "true") {
                    change.favorite = "false"
                    favorite.text = "Добавить в избранное"
                    if (windowId == 2) {
                        Log.i("debug", "Its work")
                        itemView.isVisible = false
                    }
                }
                else {
                    change.favorite = "true"
                    favorite.text = "Удалить из избранного"
                }
            }

            realm.commitTransaction()
        }
    }

    private fun initRealm() {
        Realm.init(itemView.context)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun openDetailActivity(context: Context, cat: Cat){
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(CAT_FACT_TEXT_TAG, cat.text)
        context.startActivities(arrayOf(intent))

    }
}

