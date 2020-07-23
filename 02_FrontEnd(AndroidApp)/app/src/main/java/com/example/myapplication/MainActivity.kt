package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IVolleyHttpRequest {

    var backendIP = "XXX.XXX.X.XXX" //TODO: change to your IP
    var backendPort = "8080" //default port of Spring-boot
    var backendURL = "http://$backendIP:$backendPort/"

    override fun onResponse(response: String){
        val myTextView = findViewById<TextView>(R.id.textView)
        myTextView.text = ""+response
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_get_request.setOnClickListener {
            RESTVolleyRequest.getInstance(this@MainActivity, this@MainActivity)
                .getRequest(backendURL + "get/")
        }

        btn_post_request.setOnClickListener {
            RESTVolleyRequest.getInstance(this@MainActivity, this@MainActivity)
                .postRequest(backendURL + "post/")
        }
    }
}
