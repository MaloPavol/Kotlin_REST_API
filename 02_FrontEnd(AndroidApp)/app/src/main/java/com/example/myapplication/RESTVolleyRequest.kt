package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RESTVolleyRequest {
    private var mRequestQueue: RequestQueue?=null
    private var context: Context?=null
    private var iVolleyHttpRequest: IVolleyHttpRequest?=null
    var imageLoader: ImageLoader?=null
        private set

    val requestQueue:RequestQueue
        get (){
            if(mRequestQueue == null)
                mRequestQueue = Volley.newRequestQueue(context!!.applicationContext)
            return mRequestQueue!!
        }

    private constructor(context:Context, iVolleyHttpRequest:IVolleyHttpRequest){
        this.context = context
        this.iVolleyHttpRequest = iVolleyHttpRequest
        mRequestQueue = requestQueue
        this.imageLoader = ImageLoader(mRequestQueue, object:ImageLoader.ImageCache{
            private val mCache = LruCache<String,Bitmap>(10)
            override fun getBitmap(url: String?): Bitmap? {
                return mCache.get(url!!)
            }

            override fun putBitmap(url: String?, bitmap: Bitmap?) {
                mCache.put(url!!, bitmap!!)
            }

        })
    }

    private constructor(context:Context)
    {
        this.context = context
        mRequestQueue = requestQueue
        this.imageLoader = ImageLoader(mRequestQueue, object:ImageLoader.ImageCache{
            private val mCache = LruCache<String,Bitmap>(10)
            override fun getBitmap(url: String): Bitmap? {
                return mCache.get(url)
            }
            override fun putBitmap(url: String, bitmap: Bitmap) {
                mCache.put(url, bitmap)
            }
        })
    }

    fun <T> addToRequestQueue(req: Request<T>){
        requestQueue.add(req);
    }

    fun getRequest(url:String)
    {
        val getRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            iVolleyHttpRequest!!.onResponse(response.toString())
        }, Response.ErrorListener { error ->
            iVolleyHttpRequest!!.onResponse(error.message!!)
        })
        addToRequestQueue(getRequest)
    }

    fun postRequest(url: String)
    {
        val postRequest = object:StringRequest(Request.Method.POST,url,
            Response.Listener { response ->
                iVolleyHttpRequest!!.onResponse(response.toString())
            }, Response.ErrorListener { error -> iVolleyHttpRequest!!.onResponse(error.message!!) })
        {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["name"] = "name"
                params["value"] = "value"
                return params;
            }
        }
        addToRequestQueue(postRequest)
    }

    companion object {
        private var myInstance : RESTVolleyRequest? = null
        @Synchronized
        fun getInstance(context: Context): RESTVolleyRequest{
            if(myInstance == null)
            {
                myInstance = RESTVolleyRequest(context)
            }
            return myInstance!!
        }
        @Synchronized
        fun getInstance(context: Context, iVolleyHttpRequest: IVolleyHttpRequest): RESTVolleyRequest{
            if(myInstance == null)
            {
                myInstance = RESTVolleyRequest(context, iVolleyHttpRequest)
            }
            return myInstance!!
        }
    }
}