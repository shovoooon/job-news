package com.job.jobnews.ui

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.job.jobnews.R
import com.job.jobnews.adapter.JobAdapter
import com.job.jobnews.db.MyDatabase
import com.job.jobnews.model.JobResponse
import com.job.jobnews.ui.job.JobActivity
import kotlinx.android.synthetic.main.activity_category_open.*

class CategoryOpenActivity : AppCompatActivity(), JobAdapter.OnItemClicked {

    private var catId = ""
    private var catName = ""
    private val LOG = "CategoryOpenActivity"
    private lateinit var adapter: JobAdapter
    private var jobList = ArrayList<JobResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_open)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        catName = intent.getStringExtra("catName")
        catId = intent.getStringExtra("id")
        supportActionBar?.title = catName

        initialize()
    }

    private fun initialize() {
        rv_category_open.layoutManager = LinearLayoutManager(this)
        adapter = JobAdapter(this, jobList, this)
        rv_category_open.adapter = adapter

        loadLocalData()

    }

    private fun loadLocalData() {

        class LoadLocalData: AsyncTask<Void, Void, List<JobResponse>>() {
            override fun doInBackground(vararg params: Void?): List<JobResponse> {

                return MyDatabase(this@CategoryOpenActivity).getJobDao().getJobsByCat(catId)
            }

            override fun onPostExecute(result: List<JobResponse>?) {

                jobList.clear()
                jobList.addAll(result!!)
                adapter.notifyDataSetChanged()
                Log.v(LOG, result.toString())

                super.onPostExecute(result)

            }

        }
        LoadLocalData().execute()
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    override fun onClickView(id: String) {
        try {
            val intent = Intent(this, JobActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }catch (e:Exception){Log.e(LOG, e.message.toString())}
    }
}