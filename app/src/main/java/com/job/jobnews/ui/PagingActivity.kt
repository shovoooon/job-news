package com.job.jobnews.ui

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.job.jobnews.R
import com.job.jobnews.adapter.PagingAdapter
import com.job.jobnews.db.MyDatabase
import com.job.jobnews.model.JobResponse

class PagingActivity : AppCompatActivity(), PagingAdapter.OnItemClicked {


    val numberList: MutableList<String> = ArrayList()
    var jobList = ArrayList<JobResponse>()
    var page = 0
    var isLoading = false
    val limit = 10
    private val LOG = "PagingActivity"

    lateinit var adapter: PagingAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)

        progressBar = findViewById(R.id.progressBar)
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.rv_paging)
        recyclerView.layoutManager = layoutManager
        getPage()


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

//                if (dy > 0) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount

                if (!isLoading) {

                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        page++
                        getPage()
                    }

                }
//                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    fun getPage() {
        /*isLoading = true
        recyclerView.visibility = View.VISIBLE
        val start = ((page) * limit) + 1
        val end = (page + 1) * limit*/

        /*for (i in start..end) {
            numberList.add("Item " + i.toString())
        }*/

        getRoomData()

        /*Handler().postDelayed({
            if (::adapter.isInitialized) {
                adapter.notifyDataSetChanged()
            } else {
                adapter = PagingAdapter(PagingActivity(), this)
                recyclerView.adapter = adapter
            }
            isLoading = false
            progressBar.visibility = View.GONE
        }, 5000)*/

    }

    private fun getRoomData() {
        isLoading = true
        recyclerView.visibility = View.VISIBLE
        val start = ((page) * limit) + 1
        val end = (page + 1) * limit


        class GetRoomData: AsyncTask<Void, Void, List<JobResponse>>() {
            override fun doInBackground(vararg params: Void?): List<JobResponse> {

                Log.v(LOG, "start: $start end: $end")

                return MyDatabase(this@PagingActivity).getJobDao().pagingData(start, end)
            }

            override fun onPostExecute(result: List<JobResponse>?) {

                Log.v(LOG, result.toString())

                //jobList.clear()
                jobList.addAll(result!!)

                if (::adapter.isInitialized) {
                    Log.v(LOG, "adapter.isInitialized")
                    adapter.notifyDataSetChanged()
                } else {
                    adapter = PagingAdapter(this@PagingActivity, this@PagingActivity)
                    recyclerView.adapter = adapter
                }
                isLoading = false
                progressBar.visibility = View.GONE

                Log.v(LOG, "job list: ${jobList.toString()}")

                super.onPostExecute(result)

            }

        }
        GetRoomData().execute()
    }

    override fun onClickView(id: String) {

    }


}