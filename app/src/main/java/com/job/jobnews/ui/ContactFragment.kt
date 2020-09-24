package com.job.jobnews.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.job.jobnews.R
import com.job.jobnews.model.ContactResponse
import com.job.jobnews.network.RetrofitClient
import com.job.jobnews.ui.viewmodel.ContactViewModel
import com.job.jobnews.utils.toast
import kotlinx.android.synthetic.main.contact_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactFragment : Fragment() {

    companion object {
        fun newInstance() = ContactFragment()
    }

    private lateinit var viewModel: ContactViewModel
    private var name = ""
    private var email = ""
    private var message = ""
    private val LOG = "ContactFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        btnSend.setOnClickListener {

            if (inputOkay()){
                    apiRequest()
            }else{
                toast("Fill all required fields!")
                return@setOnClickListener
            }
        }
    }

    private fun apiRequest() {

        isLoading()

        RetrofitClient.instance.contact(name, email, message)
            .enqueue(object : Callback<ContactResponse>{
                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    Log.e(LOG, t.message.toString())

                    isNotLoading()
                }

                override fun onResponse(
                    call: Call<ContactResponse>,
                    response: Response<ContactResponse>
                ) {
                    Log.v(LOG, "code: ${response.code()}")
                    Log.v(LOG, "response: ${response.body()}")

                    isNotLoading()

                    if (!response.body()?.error!!){

                        toast("Thanks for contacting with us")

                    }
                }


            })
    }

    private fun isLoading() {
        btnSend.isEnabled = false
        btnSend.text = "SENDING"
    }

    private fun isNotLoading() {
        btnSend.isEnabled = true
        btnSend.text = "SEND"
    }

    private fun inputOkay(): Boolean {

        name = etName.editText?.text.toString()
        email = etEmail.editText?.text.toString()
        message = etMessage.editText?.text.toString()

        return name.isNotEmpty() && email.isNotEmpty() && message.isNotEmpty()

    }

}