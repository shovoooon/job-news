package com.job.news.ui

import android.content.Intent
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.job.news.App
import com.job.news.R
import com.job.news.adapter.JobAdapter
import com.job.news.db.MyDatabase
import com.job.news.model.JobResponse
import com.job.news.network.RetrofitClient
import com.job.news.ui.job.JobActivity
import com.job.news.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), JobAdapter.OnItemClicked {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private val LOG = "HomeFragment"
    private lateinit var adapter: JobAdapter
    private var jobList = ArrayList<JobResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        initialize()
    }


    private fun initialize() {

        adapter = JobAdapter(App.applicationContext(), jobList, this)
        rv_jobs.layoutManager = LinearLayoutManager(context)
        rv_jobs.adapter = adapter

        loadLocalData()

        apiRequest()
    }

    private fun loadLocalData() {

        isLoading()

        class LoadLocalData: AsyncTask<Void, Void, List<JobResponse>>() {
            override fun doInBackground(vararg params: Void?): List<JobResponse> {

                return MyDatabase(activity!!).getJobDao().getAllJob()
            }

            override fun onPostExecute(result: List<JobResponse>?) {
                super.onPostExecute(result)
                jobList.clear()
                jobList.addAll(result!!)
                adapter.notifyDataSetChanged()
                Log.v(LOG, result.toString())
                stopLoading()

            }

        }
        LoadLocalData().execute()
    }

    private fun apiRequest() {

        RetrofitClient.instance.jobs()
            .enqueue(object : Callback<List<JobResponse>>{
                override fun onFailure(call: Call<List<JobResponse>>, t: Throwable) {
                    Log.e(LOG, t.message.toString())

                }

                override fun onResponse(
                    call: Call<List<JobResponse>>,
                    response: Response<List<JobResponse>>
                ) {
                    Log.v(LOG, "code: ${response.code()}")
                    Log.v(LOG, "response: ${response.body()}")

                    jobList.clear()
                    jobList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                    Log.v(LOG, jobList.toString())
                    stopLoading()

                    insertData(response.body()!!)

                }


            })
    }

    private fun insertData(body: List<JobResponse>) {
        class InsertData:AsyncTask<Void,Void,Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                try {
                    MyDatabase(activity!!).getJobDao().insertAll(body)
                }catch (e: Exception){Log.e(LOG, "insert db: "+e.message.toString())}

                return null
            }
        }
        InsertData().execute()
    }

    private fun isLoading() {

        rv_jobs.visibility = GONE
        shimmer_view_container.startShimmer()
        shimmer_view_container.visibility = VISIBLE
    }
    private fun stopLoading() {

        rv_jobs.visibility = VISIBLE
        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = GONE
    }
    override fun onClickView(id: String) {

        try {
            val intent = Intent(Intent(activity, JobActivity::class.java))
            intent.putExtra("id", id)
            startActivity(intent)
        }catch (e:Exception){Log.e(LOG, e.message.toString())}

    }

    override fun onPause() {
        super.onPause()
        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = GONE
    }


}