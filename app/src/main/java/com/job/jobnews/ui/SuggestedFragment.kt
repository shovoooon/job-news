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
import com.job.jobnews.ui.viewmodel.SuggestedViewModel
import kotlinx.android.synthetic.main.suggested_fragment.*

class SuggestedFragment : Fragment(), JobAdapter.OnItemClicked {


    private lateinit var viewModel: SuggestedViewModel
    private val LOG = "SuggestedFragment"
    private lateinit var adapter: JobAdapter
    private var jobList = ArrayList<JobResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.suggested_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SuggestedViewModel::class.java)

        initialize()
    }


    private fun initialize() {

        adapter = JobAdapter(App.applicationContext(), jobList, this)
        rv_suggested.layoutManager = LinearLayoutManager(context)
        rv_suggested.adapter = adapter

        loadLocalData()

        //apiRequest()
    }

    private fun loadLocalData() {

        //isLoading()

        class LoadLocalData: AsyncTask<Void, Void, List<JobResponse>>() {
            override fun doInBackground(vararg params: Void?): List<JobResponse> {

                return MyDatabase(activity!!).getJobDao().getSuggestedJob()
            }

            override fun onPostExecute(result: List<JobResponse>?) {
                super.onPostExecute(result)
                jobList.clear()
                jobList.addAll(result!!)
                adapter.notifyDataSetChanged()
                Log.v(LOG, result.toString())
                //stopLoading()

            }

        }
        LoadLocalData().execute()
    }

    override fun onClickView(id: String) {

        val intent = Intent(Intent(activity, JobActivity::class.java))
        intent.putExtra("id", id)
        startActivity(intent)
    }

}