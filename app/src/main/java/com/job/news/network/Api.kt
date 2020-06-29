package com.job.news.network
import com.job.news.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {


    @FormUrlEncoded
    @POST("app.php")
    fun info(
        @Field("info") info:String = "info"
    ):Call<InfoResponse>

    @FormUrlEncoded
    @POST("app.php")
    fun categories(
        @Field("categories") categories:String = "categories"
    ):Call<List<CategoryResponse>>

    @FormUrlEncoded
    @POST("app.php")
    fun jobs(
        @Field("jobs") jobs:String = "jobs"
    ):Call<List<JobResponse>>

    @FormUrlEncoded
    @POST("app.php")
    fun contact(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("message") message:String,
        @Field("contact") contact:String = "contact"
    ):Call<ContactResponse>

    @FormUrlEncoded
    @POST("app.php")
    fun addView(
        @Field("id") id: String,
        @Field("add_view") addView:String = "add_view"
    ):Call<AddViewResponse>

    @FormUrlEncoded
    @POST("app.php")
    fun openJob(
        @Field("id") id:String,
        @Field("open_job") openJob:String = "open_job"
    ):Call<OpenJobResponse>


    /*@FormUrlEncoded
    @POST("dashboard.php")
    fun dashboard(
            @Field("email") email:String
    ):Call<DashboardResponse>

    @FormUrlEncoded
    @POST("register.php")
    fun register(
            @Field("name") name:String,
            @Field("email") email:String,
            @Field("phone") phone:String,
            @Field("page_url") pageUrl:String,
            @Field("identity") identity:String,
            @Field("address") address:String,
            @Field("password") password:String
    ):Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
            @Field("email") email:String,
            @Field("password") password:String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("payment_request.php")
    fun paymentReq(
            @Field("email") email:String,
            @Field("payment_info") paymentInfo:String,
            @Field("payment_method") paymentMethod:String,
            @Field("payment_amount") paymentAmount:Int
    ):Call<PaymentResponse>

    @FormUrlEncoded
    @POST("addorder.php")
    fun addOrder(
            @Field("email") email:String,
            @Field("cName") cName:String,
            @Field("cPhone") cPhone:String,
            @Field("cAddress") cAddress:String,
            @Field("pName") pName:String,
            @Field("pCode") pCode:String,
            @Field("pColor") pColor:String,
            @Field("pSize") pSize:String,
            @Field("buyPrice") buyPrice:String,
            @Field("sellPrice") sellPrice:String,
            @Field("deliveryCharge") deliveryCharge:String,
            @Field("deliveryWay") deliveryWay:Int,
            @Field("comment") comment:String
            ):Call<OrderResponse>

    @FormUrlEncoded
    @POST("myorders.php")
    fun getMyOrders(
            @Field("email") email: String,
            @Field("status") status: Int
    ):Call<List<MyOrdersResponse>>

    @FormUrlEncoded
    @POST("cancelorder.php")
    fun cancelOrder(
            @Field("email") email: String,
            @Field("orderId") orderId: String,
            @Field("profit") profit: String
    ):Call<CancelResponse>

    @FormUrlEncoded
    @POST("ordersummery.php")
    fun orderSummery(
            @Field("email") email: String
    ):Call<SummeryResponse>

    @FormUrlEncoded
    @POST("sendcode.php")
    fun sendCode(
            @Field("email") email: String,
            @Field("code") code: String
    ):Call<ForgotResponse>

    @FormUrlEncoded
    @POST("resetpass.php")
    fun resetPass(
            @Field("email") email: String,
            @Field("password") password: String
    ):Call<ResetResponse>*/

}

