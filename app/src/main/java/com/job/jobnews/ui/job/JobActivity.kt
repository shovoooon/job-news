package com.job.jobnews.ui.job

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.job.jobnews.App
import com.job.jobnews.Constants
import com.job.jobnews.MainActivity
import com.job.jobnews.R
import com.job.jobnews.adapter.JobAdapter
import com.job.jobnews.adapter.JobImagesAdapter
import com.job.jobnews.db.MyDatabase
import com.job.jobnews.model.AddViewResponse
import com.job.jobnews.model.JobImagesResponse
import com.job.jobnews.model.JobResponse
import com.job.jobnews.network.RetrofitClient
import com.job.jobnews.utils.toast
import kotlinx.android.synthetic.main.job_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class JobActivity : AppCompatActivity(), JobImagesAdapter.OnItemClicked {

    private val LOG = "JobActivity"
    private var jobId = ""
    private var applyUrl = ""
    private var extraUrl = ""
    private var imgUrl = ""
    private var pdfUrl = ""
    private var coverUrl = ""
    private lateinit var timer: CountDownTimer
    private val SAVE_IMG_REQUEST_CODE = 100
    private val DOWNLOAD_PDF_REQUEST_CODE = 101
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var adapter: JobImagesAdapter
    private var imageList = ArrayList<JobImagesResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initialize()

    }

    private fun initialize() {

        adapter = JobImagesAdapter(this, imageList, this)
        rv_job_images.layoutManager = LinearLayoutManager(this)
        rv_job_images.adapter = adapter

        if (intent.hasExtra("id")){
            jobId = intent.getStringExtra("id").toString()
            getJobData()
        }else if (intent.hasExtra("key")){
            jobId = intent.getStringExtra("key").toString()
        }

        loadInterAd()

        apiRequest()

        getImages()

        startTimer()


    }

    private fun loadInterAd() {
        mInterstitialAd = InterstitialAd(App.applicationContext())
        mInterstitialAd.adUnitId = Constants.interstitial_id
        if (!mInterstitialAd.isLoaded) {
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        }

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
            }
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                addViewRequest()
            }

            override fun onTick(millisUntilFinished: Long) {

            }

        }.start()
    }

    private fun addViewRequest() {

        RetrofitClient.instance.addView(jobId)
            .enqueue(object : Callback<AddViewResponse> {
                override fun onFailure(call: Call<AddViewResponse>, t: Throwable) {
                    Log.e(LOG, t.message.toString())
                }

                override fun onResponse(
                    call: Call<AddViewResponse>,
                    response: Response<AddViewResponse>
                ) {
                    if (!response.body()?.error!!) {
                        apiRequest()
                    }
                }

            })

    }

    private fun getJobData() {

        class GetJobData : AsyncTask<Void, Void, JobResponse>() {
            override fun doInBackground(vararg params: Void?): JobResponse {

                var data: JobResponse? = null

                try {
                    data = MyDatabase(this@JobActivity).getJobDao().getJobById(jobId)
                }catch (e:Exception){
                    Log.e(LOG, e.message.toString())
                }
                return data!!
            }

            override fun onPostExecute(result: JobResponse) {
                super.onPostExecute(result)

                if (result.id != null || result.id != "") {
                    updateUI(result)
                }

            }

        }
        GetJobData().execute()

    }

    private fun getImages() {
        RetrofitClient.instance.jobImages(jobId)
            .enqueue(object : Callback<List<JobImagesResponse>>{
                override fun onResponse(
                    call: Call<List<JobImagesResponse>>,
                    response: Response<List<JobImagesResponse>>
                ) {
                    Log.v(LOG, "Response code: ${response.code()} Body: " + response.body()!!.toString())

                    if (response.code() == 200){
                        if (response.body()!!.isNotEmpty()){
                            imageList.clear()
                            imageList.addAll(response.body()!!)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<List<JobImagesResponse>>, t: Throwable) {
                    Log.v(LOG, t.message.toString())
                }

            })
    }

    private fun apiRequest() {

        RetrofitClient.instance.jobs()
            .enqueue(object : Callback<List<JobResponse>> {
                override fun onFailure(call: Call<List<JobResponse>>, t: Throwable) {
                    Log.e(LOG, t.message.toString())

                }

                override fun onResponse(
                    call: Call<List<JobResponse>>,
                    response: Response<List<JobResponse>>
                ) {
                    Log.v(LOG, "code: ${response.code()}")
                    Log.v(LOG, "response: ${response.body()}")

                    insertData(response.body()!!)
                    getJobData()

                }


            })
    }


    private fun insertData(body: List<JobResponse>) {
        class InsertData : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                try {
                    MyDatabase(this@JobActivity).getJobDao().insertAll(body)
                } catch (e: Exception) {
                    Log.e(LOG, "insert db: " + e.message.toString())
                }

                return null
            }

            override fun onPostExecute(result: Void?) {

                getJobData()

                super.onPostExecute(result)
            }
        }
        InsertData().execute()
    }

    private fun updateUI(body: JobResponse) {

        progressBar2.visibility = GONE

        applyUrl = body.applyUrl
        extraUrl = body.extraUrl
        imgUrl = Constants.BASE_URL_IMG + body.jobImg
        coverUrl = Constants.BASE_URL_IMG + body.coverImg
        pdfUrl = Constants.BASE_URL_FILE + body.pdf


        try {
            Glide.with(this).load(coverUrl).into(imgJobCover)
            //Glide.with(this).load(imgUrl).into(imgJob)
            tvJobTitle.text = body.title
            tvJobDetails.text = body.desc
            tvViews.text = body.views
            supportActionBar?.title = body.position

        } catch (e: Exception) {
            Log.e(LOG, e.message.toString())
        }



        if (extraUrl.isEmpty()) {
            btnSuggestedWebsite.visibility = GONE
        } else {
            btnSuggestedWebsite.visibility = VISIBLE
        }

        if (pdfUrl.isEmpty()) {
            btnDlPdf.visibility = GONE
        } else {
            btnDlPdf.visibility = VISIBLE
        }

    }

    fun applyNow(view: View) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(applyUrl))
            startActivity(intent)

        } catch (e: Exception) {
            Log.e(LOG, e.message.toString())
        }
    }

    fun saveImage(view: View) {

    }

    fun downloadPdf(view: View) {

        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
            return
        }

        if (isPermissionGranted()) {
            downloadPdf()
        }
    }

    fun suggestedSite(view: View) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(extraUrl))
            startActivity(intent)

        } catch (e: Exception) {
            Log.e(LOG, e.message.toString())
        }
    }


    private fun downloadImage(imgUrl:String) {

        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
            return
        }

        val extension = MimeTypeMap.getFileExtensionFromUrl(imgUrl)

        val fileName = (System.currentTimeMillis() / 1000).toString() + "." + extension

        Log.v(LOG, "filename: $fileName")

        val downloadUri = Uri.parse(imgUrl)

        Log.v(LOG, "uri: $downloadUri")

        val dManager =
            App.applicationContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        try {

            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("Downloading")
                .setDescription("Downloading job image")
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setMimeType(getMimeType(downloadUri))
            dManager.enqueue(request)

            toast("Downloading")

            Log.v(LOG, "Downloading")

        } catch (e: Exception) {
            Log.e(LOG, e.message.toString())
        } catch (e: IOException) {
            Log.e(LOG, "IO: ${e.message.toString()}")
        }
    }

    private fun downloadPdf() {

        val extension = MimeTypeMap.getFileExtensionFromUrl(pdfUrl)

        val fileName = (System.currentTimeMillis() / 1000).toString() + "." + extension

        Log.v(LOG, "filename: $fileName")

        val downloadUri = Uri.parse(pdfUrl)

        Log.v(LOG, "uri: $downloadUri")

        val dManager =
            App.applicationContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        try {

            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("Downloading")
                .setDescription("Downloading job pdf")
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setMimeType(getMimeType(downloadUri))
            dManager.enqueue(request)

            toast("Downloading")

            Log.v(LOG, "Downloading")

        } catch (e: Exception) {
            Log.e(LOG, e.message.toString())
        } catch (e: IOException) {
            Log.e(LOG, "IO: ${e.message.toString()}")
        }
    }

    private fun getMimeType(uri: Uri?): String? {
        val resolver = App.applicationContext().contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri!!))
    }


    private fun isPermissionGranted(): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    App.applicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    SAVE_IMG_REQUEST_CODE
                )
                return false
            }
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

            SAVE_IMG_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    //permission granted
                    //go through downloading image
                    toast("Permission granted")
                    //downloadImage()

                } else {
                    toast("Permission denied")
                }
            }

            DOWNLOAD_PDF_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    //permission granted
                    //go through downloading pdf

                    toast("Permission granted")
                } else {
                    toast("Permission denied")
                }
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {

        if (isTaskRoot){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{
            onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        if (isTaskRoot){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{
            super.onBackPressed()
        }
    }

    override fun onClickBtnDownload(jobImg: String) {
        if (isPermissionGranted()) {
            downloadImage(Constants.BASE_URL_IMG+jobImg)
        }
    }
}