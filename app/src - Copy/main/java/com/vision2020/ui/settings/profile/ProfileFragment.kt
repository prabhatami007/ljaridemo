package com.vision2020.ui.teacher.profile

import `in`.mayanknagwanshi.imagepicker.ImageSelectActivity
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.vision2020.R
import com.vision2020.data.request.EditProfileReq
import com.vision2020.data.response.ProfileResponse
import com.vision2020.data.response.UpdateProfileResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_sign_up.imgViewCamera
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern


class ProfileFragment :  Fragment() {

    val tvEmail=""
    val tvFirstName=""
    val tvLastName=""
    val tvPhone=""
    val tvAddress=""
    val tvUserType=""
    var filePath: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {



        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvFirstName = view.findViewById<TextView>(R.id.tvfirstName)
        val tvLastName = view.findViewById<TextView>(R.id.tvLastName)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        val tvUserType = view.findViewById<TextView>(R.id.tvUserType)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val imageViewProfile = view.findViewById<ImageView>(R.id.imgViewUserPic)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)
        val btnSaveProfile = view.findViewById<Button>(R.id.btnSaveProfile)
//        val tv1 = view.findViewById<TextView>(R.id.board_status_tv1)


        val editFirstName = view.findViewById<EditText>(R.id.editfirstName)
        val editLastName = view.findViewById<EditText>(R.id.editLastName)
        val editPhone = view.findViewById<EditText>(R.id.editPhone)
        val editAddress = view.findViewById<EditText>(R.id.editAddress)


        editPhone.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )


        val token = getAppPref().getString(AppConstant.KEY_TOKEN)
        RetrofitClient.instance!!.getUserData(token!!).enqueue(object : Callback<ProfileResponse> {

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                //progress!!.dismiss()
                // t.message?.let { mContext.showToastMsg(it,1) }

                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>) {

                Log.d("Response::::", response.body()!!.status_code.toString())


                if (response.body()!!.status_code.toInt()==AppConstant.CODE_SUCCESS){
                    //progress!!.dismiss()
                    // mContext.showToastMsg(response.body()!!.data.toString(),1)
                    if(response.body()!!.data.user_type==1) {
                        tvUserType.setText("Student")
                    }
                    if(response.body()!!.data.user_type==2) {
                        tvUserType.setText("Teacher")
                    }



                    Picasso.get().load(response.body()!!.data.photo).into(imageViewProfile)

                    tvEmail.setText(response.body()!!.data.email)

                    textCNView.setText(response.body()!!.data.first_name+" "+response.body()!!.data.last_name)

                    tvUserName.setText(response.body()!!.data.email)
                    tvFirstName.setText(response.body()!!.data.first_name)
                    tvLastName.setText(response.body()!!.data.last_name)
                    tvAddress.setText(response.body()!!.data.address)
                    //tvUserType.setText(response.body()!!.data.email)
                    tvPhone.setText(response.body()!!.data.phone)

                    editFirstName.setText(response.body()!!.data.first_name)
                    editLastName.setText(response.body()!!.data.last_name)
                    editAddress.setText(response.body()!!.data.address)
                    //tvUserType.setText(response.body()!!.data.email)
                    editPhone.setText(response.body()!!.data.phone)



                    //Toast.makeText(mContext, response.body()!!.data.email, Toast.LENGTH_LONG).show()

                }else{
                    //progress!!.dismiss()
                    // mContext.showToastMsg(response.body()!!.message,1)
                    Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG).show()
                    //Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                }
            }



        })


        btnEditProfile.setOnClickListener {

            btnSaveProfile.visibility=View.VISIBLE
            btnEditProfile.visibility=View.INVISIBLE

            tvFirstName.visibility=View.INVISIBLE
            editFirstName.visibility=View.VISIBLE

            tvAddress.visibility=View.INVISIBLE
            editAddress.visibility=View.VISIBLE

            tvLastName.visibility=View.INVISIBLE
            editLastName.visibility=View.VISIBLE

            tvPhone.visibility=View.INVISIBLE
            editPhone.visibility=View.VISIBLE

            imgViewCamera.visibility=View.VISIBLE

        }


        btnSaveProfile.setOnClickListener {

            val pattern =
                "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"
            var m: Matcher
            val r: Pattern = Pattern.compile(pattern)
            m = r.matcher(editPhone.getText().toString().trim());

            val maxLength = 10
            val FilterArray = arrayOfNulls<InputFilter>(1)
            FilterArray[0] = LengthFilter(maxLength)
            editPhone.setFilters(FilterArray)

            when {
                editFirstName.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(getString(R.string.error_first_name), 2)
                }
                editAddress.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(getString(R.string.error_address), 2)
                }
                editLastName.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(getString(R.string.error_last_name), 2)
                }
                editPhone.text!!.trim().isEmpty()  -> {

                     activity?.showToastMsg(getString(R.string.error_phone), 2)
                }
                editPhone.getText().toString().length <10 || editPhone.getText().toString().length >10  -> {

                    activity?.showToastMsg(getString(R.string.error_phone_length), 2)
                }
                /*m.find() -> {
                    activity?.showToastMsg(getString(R.string.error_phone), 2)
                }*/

                else -> {


                    filePath?.let { it1 ->
                        createBuilder(
                            it1,EditProfileReq(
                                token,editFirstName.text.toString(),editLastName.text.toString(),editAddress.text.toString(),editPhone.text.toString()
                            ))
                    }?.let { it2 ->
                        RetrofitClient.instance!!.userEditProfile(it2).enqueue(object  : Callback<UpdateProfileResponse> {
                            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                                // returnValue(UpdateProfileResponse(t.message!!))
                            }

                            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                                if (response.body()!!.status_code.toInt()==AppConstant.CODE_SUCCESS){


                                    btnSaveProfile.visibility=View.INVISIBLE
                                    btnEditProfile.visibility=View.VISIBLE

                                    tvFirstName.visibility=View.VISIBLE
                                    editFirstName.visibility=View.INVISIBLE

                                    tvAddress.visibility=View.VISIBLE
                                    editAddress.visibility=View.INVISIBLE

                                    tvLastName.visibility=View.VISIBLE
                                    editLastName.visibility=View.INVISIBLE

                                    tvPhone.visibility=View.VISIBLE
                                    editPhone.visibility=View.INVISIBLE

                                    imgViewCamera.visibility=View.INVISIBLE

                                    //tvEmail.setText(response.body()!!.data.email)
                                    // tvUserName.setText(response.body()!!.data.email)
                                    textCNView.setText(response.body()!!.data.first_name+" "+response.body()!!.data.last_name)
                                    tvFirstName.setText(response.body()!!.data.first_name)
                                    tvLastName.setText(response.body()!!.data.last_name)
                                    tvAddress.setText(response.body()!!.data.address)
                                    //tvUserType.setText(response.body()!!.data.email)
                                    tvPhone.setText(response.body()!!.data.phone)

                                } else{
                                    //progress!!.dismiss()
                                    // mContext.showToastMsg(response.body()!!.message,1)
                                    Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG).show()
                                    //Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                                }
                            }

                        })
                    }
                }



                }



        }



        imgViewCamera.setOnClickListener {
            val intent = Intent(context,ImageSelectActivity::class.java)
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true)
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true)
            startActivityForResult(intent, 1213)
        }

    }


    private fun createBuilder(filePath: String, editProfile: EditProfileReq
    ): RequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("tokenId",editProfile.tokenId)
        builder.addFormDataPart("first_name",editProfile.first_name)
        builder.addFormDataPart("last_name",editProfile.last_name)
        builder.addFormDataPart("phone",editProfile.phone)
        builder.addFormDataPart("address",editProfile.address)
        // Images
        if(!TextUtils.isEmpty(filePath)){
            val file = File(filePath)
            if (file.exists()) {

                builder.addFormDataPart("photo",file.name, file.asRequestBody("image/jpg".toMediaTypeOrNull()) )


            }
        }
        return builder.build()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            filePath = data!!.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH)
            val selectedImage = BitmapFactory.decodeFile(filePath)
            imgViewUserPic.setImageBitmap(selectedImage)
        }
    }



}
