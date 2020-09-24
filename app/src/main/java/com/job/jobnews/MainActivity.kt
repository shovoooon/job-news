package com.job.jobnews

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.job.jobnews.db.MyDatabase
import com.job.jobnews.model.InfoResponse
import com.job.jobnews.network.RetrofitClient
import com.job.jobnews.ui.*
import com.job.jobnews.utils.toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val LOG = "MainActivity"
    private var CLOSE = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title=getString(R.string.app_name)

        MobileAds.initialize(App.applicationContext())

        CoroutineScope(IO).launch {
            apiRequest()
        }

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout, toolbar, (R.string.open), (R.string.close)
        ){

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fragmentTransaction(HomeFragment(), getString(R.string.app_name))
        setSelectedDrawerMenu(R.id.menu_home)






        loadBannerAd()
    }

    private fun loadBannerAd() {

        val mAdView = AdView(this)
        mAdView.adSize = AdSize.SMART_BANNER
        mAdView.adUnitId = Constants.banner_id
        adMobView.addView(mAdView)

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }


    private fun apiRequest() {
        RetrofitClient.instance.info()
            .enqueue(object : Callback<InfoResponse>{
                override fun onFailure(call: Call<InfoResponse>, t: Throwable) {
                    Log.e(LOG, t.message.toString())

                }

                override fun onResponse(
                    call: Call<InfoResponse>,
                    response: Response<InfoResponse>
                ) {
                    Log.v(LOG, "code: ${response.code()}")
                    Log.v(LOG, "response: ${response.body()}")

                    insertData(response.body()!!)

                }

            })
    }


    private fun insertData(body: InfoResponse) {
        class InsertData: AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                try {
                    MyDatabase(this@MainActivity).getInfoDao().insert(body)
                }catch (e: Exception){Log.e(LOG, "insert db: "+e.message.toString())}

                return null
            }
        }
        InsertData().execute()
    }

    private fun fragmentTransaction(fragment: Fragment, title: String){
        try {
            supportActionBar?.title = title
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }catch (e: Exception){
            Log.v(LOG, e.message.toString())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_home -> {
                fragmentTransaction(HomeFragment(), getString(R.string.app_name))
            }
            R.id.menu_category -> {
                fragmentTransaction(CategoriesFragment(), "Categories")
            }
            R.id.menu_suggested -> {
                fragmentTransaction(SuggestedFragment(), "Suggested")
                setSelectedDrawerMenu(R.id.menu_suggested)
            }
            R.id.menu_about -> {
                fragmentTransaction(AboutFragment(), "About Us")
            }
            R.id.menu_contact -> {
                fragmentTransaction(ContactFragment(), "Contact Us")
            }
            /*R.id.menu_developer -> {
                developer()
            }*/
            R.id.menu_rate -> {
                rate()
            }
            R.id.menu_share -> {
                share()
            }
            R.id.menu_fb -> {
                facebook()
            }
            R.id.menu_yt -> {
                youtube()
            }
            R.id.menu_exit -> {
                finishAffinity()
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun youtube() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.youtubeUrl))
        startActivity(intent)
    }

    private fun facebook() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.facebookUrl))
        startActivity(intent)
    }

    private fun developer() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.developerUrl))
        startActivity(intent)
    }

    private fun rate() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.appLinkPlayStore))
        startActivity(intent)
    }

    private fun share() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.share_msg)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val item = menu?.findItem(R.id.actionSearch)
        val searchView = item?.actionView as androidx.appcompat.widget.SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        //searchView.isSubmitButtonEnabled = true
        //searchView.setIconifiedByDefault(false)

        /*searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    *//*search(query)*//*
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
//                    fetchSearch(newText)
                }
                return false

            }

        })*/

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_suggested -> {
                fragmentTransaction(SuggestedFragment(), "Suggested")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setSelectedDrawerMenu(id:Int){
        try {

            nav_view.setCheckedItem(id)

        }catch (e: Exception){
            Log.e(LOG, e.message.toString())
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK){

            val fragment = supportFragmentManager.findFragmentById(R.id.frame_layout)

            if (fragment != null){
                if (fragment is HomeFragment){
                    /*exit dialog here*/

                    onBackPressed()

                }else{
                    fragmentTransaction(HomeFragment(), "Home")
                }
            }

        }

        return true

    }

    override fun onBackPressed() {

        if (CLOSE){
            finishAffinity()
            finish()
        }else{
            CLOSE = true
            toast("Press back again to Exit!")
        }
    }

}