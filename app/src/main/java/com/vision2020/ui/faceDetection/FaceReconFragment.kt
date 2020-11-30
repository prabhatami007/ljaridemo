package com.vision2020.ui.faceDetection
import BaseFragment
import `in`.mayanknagwanshi.imagepicker.ImageSelectActivity
import `in`.mayanknagwanshi.imagepicker.imagePicker.ImagePicker
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vision2020.R
import com.vision2020.utils.AppConstant
import com.vision2020.utils.AppConstant.KEY_AGE
import com.vision2020.utils.AppConstant.KEY_NAME
import com.vision2020.utils.AppConstant.KEY_PATH
import com.vision2020.utils.navigateForCamera
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_face.*
import kotlinx.android.synthetic.main.fragment_face.imgViewCamera
class FaceReconFragment : BaseFragment<FaceViewModel>() {
    private var filePath:String =""
    override val layoutId: Int
        get() = R.layout.fragment_face
    override val viewModel: FaceViewModel
        get() = ViewModelProvider(this).get(FaceViewModel::class.java)

    override fun onCreateStuff() {

    }

    override fun initListeners() {

        tvEffectOnFace.setOnClickListener {
            when {
                editTextName.text!!.trim().isEmpty() -> {
                    mContext.showToastMsg("Please Enter Name First!",AppConstant.ERROR)
                }
                editTextAge.text!!.trim().isEmpty() -> {
                    mContext.showToastMsg("Please Enter Age First!",AppConstant.ERROR)
                }
                filePath.isEmpty() -> {
                    mContext.showToastMsg("Please Set Photo First!",AppConstant.ERROR)
                }
                else -> {
                    val bundle = bundleOf(KEY_NAME to editTextName.text.toString(),KEY_AGE to editTextAge.text.toString(), KEY_PATH to filePath)
                    findNavController().navigate(R.id.action_navigation_face_to_effectsOnFaceFragment,bundle)
                }
            }

        }
        tvEffectsOnBrain.setOnClickListener {

        }

        tvEffectsOnLungs.setOnClickListener {

        }

        imgViewCamera.setOnClickListener {
          /*  imgPicker = ImagePicker()
            imgPicker.withFragment(this)
                .withCompression(false)
                .start()*/

            val intent = Intent(context,ImageSelectActivity::class.java)
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true)
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true)
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS,false)
            startActivityForResult(intent, 1213)
            //mContext.navigateForCamera(requireActivity())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
           filePath = data!!.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH)
            val selectedImage = BitmapFactory.decodeFile(filePath)
            imgViewUserPic.setImageBitmap(selectedImage)

           /*val filePath = imgPicker.getImageFilePath(data!!)
            if(filePath!=null){
                val selectedImage = BitmapFactory.decodeFile(filePath)
                imgViewUserPic.setImageBitmap(selectedImage)
            }*/

        }
    }


}
