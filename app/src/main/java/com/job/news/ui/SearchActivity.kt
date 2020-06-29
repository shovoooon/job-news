package com.job.news.ui

import android.app.SearchManager
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.job.news.App
import com.job.news.R
import com.job.news.adapter.JobAdapter
import com.job.news.db.MyDatabase
import com.job.news.model.JobResponse
import com.job.news.ui.job.JobActivity
import com.job.news.utils.MySuggestionProvider
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), JobAdapter.OnItemClicked {

    private val LOG = "SearchActivity"
    private lateinit var adapter: JobAdapter
    private var jobList = ArrayList<JobResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Search Job"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initialize()

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)

                search(query)
                Log.v(LOG, query)
            }
        }


    }

    private fun initialize() {
        adapter = JobAdapter(App.applicationContext(), jobList, this)
        rv_search.layoutManager = LinearLayoutManager(this)
        rv_search.adapter = adapter
        rv_search.hasFixedSize()
    }

    private fun search(query: String) {

        //startLoading()

        getLocalData("%$query%")


    }

    private fun getLocalData(query: String) {

        class GetLocalData : AsyncTask<Void, Void, List<JobResponse>>() {

            override fun doInBackground(vararg params: Void?): List<JobResponse> {

                return MyDatabase(this@SearchActivity).getJobDao().searchJob(query)
            }

            override fun onPostExecute(result: List<JobResponse>) {

                jobList.clear()
                jobList.addAll(result)
                adapter.notifyDataSetChanged()

                if (result.isEmpty() || result[0] == null || result == null){
                    noResult()
                    Log.v(LOG, "no result")
                }else{
                    stopLoading()
                    Log.v(LOG, "found result")
                }

                Log.v(LOG, jobList.toString())
                Log.v(LOG, result.toString())
                super.onPostExecute(result)
            }

            private fun noResult() {
                rv_search.visibility = GONE
                tvSearchInfo.visibility = VISIBLE
                tvSearchInfo.text = "No result found"

            }

        }

        GetLocalData().execute()
    }

    private fun startLoading() {
        rv_search.visibility = GONE
        tvSearchInfo.text = "Searching job..."
        tvSearchInfo.visibility = VISIBLE
    }

    private fun stopLoading() {
        rv_search.visibility = VISIBLE
        tvSearchInfo.text = "No job found"
        tvSearchInfo.visibility = GONE
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val item = menu?.findItem(R.id.menuSearch)
        val searchView = item?.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    search(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    search(newText)
                }
                return false

            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClickView(id: String) {
        try {
            val intent = Intent(Intent(this, JobActivity::class.java))
            intent.putExtra("id", id)
            startActivity(intent)
        }catch (e:Exception){Log.e(LOG, e.message.toString())}
    }
}