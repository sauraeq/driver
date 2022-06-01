package com.abc.taxidriver.Signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.API.APIUtils
import com.abc.taxidriver.Otp.Otp
import com.abc.taxidriver.R
import com.abc.taxidriver.Signup.Model.SigninResponse
import com.abc.taxidriver.Signup.Model.SignupResponse
import com.abc.taxidriver.Terms_Condition.TermsCondition
import com.abc.taxidriver.Utils.Camerautils.FileCompressor
import com.abc.taxidriver.Utils.Camerautils.PermissionUtils
import com.abc.taxidriver.Utils.Camerautils.Utility
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.NetworkUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import com.abc.taxidriver.Utils.StringUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SignupActivity : AppCompatActivity() {
    var mobile = ""

    var image = ""

    var currentPhotoPath = ""
    var userChoosenTask = ""
    var profileimage = ""
    var REQUEST_CODE: Int = 0
    var filepath_presc: Uri? = null
    var driverLicencePath = ""
    var vehicleLicencePath = ""

    var mCompressor: FileCompressor? = null
    private var CAMERA_REQUEST: Int = 1
    private var PICK_IMAGE_REQUEST: Int = 1

    var gender: String = ""
    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton
    var radioText = ""
    var imageType = ""
    var vehicleno = ""
    var vehicletye = ""
    var fcmToken = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()
        mCompressor = FileCompressor(this)
        getFCMToken()
        onClick()
        click()
        radioGroup = findViewById(R.id.rgGender)
        var txt_1 = findViewById<TextView>(R.id.tvSignUpTab1)
        var txt_2 = findViewById<TextView>(R.id.tvSiginTab2)
        var txt_3 = findViewById<TextView>(R.id.tvSignUpTab3)
        var txt_4 = findViewById<TextView>(R.id.tvSiginTab4)
        var separator = findViewById<View>(R.id.separator)

        var linear_sign_up = findViewById<LinearLayout>(R.id.singnup_linear)
        var linear_sign_in = findViewById<LinearLayout>(R.id.signIn_linear)
        var linear_sign_up_content = findViewById<LinearLayout>(R.id.signUp_content)
        var Linear_sign_in_content = findViewById<LinearLayout>(R.id.signin_content)
        var Linear_sign_Up_Text = findViewById<LinearLayout>(R.id.Sign_Up_Text_linear)
        var Linear_soical_media_icon = findViewById<LinearLayout>(R.id.Social_media_Linear)
        var Linear_terms_condition = findViewById<LinearLayout>(R.id.term_Linear)


        linear_sign_up.setVisibility(View.GONE)
        separator.setVisibility(View.VISIBLE)
        linear_sign_up_content.setVisibility(View.GONE)
        Linear_sign_in_content.setVisibility(View.VISIBLE)
        linear_sign_in.setVisibility(View.VISIBLE)
        Linear_soical_media_icon.setVisibility(View.GONE)
        Linear_sign_Up_Text.setVisibility(View.GONE)
        Linear_terms_condition.setVisibility(View.GONE)

        txt_4.setOnClickListener {

            linear_sign_up.setVisibility(View.GONE)
            linear_sign_up_content.setVisibility(View.GONE)
            Linear_sign_in_content.setVisibility(View.VISIBLE)
            linear_sign_in.setVisibility(View.VISIBLE)
           // Linear_soical_media_icon.setVisibility(View.GONE)
           // Linear_sign_Up_Text.setVisibility(View.GONE)
            Linear_terms_condition.setVisibility(View.GONE)

        }
        txt_1.setOnClickListener {

            linear_sign_in.setVisibility(View.GONE)
            Linear_sign_in_content.setVisibility(View.GONE)
            linear_sign_up.setVisibility(View.VISIBLE)
            linear_sign_up_content.setVisibility(View.VISIBLE)
           // Linear_soical_media_icon.setVisibility(View.VISIBLE)
           // Linear_sign_Up_Text.setVisibility(View.VISIBLE)
            Linear_terms_condition.setVisibility(View.VISIBLE)
        }
        txt_2.setOnClickListener {
            linear_sign_in.setVisibility(View.VISIBLE)
            separator.setVisibility(View.VISIBLE)
            linear_sign_up.setVisibility(View.GONE)
            linear_sign_up_content.setVisibility(View.GONE)
            Linear_sign_in_content.setVisibility(View.VISIBLE)
            linear_sign_in.setVisibility(View.VISIBLE)
         //   Linear_soical_media_icon.setVisibility(View.GONE)
           // Linear_sign_Up_Text.setVisibility(View.GONE)
            Linear_terms_condition.setVisibility(View.GONE)


        }
        txt_3.setOnClickListener {
            linear_sign_in.setVisibility(View.GONE)
            separator.setVisibility(View.GONE)
            linear_sign_up.setVisibility(View.VISIBLE)
            Linear_sign_in_content.setVisibility(View.GONE)
            linear_sign_up.setVisibility(View.VISIBLE)
            linear_sign_up_content.setVisibility(View.VISIBLE)
          //  Linear_soical_media_icon.setVisibility(View.VISIBLE)
           // Linear_sign_Up_Text.setVisibility(View.VISIBLE)
            Linear_terms_condition.setVisibility(View.VISIBLE)
        }

        rgGender.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                val id = group.checkedRadioButtonId
                val rb = findViewById<View>(checkedId) as RadioButton
                radioText = rb.text.toString()

            }
        })

    }

    fun getFCMToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                   // Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token

                fcmToken=task.result



            })
    }
    private fun click() {

        tvNext.setOnClickListener {

            mobile = etPhoneNum.text.toString()

            if (mobile.isEmpty()) {
                Toast.makeText(
                    this,
                    resources.getString(com.abc.taxidriver.R.string.plzmobile),
                    Toast.LENGTH_LONG
                ).show()
            }

            else if (NetworkUtils.checkInternetConnection(this)) {
                Login()
            }
        }
    }

    private fun onClick() {
        rlDeiverLicence.setOnClickListener {
           imageType="driver"
            getPermissions()

        }
        tvTerms.setOnClickListener {
            var intent = Intent(this, TermsCondition::class.java)
            startActivity(intent)

        }
        rlVehicleLicence.setOnClickListener {
            imageType="vehicle"
            getPermissions()

        }
        //----Signup Api-----//

        tv_Signup.setOnClickListener() {
              vehicleno=etVehicleno.text.toString()
              vehicletye=etVehicletype.text.toString()
            if (etName.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzdrivename),
                    Toast.LENGTH_LONG
                ).show()

            } else if (etMobile.text.toString().toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzmobile),
                    Toast.LENGTH_LONG
                ).show()
            }else if (etEmail.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzemail),
                    Toast.LENGTH_LONG
                ).show()

            }else if (!StringUtil.isEmailValid(etEmail.text.toString())) {
                Toast.makeText(
                    this, resources.getString(R.string.plzevalidmail),
                    Toast.LENGTH_LONG
                ).show()

            }else if (vehicletye.isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(R.string.plzevehicletype),
                    Toast.LENGTH_LONG
                ).show()

            }else if (vehicleno.isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(R.string.plzevehicleno),
                    Toast.LENGTH_LONG
                ).show()

            }/* else if (etAddress.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzaddress),
                    Toast.LENGTH_LONG
                ).show()

            } */else if (etDriverLicence.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzdriverlicence),
                    Toast.LENGTH_LONG
                ).show()

            }else if (etVehicleLicence.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzvehlicence),
                    Toast.LENGTH_LONG
                ).show()

            }else if (!ckTerms.isChecked) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzterms),
                    Toast.LENGTH_LONG
                ).show()

            } /*  else if (radioText.isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzgender),
                    Toast.LENGTH_LONG
                ).show()
            }*/ else {
                if (NetworkUtils.checkInternetConnection(this)) {
                    signup()
                }

            }
        }
    }

    private fun signup() {

        rlLoader.visibility = View.VISIBLE
        val multiPartRepeatString = "application/image"
        var driverLicenceImage: MultipartBody.Part? = null
        var vehicelLicenceImage: MultipartBody.Part? = null
        val device_tokanid: RequestBody = RequestBody.create(
            MultipartBody.FORM, fcmToken
        )

        val name: RequestBody = RequestBody.create(
            MultipartBody.FORM, etName.text.toString()
        )
        val phone_no: RequestBody = RequestBody.create(
            MultipartBody.FORM, etMobile.text.toString()
        )

        val email: RequestBody = RequestBody.create(
            MultipartBody.FORM, etEmail.text.toString()
        )
        val address: RequestBody = RequestBody.create(
            MultipartBody.FORM, etAddress.text.toString()
        )

        val gender: RequestBody = RequestBody.create(
            MultipartBody.FORM, radioText
        )
        val vehicle_type: RequestBody = RequestBody.create(
            MultipartBody.FORM, vehicletye
        )
        val vehicle_no: RequestBody = RequestBody.create(
            MultipartBody.FORM, vehicleno
        )

        val language: RequestBody = RequestBody.create(
            MultipartBody.FORM, SharedPreferenceUtils.getInstance(this)
                ?.getStringValue(ConstantUtils.LANGUAG, "").toString()
        )

        if (!driverLicencePath.isNullOrEmpty()) {
            val file = File(driverLicencePath)
            val signPicBody =
                RequestBody.create(multiPartRepeatString.toMediaTypeOrNull(), file)
            driverLicenceImage = MultipartBody.Part.createFormData("licence", file.name, signPicBody)
        }
        if (!vehicleLicencePath.isNullOrEmpty()) {
            val file = File(vehicleLicencePath)
            val signPicBody =
                RequestBody.create(multiPartRepeatString.toMediaTypeOrNull(), file)
            vehicelLicenceImage = MultipartBody.Part.createFormData("vehicle_licence", file.name, signPicBody)
        }
        var call = APIUtils.getServiceAPI()!!.signUp(
            device_tokanid,
            name,
            phone_no,
            email,
            address,
            gender,
            language,
            vehicle_type,
            vehicle_no,
            driverLicenceImage,
            vehicelLicenceImage
            )

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                try {
                    rlLoader.visibility = View.GONE
                    if (response.body()!!.success.equals("true")) {

                        if (!response.body()!!.data.name.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(ConstantUtils.NAME, response.body()!!.data.name)
                        }
                        if (!response.body()!!.data.email.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(ConstantUtils.EMAIL, response.body()!!.data.email)
                        }
                        if (!response.body()!!.data.address.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.ADDRESS,
                                    response.body()!!.data.address
                                )
                        }
                        SharedPreferenceUtils.getInstance(this@SignupActivity)
                            ?.setStringValue(
                                ConstantUtils.PHONE_NO,
                                etMobile.text.toString()
                            )
                        SharedPreferenceUtils.getInstance(this@SignupActivity)?.getStringValue(ConstantUtils.PHONE_NO, "")
                            ?.let { Log.d("mobileno", it) }
                        if (!response.body()!!.data.gender.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.GENDER,
                                    response.body()!!.data.gender
                                )
                        }

                        SharedPreferenceUtils.getInstance(this@SignupActivity)
                            ?.setStringValue(
                                ConstantUtils.DRIVING_LICENSE,
                                etDriverLicence.text.toString()
                            )
                        SharedPreferenceUtils.getInstance(this@SignupActivity)
                            ?.setStringValue(
                                ConstantUtils.VEHICLE_LICENSE,
                                etVehicleLicence.text.toString()
                            )

                        if (!response.body()!!.data.driver_id.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.DRIVER_ID,
                                    response.body()!!.data.driver_id
                                )
                        }
                        if (!response.body()!!.data.profile_photo.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.PROFILE_PHOTO,
                                    response.body()!!.data.profile_photo
                                )
                        }
                        SharedPreferenceUtils.getInstance(this@SignupActivity)
                            ?.setStringValue(
                                ConstantUtils.VEHICLE_TYPE,
                                vehicletye
                            )
                        SharedPreferenceUtils.getInstance(this@SignupActivity)
                            ?.setStringValue(
                                ConstantUtils.VEHICLE_NO,
                                vehicleno
                            )
                        Toast.makeText(
                            this@SignupActivity,
                            response.body()!!.data.otp,
                            Toast.LENGTH_LONG
                        ).show()
                        var intent = Intent(this@SignupActivity, Otp::class.java)
                        intent.putExtra("otp", response.body()!!.data.otp)
                        startActivity(intent)

                    } else {

                        rlLoader.visibility = View.GONE
                    }

                } catch (e: Exception) {
                    Log.e("nitish", e.toString())
                    Toast.makeText(this@SignupActivity, e.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    rlLoader.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                rlLoader.visibility = View.GONE
                Log.e("nitish", t.message.toString())
                Toast.makeText(
                    this@SignupActivity,
                    "Mobile number is already exist",
                    Toast.LENGTH_LONG
                ).show()

            }

        })

    }

    //-----Login Api-----//

    fun Login() {
        val request = HashMap<String, String>()
        request.put("mobile", mobile)
        request.put("device_tokanid", fcmToken)
        rlLoader.visibility = View.VISIBLE

        var login: Call<SigninResponse> =
            APIUtils.getServiceAPI()!!.driverLoginOtpApi(request)



        login.enqueue(object : Callback<SigninResponse> {
            override fun onResponse(
                call: Call<SigninResponse>,
                response: Response<SigninResponse>
            ) {
                try {

                    rlLoader.visibility = View.GONE
                    if (response.body()!!.success.equals("true")) {
                        Toast.makeText(
                            this@SignupActivity,
                            response.body()!!.data.otp,
                            Toast.LENGTH_LONG
                        ).show()
                        if (!response.body()!!.data.id.isNullOrEmpty()) {

                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.DRIVER_ID, response.body()!!.data.id
                                )
                        }
                        SharedPreferenceUtils.getInstance(this@SignupActivity)
                            ?.setStringValue(
                                ConstantUtils.PHONE_NO,
                                mobile
                            )

                        if (!response.body()!!.data.name.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.NAME, response.body()!!.data.name
                                )
                        }
                        if (!response.body()!!.data.email.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.EMAIL, response.body()!!.data.email
                                )
                        }

                        if (!response.body()!!.data.address.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.ADDRESS, response.body()!!.data.address
                                )
                        }
                        if (!response.body()!!.data.gender.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.GENDER, response.body()!!.data.gender
                                )
                        }

                        if (!response.body()!!.data.profile_photo.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.PROFILE_PHOTO,
                                    response.body()!!.data.profile_photo
                                )
                        }
                        if (!response.body()!!.data.vehicle_name.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.VEHICLE_TYPE,
                                    response.body()!!.data.vehicle_name
                                )
                        }
                        if (!response.body()!!.data.vehicle_no.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.VEHICLE_NO,
                                    response.body()!!.data.vehicle_no
                                )
                        }
                        if (!response.body()!!.data.driving_license.isNullOrEmpty()) {
                            var path=response.body()!!.data.driving_license.split("/").toTypedArray()

                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.DRIVING_LICENSE,
                                    path[path.size-1]
                                )
                        }
                        if (!response.body()!!.data.vehicle_permit.isNullOrEmpty()) {
                            var path=response.body()!!.data.vehicle_permit.split("/").toTypedArray()
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.VEHICLE_LICENSE,
                                    path[path.size-1]
                                )
                        }

                        var intent = Intent(this@SignupActivity, Otp::class.java)
                        intent.putExtra("otp", response.body()!!.data.otp)
                        startActivity(intent)

                    } else {

                        Toast.makeText(
                            this@SignupActivity, response.body()!!.msg, Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    Log.e("Nitish", e.toString())
                    Toast.makeText(
                        this@SignupActivity, response.body()!!.msg, Toast.LENGTH_LONG
                    ).show()
                    rlLoader.visibility = View.GONE

                }

            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Log.e("Nitish", t.message.toString())
                rlLoader.visibility = View.GONE
                Toast.makeText(this@SignupActivity,"this phone number does not exist",Toast.LENGTH_LONG).show()

            }

        })
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    //----- For Image Upload-----//

    fun getPermissions() {
        if (PermissionUtils.checkPermission(
                this as Activity,
                Manifest.permission.CAMERA,
                REQUEST_CODE
            )
        ) {
            if (PermissionUtils.checkPermission(
                    this as Activity?,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    REQUEST_CODE
                )
            ) {
                if (PermissionUtils.checkPermission(
                        this as Activity?,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        REQUEST_CODE
                    )
                ) {
                    selectImage()
                }
            }
        }
    }

    private fun selectImage() {

        val items = arrayOf<CharSequence>("Take photo", "Choose from gallery")
        val builder = AlertDialog.Builder(this as Activity)
        builder.setTitle("Add Photo")
        builder.setCancelable(true)
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this)
            if (items[item] == "Take photo") {
                userChoosenTask = "Take photo"
                if (result) {
                    dispatchTakePictureIntent()

                }

            } else if (items[item] == "Choose from gallery") {
                userChoosenTask = "Choose from gallery"
                if (result) {
                    openGallery()
                }
            }
        }
        builder.show()
    }

    fun openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )

        } else {
            if (Build.VERSION.SDK_INT <= 19) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_REQUEST
                )
            } else if (Build.VERSION.SDK_INT > 19) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_REQUEST
                )
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    getApplicationContext()?.getPackageName() + ".fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath()
        Log.d("photopath", currentPhotoPath)
        return image
    }

    fun onselectfromcamera1() {
        //  try {
        var bitmap: Bitmap? = null
        var imgFile = File(currentPhotoPath)
        Log.d("flow1", "dfdf")
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgFile = mCompressor!!.compressToFile(imgFile)
            bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
             if(imageType.equals("driver")){
                 driverLicencePath = imgFile.path
                 etDriverLicence.setText(imgFile.name)
             }else{
                 vehicleLicencePath = imgFile.path
                 etVehicleLicence.setText(imgFile.name)
             }

            profileimage = getStringImage(bitmap)

            //  uploadImage(imgFile.path)
            // upladImage()
        }
        Log.d("flow2", "dfdf")
    }


    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    fun onselectfromgallery(data: Intent) {
        filepath_presc = data.data
        if (filepath_presc.toString() == null) {

        } else {
            try {
                Log.e("filepath", filepath_presc.toString() + "")
                var bitmap: Bitmap? = null

                var file = File(filepath_presc?.let { getRealPathFromUri(it) })
                try {
                    file = mCompressor!!.compressToFile(file)
                    etDriverLicence.setText(file.name)
                    bitmap = mCompressor!!.compressToBitmap(file)
                    if(imageType.equals("driver")){
                        driverLicencePath = file.path
                        etDriverLicence.setText(file.name)
                    }else{
                        vehicleLicencePath = file.path
                        etVehicleLicence.setText(file.name)
                    }
                    profileimage = bitmap?.let { getStringImage(it) }.toString()
                    //    etLicence.setImageBitmap(bitmap)
                    //  upladImage()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Log.e("exception1", e.toString())
                }

            } catch (e: java.lang.Exception) {
                Log.e("exception", e.toString())
                e.printStackTrace()
            }
        }
    }


    @SuppressLint("Range")
    fun getRealPathFromUri(uri: Uri): String? {
        var result = ""
        val documentID: String
        documentID = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            val pathParts = uri.path!!.split("/".toRegex()).toTypedArray()
            pathParts[pathParts.size - 1]
        } else {
            val pathSegments = uri.lastPathSegment!!.split(":".toRegex()).toTypedArray()
            pathSegments[pathSegments.size - 1]
        }
        val mediaPath = MediaStore.Images.Media.DATA
        val imageCursor: Cursor? = contentResolver.query(
            uri,
            arrayOf(mediaPath),
            MediaStore.Images.Media._ID + "=" + documentID,
            null,
            null
        )
        if (imageCursor!!.moveToFirst()) {
            result = imageCursor.getString(imageCursor.getColumnIndex(mediaPath))
        }
        return result
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.data != null) {
            Log.e("filename", data.toString() + "")
            onselectfromgallery(data)
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            onselectfromcamera1()
        }
    }


}





