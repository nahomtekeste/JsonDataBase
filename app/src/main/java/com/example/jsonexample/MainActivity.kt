package com.example.jsonexample

import android.app.DownloadManager
import android.app.VoiceInteractor
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.CacheResponse
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    lateinit var progress :ProgressBar
    lateinit var listView_details : ListView
    var arrayList_details :ArrayList<StudentModel> = ArrayList()
    var client =  OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = progressBar
        progress.visibility = View.VISIBLE
        listView_details = listView
        run("http://52.151.100.223")

    }
    fun run(url:String){
        progress.visibility = View.VISIBLE
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call,
                                    response: Response
            ) {
                var str_response = response.body()!!.string()
                //creating json object
                val json_contact: JSONObject = JSONObject(str_response)
                //creating json array
                var jsonarray_info: JSONArray =
                    json_contact.getJSONArray("info")
                var i:Int = 0
                var size:Int = jsonarray_info.length()
                arrayList_details= ArrayList();
                for (i in 0.. size-1) {
                    var json_objectdetail:JSONObject=
                        jsonarray_info.getJSONObject(i)
                    var model:StudentModel= StudentModel();
                    model.id=json_objectdetail.getString("id")
                    model.name=json_objectdetail.getString("name")
                    model.email=json_objectdetail.getString("email")
                    arrayList_details.add(model)
                }

                runOnUiThread{
                    val obj_adapter :CustomAdapter
                    obj_adapter = CustomAdapter(applicationContext,
                        arrayList_details)
                    listView_details.adapter = obj_adapter

                    progress.visibility = View.GONE

                }
            }

            override fun onFailure(call: okhttp3.Call,
                                   e: IOException) {
                progress.visibility = View.GONE
            }

        })
    }
}
// thix is our companoin class
 class CustomAdapter(context: Context , arrayListDetails: ArrayList<StudentModel>) :BaseAdapter(){


    private val layoutInflater:LayoutInflater
    private val arrayListDetails:ArrayList<StudentModel>

    init {
        this.layoutInflater = LayoutInflater.from(context)
        this.arrayListDetails = arrayListDetails
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view:View?
        val listRowHolder :ListRowHolder
        if (convertView == null){
            view = this.layoutInflater.inflate(R.layout.adapter_layout , parent , false)
            listRowHolder = ListRowHolder(view)
            view.tag= listRowHolder
        }else{
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }
     listRowHolder.tvName.text = arrayListDetails[position].name
        listRowHolder.tvEmail.text = arrayListDetails[position].email
        listRowHolder.tvId.text = arrayListDetails[position ].id

        return view
    }

    override fun getItem(position: Int): Any {
        return arrayListDetails.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayListDetails.size
    }
}
private class ListRowHolder(row : View){
  public  val tvName :TextView
    public val tvId :TextView
    public val tvEmail :TextView
    public val linearLayout :LinearLayout

    init {
        this.tvName = row?.findViewById(R.id.tvName) as TextView
        this.tvId = row?.findViewById(R.id.tvId) as TextView
        this.tvEmail = row?.findViewById(R.id.tvEmail) as TextView
        this.linearLayout = row?.findViewById(R.id.linearLayout) as LinearLayout
    }
}
