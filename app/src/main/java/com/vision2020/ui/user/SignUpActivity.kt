package com.vision2020.ui.user
import BaseActivity
import `in`.mayanknagwanshi.imagepicker.ImageSelectActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vision2020.R
import com.vision2020.data.request.SignUpReq
import com.vision2020.utils.*
import com.vision2020.utils.AppConstant.CAMERA_CODE
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.VALUE_STUDENT
import com.vision2020.utils.AppConstant.VALUE_TEACHER
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_sign_up.*
class SignUpActivity : BaseActivity<UserViewModel>() {
    var filePath: String? = ""
    var isStudent:Boolean = true
    var userType:String=""
    override val layoutId: Int
        get() = R.layout.activity_sign_up
    override val viewModel: UserViewModel
        get() = ViewModelProvider(this).get(UserViewModel::class.java)
    override val context: Context
        get() = this@SignUpActivity
    override fun onCreate() {
        progress = context.progressDialog(getString(R.string.login))
        filePath = " "
        if(rbStudent.isChecked){
            userType = VALUE_STUDENT
            isStudent = true
           // layoutForTeacher.visibility =View.GONE
           // layoutStudent.visibility = View.VISIBLE
        }
        context.spanText(tvLogin,getString(R.string.already_have_account_login_here),21,26){
            if(it){
                navigateToLogin(this@SignUpActivity,LoginActivity::class.java)
                finish()
            }
        }
        mViewModel!!.getUser().observe(this, Observer {
            progress!!.dismiss()
            if(it!=null){
                if(it.status_code== CODE_SUCCESS && it.data!=null){
                   showToastMsg("Register Successfully. Please check your email for account verification.",1)
                    navigateToLogin(this@SignUpActivity,LoginActivity::class.java)
                    finish()
                }else{
                    responseHandler(it.status_code,it.message)
                }
            }else{
                context.showToastMsg(getString(R.string.server_error),2)
            }
        })

    }
    override fun initListeners() {
        imgViewCamera.setOnClickListener {
            val intent = Intent(context,ImageSelectActivity::class.java)
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true)
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true)
            startActivityForResult(intent, 1213)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbStudent ->{
                    userType = VALUE_STUDENT
                    isStudent = true
                  //  layoutStudent.visibility = View.VISIBLE
                   // layoutForTeacher.visibility = View.GONE
                    imgViewProfile.visibility = View.VISIBLE
                    imgViewCamera.visibility = View.VISIBLE
                }
                R.id.rbTeacher->{
                    userType = VALUE_TEACHER
                    isStudent = false
                   // layoutStudent.visibility = View.GONE
                   // layoutForTeacher.visibility = View.VISIBLE
                    imgViewProfile.visibility = View.INVISIBLE
                    imgViewCamera.visibility = View.INVISIBLE
                }
            }
        }

        btnRegister.setOnClickListener {
            when {
                editTextFirstName.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_first_name),2)
                }
                editTextLastName.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_last_name),2)
                }
                editTextStudentId.text!!.trim().isEmpty() ->{
                    showToastMsg(getString(R.string.error_student_id),2)
                }
                editTextClassId.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_class_id),2)
                }editTextSchoolId.text!!.trim().isEmpty() ->{
                showToastMsg(getString(R.string.error_school_id),2)
            }
                editTextPhone.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_phone),2)
                }
                editTextEmailAddress.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_empty_email),2)
                }
                !editTextEmailAddress.validEmail() -> {
                    showToastMsg(getString(R.string.error_valid_email),2)
                }
                editTextPassword.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_empty_password),2)
                }editTextPassword.text!!.length <8 ->{
                showToastMsg(getString(R.string.error_less_password),2)
            }
                editTextRetypePassword.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_retype_pass),2)
                }editTextRetypePassword.text.toString()!=editTextPassword.text.toString() ->{
                showToastMsg(getString(R.string.error_pass_not_match),2)
            }
                editTextAddress.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_address),2)
                }else ->{
                if(isAppConnected()){
                    progress!!.show()
                    val signUp = SignUpReq(userType,editTextFirstName.text.toString(),editTextLastName.text.toString(),editTextSchoolId.text.toString(),editTextStudentId.text.toString(),
                        editTextClassId.text.toString(),editTextPhone.text.toString(),editTextEmailAddress.text.toString(),editTextPassword.text.toString(),editTextAddress.text.toString())
                    mViewModel!!.signUpUser(filePath!!,signUp,isStudent) } }
            }


           /* if(isStudent){
                *//*when {
                    editTextStuFirstName.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_first_name),2)
                    }
                    editTextStuLastName.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_last_name),2)
                    }
                    editTextAge.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_age),2)
                    }
                    editTextStudentId.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_stu_id),2)
                    }
                    editTextStuClassId.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_class_id),2)
                    }
                    editTextStuPhone.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_phone),2)
                    }
                    editTextStuEmailAddress.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_empty_email),2)
                    }
                    !editTextStuEmailAddress.validEmail() -> {
                        showToastMsg(getString(R.string.error_valid_email),2)
                    }
                    editTextStuPassword.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_empty_password),2)
                    } editTextStuPassword.text!!.length <8 ->{
                    showToastMsg(getString(R.string.error_less_password),2)
                    }
                    editTextRetypeStuPassword.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_retype_pass),2)
                    }
                    editTextRetypeStuPassword.text.toString()!=editTextStuPassword.text.toString() -> {
                        showToastMsg(getString(R.string.error_pass_not_match),2)
                    }
                    editTextStuAddress.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_address),2)
                    }else ->{ if(isAppConnected()){
                         progress!!.show()
                         val signUp = SignUpReq(
                             userType,editTextStuFirstName.text.toString(),editTextStuLastName.text.toString(),editTextAge.text.toString(),editTextStudentId.text.toString(),
                             editTextClassId.text.toString(),editTextStuPhone.text.toString(),editTextStuEmailAddress.text.toString(),editTextStuPassword.text.toString(),editTextStuAddress.text.toString())
                         mViewModel!!.signUpUser(filePath!!, signUp, isStudent) } }
                }*//*

            }
              else{
                when {
                    // Teacher Registrtion
                    editTextFirstName.text!!.trim().isEmpty() -> {
                      showToastMsg(getString(R.string.error_first_name),2)
                    }
                    editTextLastName.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_last_name),2)
                    }
                    editTextStudentId.text!!.trim().isEmpty() ->{
                        showToastMsg(getString(R.string.error_student_id),2)
                    }
                    editTextClassId.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_class_id),2)
                    }editTextSchoolId.text!!.trim().isEmpty() ->{
                    showToastMsg(getString(R.string.error_school_id),2)
                    }
                    editTextPhone.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_phone),2)
                    }
                    editTextEmailAddress.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_empty_email),2)
                    }
                    !editTextEmailAddress.validEmail() -> {
                        showToastMsg(getString(R.string.error_valid_email),2)
                    }
                    editTextPassword.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_empty_password),2)
                    }editTextPassword.text!!.length <8 ->{
                    showToastMsg(getString(R.string.error_less_password),2)
                    }
                    editTextRetypePassword.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_retype_pass),2)
                    }editTextRetypePassword.text.toString()!=editTextPassword.text.toString() ->{
                    showToastMsg(getString(R.string.error_pass_not_match),2)
                     }
                    editTextAddress.text!!.trim().isEmpty() -> {
                        showToastMsg(getString(R.string.error_address),2)
                    }else ->{
                    if(isAppConnected()){
                        progress!!.show()
                        val signUp = SignUpReq(userType,editTextFirstName.text.toString(),editTextLastName.text.toString(),"","",
                            editTextClassId.text.toString(),editTextPhone.text.toString(),editTextEmailAddress.text.toString(),editTextPassword.text.toString(),editTextAddress.text.toString())
                            mViewModel!!.signUpUser(filePath!!,signUp,isStudent) } }
                    }
                }*/
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            filePath = data!!.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH)
            val selectedImage = BitmapFactory.decodeFile(filePath)
            imgViewProfile.setImageBitmap(selectedImage)
        }
    }

}
