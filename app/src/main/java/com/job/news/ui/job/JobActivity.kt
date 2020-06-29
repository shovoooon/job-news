package com.job.news.ui.job

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
import com.bumptech.glide.Glide
import com.job.news.App
import com.job.news.Constants
import com.job.news.R
import com.job.news.db.MyDatabase
import com.job.news.model.AddViewResponse
import com.job.news.model.JobResponse
import com.job.news.network.RetrofitClient
import com.job.news.utils.toast
import kotlinx.android.synthetic.main.job_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class JobActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initialize()

    }

    private fun initialize() {

        jobId = intent.getStringExtra("id").toString()

        getJobData()

        apiRequest()

        startTimer()


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

                return MyDatabase(this@JobActivity).getJobDao().getJobById(jobId)
            }

            override fun onPostExecute(result: JobResponse?) {
                super.onPostExecute(result)

                updateUI(result!!)
            }

        }
        GetJobData().execute()

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
        }
        InsertData().execute()
    }

    private fun updateUI(body: JobResponse) {

        applyUrl = body.applyUrl
        extraUrl = body.extraUrl
        imgUrl = Constants.BASE_URL_IMG + body.jobImg
        coverUrl = Constants.BASE_URL_IMG + body.coverImg
        pdfUrl = Constants.BASE_URL_FILE + body.pdf

        Glide.with(this).load(coverUrl).into(imgJobCover)
        Glide.with(this).load(imgUrl).into(imgJob)
        tvJobTitle.text = body.title
        tvJobDetails.text = body.desc
        tvViews.text = body.views
        supportActionBar?.title = body.position

        if (extraUrl.isEmpty()) {
            btnSuggestedWebsite.visibility = GONE
        } else {
            btnSuggestedWebsite.visibility = VISIBLE
        }

    }

    fun applyNow(view: View) {
        try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(applyUrl))
        startActivity(intent)

        }catch (e: Exception){
            Log.e(LOG, e.message.toString())
        }
    }

    fun saveImage(view: View) {
        if (isPermissionGranted()) {
            downloadImage()
        }
    }

    fun downloadPdf(view: View) {
        if (isPermissionGranted()){
            downloadPdf()
        }
    }
    fun suggestedSite(view: View) {
        try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(extraUrl))
        startActivity(intent)

        }catch (e: Exception){
            Log.e(LOG, e.message.toString())
        }
    }


    private fun downloadImage() {

        val extension = MimeTypeMap.getFileExtensionFromUrl(imgUrl)

        val fileName = (System.currentTimeMillis() / 1000).toString()+"."+extension

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

        val fileName = (System.currentTimeMillis() / 1000).toString()+"."+extension

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
                    downloadImage()

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

        onBackPressed()
        return true
    }
}