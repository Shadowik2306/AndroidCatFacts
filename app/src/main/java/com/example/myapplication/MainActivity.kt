package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private val url = "https://cat-fact.herokuapp.com/facts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("TAG", "HelloWorld")

        val queue = Volley.newRequestQueue(this)
        getCatsFromServer(queue)
    }

    private fun getCatsFromServer(queue: RequestQueue){
        val stringReq = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val catList = parseResponse(response)
                setList(catList)
            },
            {
                Toast.makeText(this, "Error. Not Found", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringReq)
    }

    private fun parseResponse(responseText: String): List<Cat> {
        val catList: MutableList<Cat> = mutableListOf()
        val jsonArray = JSONArray(responseText)
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            val catText = jsonObject.getString("text")
            val cat = Cat(catText)
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