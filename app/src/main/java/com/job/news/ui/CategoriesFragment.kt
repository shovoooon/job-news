package com.job.news.ui

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
import com.job.news.App
import com.job.news.R
import com.job.news.adapter.CategoryAdapter
import com.job.news.db.MyDatabase
import com.job.news.model.CategoryResponse
import com.job.news.network.RetrofitClient
import com.job.news.ui.viewmodel.CategoriesViewModel
import kotlinx.android.synthetic.main.categories_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesFragment : Fragment(), CategoryAdapter.OnItemClicked {

    companion object {
        fun newInstance() =
            CategoriesFragment()
    }

    private lateinit var viewModel: CategoriesViewModel
    private val LOG = "CategoriesFragment"
    private lateinit var adapter: CategoryAdapter
    private var categoryList = ArrayList<CategoryResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.categories_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)

        initialize()

    }

    private fun initialize() {
        adapter = CategoryAdapter(App.applicationContext(), categoryList, this)
        rv_categories.layoutManager = LinearLayoutManager(context)
        rv_categories.adapter = adapter

        loadLocalData()

        CoroutineScope(IO).launch {
            apiRequest()
        }
    }

    private fun apiRequest() {

        RetrofitClient.instance.categories()
            .enqueue(object : Callback<List<CategoryResponse>>{
                override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                    Log.e(LOG, t.message.toString())

                }

                override fun onResponse(
                    call: Call<List<CategoryResponse>>,
                    response: Response<List<CategoryResponse>>
                ) {
                    Log.v(LOG, "code: ${response.code()}")
                    Log.v(LOG, "response: ${response.body()}")

                    /*categoryList.clear()
                    categoryList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()*/
                    Log.v(LOG, categoryList.toString())

                    insertData(response.body()!!)
                }

            })
    }

    private fun insertData(body: List<CategoryResponse>) {
        class InsertData:AsyncTask<Void,Void,Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                try {
                    MyDatabase(activity!!).getCategoryDao().insert(body)
                }catch (e: Exception){Log.e(LOG, "insert db: "+e.message.toString())}

                return null
            }

            override fun onPostExecute(result: Void?) {
                loadLocalData()
                super.onPostExecute(result)
            }
        }
        InsertData().execute()
    }

    private fun loadLocalData() {

        class LoadLocalData: AsyncTask<Void, Void, List<CategoryResponse>>() {
            override fun doInBackground(vararg params: Void?): List<CategoryResponse> {

                return MyDatabase(activity!!).getCategoryDao().getCategories()
            }

            override fun onPostExecute(result: List<CategoryResponse>?) {
                super.onPostExecute(result)
                categoryList.clear()
                categoryList.addAll(result!!)
                adapter.notifyDataSetChanged()
                Log.v(LOG, result.toString())

            }

        }
        LoadLocalData().execute()
    }

    override fun onItemClicked(id: String) {

        transactFragment(id)



    }

    private fun transactFragment(id: String) {

        class GetCatName:AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String {

                val catName = MyDatabase(App.applicationContext()).getCategoryDao()
                    .getCategoryById(id).catName
                Log.v(LOG, catName)
                return catName
            }

            override fun onPostExecute(result: String) {

                try {
                    val intent = Intent(requireActivity(), CategoryOpenActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("catName", result)
                    startActivity(intent)
                }catch (e:Exception){Log.e(LOG, e.message.toString())}

                super.onPostExecute(result)
            }
        }

        GetCatName().execute()
    }

}