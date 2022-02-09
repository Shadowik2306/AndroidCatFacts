package com.example.myapplication

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private var favoriteWindow = true
    private val url = "https://cat-fact.herokuapp.com/facts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRealm()
        if (favoriteWindow) {
            val queue = Volley.newRequestQueue(this)
            getCatsFromServer(queue)
        }
        else {

        }
        FavoriteButtonID.setOnClickListener() {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            favoriteWindow = false
            overridePendingTransition(0, 0);
        }
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun getCatsFromServer(queue: RequestQueue){
        val stringReq = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val catList = parseResponse(response)
                setList(catList)
                saveIntoDb(catList)
            },
            {
                Toast.makeText(this, "Error. Not Found", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringReq)
    }

    private fun saveIntoDb(cats: List<Cat>) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealm(cats)
        realm.commitTransaction()
    }

    private fun loadFromDb(): List<Cat>{
        val realm = Realm.getDefaultInstance()
        return realm.where(Cat::class.java).findAll()
    }

    private fun showListFromDB() {
        val cats = loadFromDb()
        setList(cats)
    }

    private fun parseResponse(responseText: String): List<Cat> {
        val catList: MutableList<Cat> = mutableListOf()
        val jsonArray = JSONArray(responseText)
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            val catText = jsonObject.getString("text")
            val cat = Cat()
            cat.text = catText
            cat.favorite = "false"
            catList.add(cat)
        }
        return catList
    }

    private fun setList(cats: List<Cat>) {
        val adapter = CatAdapter(cats)
        recyclerViewId.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerViewId.layoutManager = layoutManager
    }
}