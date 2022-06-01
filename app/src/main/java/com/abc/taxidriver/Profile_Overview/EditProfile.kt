package com.abc.taxidriver.Profile_Overview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.API.APIUtils
import com.abc.taxidriver.Profile_Overview.ModelEditImage.UpdatePhotoResponse
import com.abc.taxidriver.Profile_Overview.ModelEditImage.UpdateProfileDetailResponse
import com.abc.taxidriver.Profile_Overview.ModelEditProfile.EditProfileResponse
import com.abc.taxidriver.R
import com.abc.taxidriver.Utils.Camerautils.FileCompressor
import com.abc.taxidriver.Utils.Camerautils.PermissionUtils
import com.abc.taxidriver.Utils.Camerautils.Utility
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.NetworkUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import com.abc.taxidriver.Utils.StringUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.etAddress
import kotlinx.android.synthetic.main.activity_edit_profile.etEmail
import kotlinx.android.synthetic.main.activity_edit_profile.etLicence
import kotlinx.android.synthetic.main.activity_edit_profile.etName
import kotlinx.android.synthetic.main.activity_edit_profile.ivProfile
import kotlinx.android.synthetic.main.activity_edit_profile.iv_camera
import kotlinx.android.synthetic.main.activity_edit_profile.rlLicence
import kotlinx.android.synthetic.main.activity_profile_overview.*

import kotlinx.android.synthetic.main.header.*
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
import kotlin.collections.ArrayList

class EditProfile : AppCompatActivity() {

    var licence = ""

    var currentPhotoPath = ""
    var userChoosenTask = ""
    var profileimage = ""
    var REQUEST_CODE: Int = 0
    var filepath_presc: Uri? = null
    var profile_photo = ""
    var licence_photo = ""
    var driver_id = ""
    var profile_profile: String = ""
    var driving_license: String = ""
    var vehicleLicencePath = ""
    var image1path = ""
    var vehicleno = ""
    var vehicletye = ""

    var mCompressor: FileCompressor? = null

    private var CAMERA_REQUEST: Int = 1
    private var PICK_IMAGE_REQUEST: Int = 1
    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        mCompressor = FileCompressor(this)
        val languages = resources.getStringArray(R.array.gender)

        var genderlist = ArrayList<String>()
        genderlist.add("Select Gender")
        genderlist.add("Male")
        genderlist.add("Female")
        val arrayAdapter1 = ArrayAdapter(this, R.layout.dropdown_item, genderlist)
        spGender.adapter = arrayAdapter1

        SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.GENDER, "")
            ?.let { Log.d("Gender", it) }
        if (SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.GENDER, ""
            ).equals("Male")
        ) {
            spGender.setSelection(1)

        } else if (SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.GENDER, ""
            ).equals("Female")
        ) {

            spGender.setSelection(2)

        }

        onClick()

        init()
    }


    private fun init() {
        etName.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.NAME, ""
            )
        )
        etMobileNum.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.PHONE_NO, ""
            )
        )
        etEmail.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.EMAIL, ""
            )
        )
        etAddress.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.ADDRESS, ""
            )
        )
        etVehicletype.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.VEHICLE_TYPE, ""
            )
        )
        etVehicleno.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.VEHICLE_NO, ""
            )
        )
        try {
            val picasso = Picasso.get()
            picasso.load(
                SharedPreferenceUtils.getInstance(this)
                    ?.getStringValue(ConstantUtils.PROFILE_PHOTO, "")
            ).placeholder(R.drawable.default_profile).into(ivProfile)
        } catch (e: java.lang.Exception) {

        }
        etLicence.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.DRIVING_LICENSE, ""
            )

        )
        etVehicleLicence.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.VEHICLE_LICENSE, ""
            )

        )
    }

    private fun onClick() {
        iv_camera.setOnClickListener {
            type = "camera"
            getPermissions()
        }

        rlLicence.setOnClickListener {
            type = "licence"
            getPermissions()
        }
        rlVehicleLicence.setOnClickListener {
            type = "vehiclelicence"
            getPermissions()
        }

        tvTitle.setText("Profile Edit")

        rlBack.setOnClickListener {
            onBackPressed()
        }

        tvSave.setOnClickListener() {
            vehicleno=etVehicleno.text.toString()
            vehicletye=etVehicletype.text.toString()

            if (etName.text.toString().isNullOrEmpty()) {

                Toast.makeText(
                    this, resources.getString(R.string.plzname),
                    Toast.LENGTH_LONG
                ).show()

            } else if (etEmail.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(R.string.plzemail),
                    Toast.LENGTH_LONG
                ).show()

            } else if (!StringUtil.isEmailValid(etEmail.text.toString())) {
                Toast.makeText(
                    this, resources.getString(R.string.plzevalidmail),
                    Toast.LENGTH_LONG
                ).show()

            }/* else if (etAddress.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(R.string.plzaddress),
                    Toast.LENGTH_LONG
                ).show()
            } else if (spGender.selectedItemPosition == 0) {
                Toast.makeText(
                    this, resources.getString(R.string.plzgender),
                    Toast.LENGTH_LONG
                ).show()
            }*/else if (vehicletye.isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(R.string.plzevehicletype),
                    Toast.LENGTH_LONG
                ).show()

            }else if (vehicleno.isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(R.string.plzevehicleno),
                    Toast.LENGTH_LONG
                ).show()

            } else {
                if (NetworkUtils.checkInternetConnection(this)) {
                    editprofile()
                }
            }
        }


    }

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

            }/* else if (items[item] == "Cancel") {
                dialog.dismiss()
            }*/
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
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
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
            if (type.equals("camera")) {
                profile_photo = imgFile.path
                profileimage = getStringImage(bitmap)
                ivProfile.setImageBitmap(bitmap)
                uploadProfileImage()
            }else if (type.equals("licence")) {
                licence_photo = imgFile.path
                etLicence.setText(imgFile.name)
            } else{
                vehicleLicencePath = imgFile.path
                etVehicleLicence.setText(imgFile.name)
            }
        }
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
                    bitmap = mCompressor!!.compressToBitmap(file)


                    if (type.equals("camera")) {
                        profile_photo = file.path
                        profileimage = bitmap?.let { getStringImage(it) }.toString()
                        ivProfile.setImageBitmap(bitmap)
                        uploadProfileImage()
                    } else if (type.equals("licence")) {
                        licence_photo = file.path
                        etLicence.setText(file.name)
                    } else{
                        vehicleLicencePath = file.path
                        etVehicleLicence.setText(file.name)
                    }

                    //  uploadImage()
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

    private fun uploadProfileImage() {

        val multiPartRepeatString = "application/image"
        var image1: MultipartBody.Part? = null


        val driver_id: RequestBody = RequestBody.create(
            MultipartBody.FORM, SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.DRIVER_ID, ""
            ).toString()
        )


        Log.e(
            "driverid", SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.DRIVER_ID, ""
            ).toString()
        )
        if (!profile_photo.isNullOrEmpty()) {
            val file = File(profile_photo)
            val signPicBody =
                RequestBody.create(multiPartRepeatString.toMediaTypeOrNull(), file)
            image1 = MultipartBody.Part.createFormData("profile_photo", file.name, signPicBody)
        }

        val dialog: ProgressDialog = ProgressDialog.show(this, null, "Please Wait")

        var call = APIUtils.getServiceAPI()!!.editDriProfileApi(driver_id, image1)

        call.enqueue(object : Callback<UpdateProfileDetailResponse> {
            override fun onResponse(
                call: Call<UpdateProfileDetailResponse>,
                response: Response<UpdateProfileDetailResponse>
            ) {
                try {

                    if (response.code() == 200) {

                        if (response.body()!!.success == "true") {

                            if (!response.body()!!.data[0].profile_photo.isNullOrEmpty()) {

                                SharedPreferenceUtils.getInstance(this@EditProfile)
                                    ?.setStringValue(
                                        ConstantUtils.PROFILE_PHOTO,
                                        response.body()!!.data[0].profile_photo
                                    )

                            }


                            dialog.dismiss()

                        } else {

                            dialog.dismiss()
                        }
                    } else {

                    }
                } catch (e: Exception) {
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateProfileDetailResponse>, t: Throwable) {
                dialog.dismiss()

            }
        })

    }

    private fun editprofile() {

        rlLoaderEdit.visibility = View.VISIBLE
        val multiPartRepeatString = "application/image"
        var image1: MultipartBody.Part? = null
        var vehicelLicenceImage: MultipartBody.Part? = null

        val name: RequestBody = RequestBody.create(
            MultipartBody.FORM, etName.text.toString()
        )

        val email: RequestBody = RequestBody.create(
            MultipartBody.FORM, etEmail.text.toString()
        )
        val address: RequestBody = RequestBody.create(
            MultipartBody.FORM, etAddress.text.toString()
        )
        val gender: RequestBody = RequestBody.create(
            MultipartBody.FORM, spGender.selectedItem.toString()
        )

        val driver_id: RequestBody = RequestBody.create(
            MultipartBody.FORM,
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.DRIVER_ID, "")
                .toString()
        )
        val vehicle_type: RequestBody = RequestBody.create(
            MultipartBody.FORM, vehicletye
        )
        val vehicle_no: RequestBody = RequestBody.create(
            MultipartBody.FORM, vehicleno
        )
        var driving_licenseImage: MultipartBody.Part? = null
        if (!licence_photo.isNullOrEmpty()) {
            val file = File(licence_photo)

            val signPicBody =
                RequestBody.create(multiPartRepeatString.toMediaTypeOrNull(), file)
            driving_licenseImage =
                MultipartBody.Part.createFormData("profile_photo", file.name, signPicBody)

        }
        if (!vehicleLicencePath.isNullOrEmpty()) {
            val file = File(vehicleLicencePath)
            val signPicBody =
                RequestBody.create(multiPartRepeatString.toMediaTypeOrNull(), file)
            vehicelLicenceImage = MultipartBody.Part.createFormData("vehicle_licence", file.name, signPicBody)
        }
        var call = APIUtils.getServiceAPI()!!.drivereditprofileApi(
            name,
            email,
            address,
            gender,
            driver_id,
            vehicle_type,
            vehicle_no,
            driving_licenseImage,
            vehicelLicenceImage
            )

        call.enqueue(object : Callback<UpdateProfileDetailResponse> {
            override fun onResponse(
                call: Call<UpdateProfileDetailResponse>,
                response: Response<UpdateProfileDetailResponse>
            ) {
                try {
                    rlLoaderEdit.visibility = View.GONE
                    if (response.body()!!.success.equals("true")) {

                        if (!response.body()!!.data[0].name.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@EditProfile)
                                ?.setStringValue(ConstantUtils.NAME, response.body()!!.data[0].name)
                        }
                        if (!response.body()!!.data[0].email.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@EditProfile)
                                ?.setStringValue(ConstantUtils.EMAIL, response.body()!!.data[0].email)
                        }
                        if (!response.body()!!.data[0].address.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@EditProfile)
                                ?.setStringValue(
                                    ConstantUtils.ADDRESS,
                                    response.body()!!.data[0].address
                                )
                        }

                        if (!response.body()!!.data[0].driving_license.isNullOrEmpty()) {
                           var path=response.body()!!.data[0].driving_license.split("/").toTypedArray()

                            SharedPreferenceUtils.getInstance(this@EditProfile)
                                ?.setStringValue(
                                    ConstantUtils.DRIVING_LICENSE,
                                    path[path.size-1]
                                )
                        }
                        if (!response.body()!!.data[0].vehicle_permit.isNullOrEmpty()) {
                            var path=response.body()!!.data[0].vehicle_permit.split("/").toTypedArray()
                            SharedPreferenceUtils.getInstance(this@EditProfile)
                                ?.setStringValue(
                                    ConstantUtils.VEHICLE_LICENSE,
                                    path[path.size-1]
                                )
                        }
                        if (!response.body()!!.data[0].gender.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@EditProfile)
                                ?.setStringValue(
                                    ConstantUtils.GENDER,
                                    response.body()!!.data[0].gender
                                )
                        }
                        SharedPreferenceUtils.getInstance(this@EditProfile)
                            ?.setStringValue(
                                ConstantUtils.VEHICLE_TYPE,
                                vehicletye
                            )
                        SharedPreferenceUtils.getInstance(this@EditProfile)
                            ?.setStringValue(
                                ConstantUtils.VEHICLE_NO,
                                vehicleno
                            )
                        onBackPressed()


                    } else {

                        rlLoaderEdit.visibility = View.GONE
                    }

                } catch (e: Exception) {
                    Log.e("nitish", e.toString())
                    Toast.makeText(this@EditProfile, e.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    rlLoaderEdit.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<UpdateProfileDetailResponse>, t: Throwable) {
                rlLoaderEdit.visibility = View.GONE
                Log.e("nitish", t.message.toString())
                Toast.makeText(
                    this@EditProfile,
                    t.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}