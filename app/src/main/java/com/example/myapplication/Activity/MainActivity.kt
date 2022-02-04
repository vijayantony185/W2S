package com.example.myapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.UserAdapter
import com.example.myapplication.Constants.Progress
import com.example.myapplication.Model.PostsResponse
import com.example.myapplication.R
import com.example.myapplication.ViewModel.PostsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var count = 0
    lateinit var postsViewModel: PostsViewModel
    lateinit var adapter: UserAdapter
    var progress: Progress? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showProgress()
        getPosts()
        onclicks()
    }

    public fun onclicks(){
        img_map.setOnClickListener {
            var intent = Intent(applicationContext,MapsActivity::class.java)
            startActivity(intent)
        }
    }

    public fun getPosts(){
        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        postsViewModel.getposts("posts")
        postsViewModel.getLiveData()!!.observe(this, Observer {
            if (it != null){
                cancelProgress()
                Log.d("response","------------------------->"+it)
                setUserRecyclerview(it)
            }
        })
    }

    public fun setUserRecyclerview(response : ArrayList<PostsResponse>){
        adapter = UserAdapter(applicationContext,response,object : UserAdapter.PostClick{
            override fun model(postsResponse: PostsResponse) {

            }

        })
        rcv_user_list.adapter = adapter
        rcv_user_list.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun showProgress() {
        progress = Progress(this)
        progress!!.show()
    }

    private fun cancelProgress() {
        if (progress != null) {
            progress!!.dismiss()
        }
    }

    override fun onBackPressed() {
        if (count == 0){
            Toast.makeText(applicationContext,"Click One more time to Exit",Toast.LENGTH_SHORT).show()
            count = count+1
        }else if (count == 1){
            finishAffinity()
        }
    }
}