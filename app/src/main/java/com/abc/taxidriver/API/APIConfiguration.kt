package com.API


import com.abc.taxidriver.Dashboard.CancelReasonModels.CancelReasonResponse
import com.abc.taxidriver.Dashboard.Model.*
import com.abc.taxidriver.HelpandSupport.Model.QuickContactResponse
import com.abc.taxidriver.Language.Model.LanguageListResponse
import com.abc.taxidriver.Notification.notificationResponse
import com.abc.taxidriver.Otp.OtpModels.OtpVerifyResponse
import com.abc.taxidriver.Payment.CompeteBookingModels.CompleteBookingResponse
import com.abc.taxidriver.Privacy_Policy.Model.PrivacyPolicyResponse
import com.abc.taxidriver.Profile_Overview.ModelEditImage.UpdatePhotoResponse
import com.abc.taxidriver.Profile_Overview.ModelEditImage.UpdateProfileDetailResponse
import com.abc.taxidriver.Profile_Overview.ModelEditProfile.EditProfileResponse
import com.abc.taxidriver.Signup.Model.SignupResponse

import com.abc.taxidriver.Signup.Model.SigninResponse
import com.abc.taxidriver.Terms_Condition.Model.TermsConditionsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIConfiguration {
    /*{"name":"test","email":"anand@gmail.com","mobile":"9865896585",
    "language":"1","vehicle_type":"hondacity","vehicle_no":"420",
    "licence":"test.png","vehicle_licence":"test.png"}*/
    @Multipart
    @POST("driver/driversignup")
    fun signUp(

//        @Part("user_id") userId: RequestBody,
        @Part("device_tokanid") devicetokanid: RequestBody,
        @Part("name") name: RequestBody,
        @Part("mobile") phone_no: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("language") language: RequestBody,
        @Part("vehicle_type") vehicle_type: RequestBody,
        @Part("vehicle_no") vehicle_no: RequestBody,
//        @Part("licence") licence: MultipartBody.Part?,
        @Part image1: MultipartBody.Part?,
        @Part vehicle_licence: MultipartBody.Part?,
//        @Part("location") weight: RequestBody,
//        @Part("exercise_id") packageCondition: RequestBody,
//        @Part("lat") sendNotification: RequestBody,
//        @Part("lng") packageStatus: RequestBody,

    ): Call<SignupResponse>

    @POST("driver/termsconditions")
    fun termsConditionApi(
        @Body request: java.util.HashMap<String, String>
    ): Call<TermsConditionsResponse>

    @POST("driver/privacypolicy")
    fun privacyPolicyApi(
        @Body request: java.util.HashMap<String, String>
    ): Call<PrivacyPolicyResponse>

    @POST("users/languagelist")
    fun languageListApi(
        @Body request: java.util.HashMap<String, String>
    ): Call<LanguageListResponse>

    @POST("driver/driverLoginotp")
    fun driverLoginOtpApi(
        @Body request: java.util.HashMap<String, String>
    ): Call<SigninResponse>

    @POST("driver/driverotpverify")
    fun otpVerify(
        @Body request: java.util.HashMap<String, String>
    ): Call<OtpVerifyResponse>

/*    @POST("driver/editdriprofile")
    fun editDriProfileApi(
        @Body request: java.util.HashMap<String, String>
    ) : Call<UpdatePhotoResponse>*//**/

    @POST("driver/quickcontact")
    fun driverQuickContactApi(
        @Body request: java.util.HashMap<String, String>
    ): Call<QuickContactResponse>

    @Multipart
    @POST("driver/editdriprofile")
    fun editDriProfileApi(
        @Part("driver_id") driver_id: RequestBody,
        @Part image1: MultipartBody.Part?,
    ): Call<UpdateProfileDetailResponse>

    @Multipart
    @POST("driver/drivereditprofile")
    fun drivereditprofileApi(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("customer_id") customer_id: RequestBody,
        @Part("vehicle_type") vehicle_type: RequestBody,
        @Part("vehicle_no") vehicle_no: RequestBody,
        @Part driving_license: MultipartBody.Part?,
        @Part vehicle_licence: MultipartBody.Part?,
    ): Call<UpdateProfileDetailResponse>

    @POST("driver/drivertrip")
    fun driverTripApi(
        @Body request: java.util.HashMap<String, String>
    ): Call<DriverTripResponse>

//---------gopal---------/////

    @POST("driver/driverlocation")
    @Headers("Content-Type: application/json")
    fun sendDriverLocation(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<DriverLocationResponse>

    @POST("driver/rejecttrip")
    @Headers("Content-Type: application/json")
    fun rejectTrip(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<RejectrideResponse>

    @POST("driver/Cancelride")
    @Headers("Content-Type: application/json")
    fun rejectTrip2(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<RejectrideResponse>

    @POST("driver/accepttrip")
    @Headers("Content-Type: application/json")
    fun accepttrip(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<AcceptrideResponse>


    @POST("driver/todayhistory")
    @Headers("Content-Type: application/json")
    fun TodayHistory(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<TodayEarningResponse>

    @POST("driver/allhistory")
    @Headers("Content-Type: application/json")
    fun AllHistory(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<TodayEarningResponse>


    @POST("driver/notification")
    @Headers("Content-Type: application/json")
    fun notification(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<notificationResponse>


    @POST("driver/offlinehistory")
    @Headers("Content-Type: application/json")
    fun offlinedetailsApi(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<DriverOfflineDetailResponse>

    @GET("driver/cancelreasondata")
    @Headers("Content-Type: application/json")
    fun getCancelReason(
    ): Call<CancelReasonResponse>

    @POST("driver/completetrip")
    @Headers("Content-Type: application/json")
    fun completeRide(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<CompleteBookingResponse>
}