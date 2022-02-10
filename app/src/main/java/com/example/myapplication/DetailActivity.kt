package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.cat_item.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    companion object {
        const val CAT_FACT_TEXT_TAG = "com.example.myapplication.cat_fact_text_tag"
    }

    private val urlImage = "https://aws.random.cat/meow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initRealm()
        val queue = Volley.newRequestQueue(this)
        getImageFromServer(queue)
        setupActionBar()
        setText()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun getImageFromServer(queue: RequestQueue) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            urlImage,
            { response ->
                takeImageUrl(response)
            },
            {
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)
    }

    private fun takeImageUrl(json: String) {
        val file = JSONObject(json)
        val urlfile = file.getString("file")
        Glide.with(this).load(urlfile).into(imageView)
    }


    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)

            title = "Подробнее"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setText() {
        val text = intent?.extras?.getString(CAT_FACT_TEXT_TAG)
        textView2id.text = text
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        var change = realm.where(Cat::class.java).equalTo("text", text as String).findFirst()
        if (change?.favorite == "true") {
            buttonId.text = "Удалить из избранного"
        }
        else {
            buttonId.text = "Добавить в избранное"
        }
        realm.commitTransaction()

        buttonId.setOnClickListener() {
            realm.beginTransaction()
            change = realm.where(Cat::class.java).equalTo("text", text as String).findFirst()
            if (change?.favorite == "true") {
                buttonId.text = "Добавить в избранное"
                change!!.favorite = "false"
            }
            else {
                buttonId.text = "Удалить из избранного"
                change?.favorite = "true"
            }
            realm.commitTransaction()
        }

    }
}