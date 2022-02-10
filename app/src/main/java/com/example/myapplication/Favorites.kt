package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*

class Favorites : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        initRealm()
        showListFromDB()

        FavoriteButtonID.setOnClickListener() {
            val intent = Intent(this@Favorites, MainActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.apply {
            title = "Избранное"
        }
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun loadFromDb(): List<Cat>{
        val realm = Realm.getDefaultInstance()
        return realm.where(Cat::class.java).equalTo("favorite", "true").findAll()
    }

    private fun showListFromDB() {
        val cats = loadFromDb()
        val unic: MutableSet<String> = mutableSetOf()
        val new_cats: MutableList<Cat> = mutableListOf()
        cats.forEach() {cat
        ->
            if (!unic.contains(cat.text)) {
                unic.add(cat.text)
                new_cats.add(cat)
            }
        }
        setList(new_cats)
    }

    private fun setList(cats: List<Cat>) {
        val adapter = CatAdapter(cats, 2)
        recyclerViewId.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerViewId.layoutManager = layoutManager
    }
}