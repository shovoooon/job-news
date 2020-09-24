package com.job.jobnews.ui

import android.content.Intent
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.job.jobnews.App
import com.job.jobnews.R
import com.job.jobnews.adapter.JobAdapter
import com.job.jobnews.db.MyDatabase
import com.job.jobnews.model.JobResponse
import com.job.jobnews.ui.job.JobActivity
import com.job.jobnews.ui.viewmodel.JobViewModel
import com.job.jobnews.utils.getData
import com.job.jobnews.utils.toast
import kotlinx.android.synthetic.main.category_open_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*

class CategoryOpenFragment : Fragment(), JobAdapter.OnItemClicked {

    private lateinit var viewModel: JobViewModel
    private val LOG = "HomeFragment"
    private lateinit var adapter: JobAdapter
    private var jobList = ArrayList<JobResponse>()
    private var CAT_ID = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.category_open_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JobViewModel::class.java)

        initialize()

    }

    private fun initialize() {

        toast(getData("catId")!!)

        adapter = JobAdapter(App.applicationContext(), jobList, this)
        rv_category_open.layoutManager = LinearLayoutManager(context)
        rv_category_open.adapter = adapter

        loadLocalData()

        //apiRequest()
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
    private fun isLoading() {
        rv_jobs.visibility = View.GONE

    }
    private fun stopLoading() {
        rv_jobs.visibility = View.VISIBLE

    }
    override fun onClickView(id: String) {
        try {
            val intent = Intent(Intent(activity, JobActivity::class.java))
            intent.putExtra("id", id)
            startActivity(intent)
        }catch (e:Exception){
            Log.e(LOG, e.message.toString())}

    }

}