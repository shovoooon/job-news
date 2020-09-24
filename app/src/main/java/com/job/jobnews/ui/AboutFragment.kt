package com.job.jobnews.ui

import android.os.AsyncTask
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.job.jobnews.R
import com.job.jobnews.db.MyDatabase
import com.job.jobnews.ui.viewmodel.AboutViewModel
import kotlinx.android.synthetic.main.about_fragment.*

class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }

    private lateinit var viewModel: AboutViewModel
    private var head = ""
    private var body = ""
    private val LOG = "AboutFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutViewModel::class.java)

        initialize()
    }

    private fun initialize() {
        getInfo()
    }

    private fun getInfo() {
        class GetInfo:AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {

                head = MyDatabase(activity!!).getInfoDao().getInfo().aboutHead
                body = MyDatabase(activity!!).getInfoDao().getInfo().aboutBody

                Log.v(LOG, "$head $body")
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                tvAboutHead.text = head
                tvAbout.text = body
            }
        }
        GetInfo().execute()
    }

}