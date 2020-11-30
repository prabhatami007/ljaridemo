package com.vision2020.ui.faceDetection

import BaseFragment
import android.Manifest
import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.SparseArray
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.google.android.gms.vision.face.Landmark
import com.norulab.exofullscreen.MediaPlayer
import com.norulab.exofullscreen.preparePlayer
import com.norulab.exofullscreen.setSource
import com.vision2020.R
import com.vision2020.adapter.DrugVideoAdapter
import com.vision2020.data.response.DrugList
import com.vision2020.ui.views.dialog.DrugListDialog
import com.vision2020.ui.views.dialog.ItemCallBack
import com.vision2020.utils.*
import com.vision2020.utils.AppConstant.KEY_AGE
import com.vision2020.utils.AppConstant.KEY_NAME
import com.vision2020.utils.AppConstant.KEY_PATH
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import kotlinx.android.synthetic.main.fragment_effects_on_face.*
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
class EffectsOnFaceFragment : BaseFragment<FaceViewModel>(),DrugVideoAdapter.ItemClick,
    PermissionRequest.Listener {
    companion object {
        private const val LEFT_EYE = 4
        private const val RADIUS = 10f
        private const val TEXT_SIZE = 50f
        private const val CORNER_RADIUS = 2f
        private const val STROKE_WIDTH = 5f
    }

    private var drugList = arrayListOf<DrugList.Data>()
    private var drugName:String= ""
    private var file: String?=null
    private var sparseArray: SparseArray<Face>?=null
    private var isPermission:Boolean = false
    private val request by lazy { permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build()}
    private lateinit var defaultBitmap: Bitmap
    private lateinit var temporaryBitmap: Bitmap

    private lateinit var eyePatchBitmap: Bitmap
    private lateinit var leftEyeBitMap : Bitmap
    private lateinit var rightEyeBitMap : Bitmap

    private lateinit var leftCheekBitmap:Bitmap
    private lateinit var rightCheekBitmap:Bitmap

    private lateinit var lipsBitmap:Bitmap
    private lateinit var headBitmap:Bitmap

    private lateinit var circlePatchBitmap: Bitmap


    private lateinit var canvas: Canvas
    private val rectPaint = Paint()
   private val faceDetector: FaceDetector
        get() = initializeDetector()

    private fun initializeDetector(): FaceDetector {
        return FaceDetector.Builder(mContext)
            .setTrackingEnabled(false)
            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
            .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
            .build()
    }

    override val layoutId: Int
        get() = R.layout.fragment_effects_on_face
    override val viewModel: FaceViewModel
        get() = ViewModelProvider(this).get(FaceViewModel::class.java)
    override fun onCreateStuff() {
        progress = mContext.progressDialog(getString(R.string.request))
        request.addListener(this)
        request.send()
        inItView()
        inItVision2020EffectsView()

        if(mContext.isAppConnected()){
            progress!!.show()
           mViewModel!!.drugList().observe(this, androidx.lifecycle.Observer {
              progress!!.dismiss()
               if(it!=null){
                   if(it.status_code == AppConstant.CODE_SUCCESS && it.data!=null){
                     drugList = it.data!!
                      editTextDrugName.setText(drugList[0].dgName)
                       if(isPermission){
                           drugName = drugList[0].dgName
                           getVideoList()
                       }
                   }else{
                       mContext.responseHandler(it.status_code,it.message)
                   }
               }else{
                   mContext.showToastMsg(getString(R.string.server_error),2)
               }
           })
        }
    }

    private fun inItVision2020EffectsView(){
        val bitmapOptions = BitmapFactory.Options().apply {
            inMutable = true
        }
        initializeBitmap(bitmapOptions)
        createRectanglePaint()
        canvas = Canvas(temporaryBitmap).apply {
            drawBitmap(defaultBitmap, 0f, 0f, null)
        }

        if (!faceDetector.isOperational) {
            AlertDialog.Builder(mContext)
                .setMessage("Face Detector could not be set up on your device :(")
                .show()
        } else {
            val frame = Frame.Builder().setBitmap(defaultBitmap).build()
             sparseArray = faceDetector.detect(frame)
            imgFacePic.setImageDrawable(BitmapDrawable(resources, temporaryBitmap))
            faceDetector.release()
            // add effects on face
            //detectFaces(sparseArray)

        }
    }

    private fun detectFaces(sparseArray: SparseArray<Face>?) {
        for (i in 0 until sparseArray!!.size()) {
            val face = sparseArray.valueAt(i)

            val left = face.position.x
            val top = face.position.y
            val right = left + face.width
            val bottom = top + face.height
             // show react angle view on face
            val rectF = RectF(left, top, right, bottom)
            //canvas.drawRoundRect(rectF, CORNER_RADIUS, CORNER_RADIUS, rectPaint)
            detectLandmarks(face)
        }
    }

    private fun detectLandmarks(face: Face?) {
        for (landmark in face!!.landmarks) {
            val xCoordinate = landmark.position.x
             val yCoordinate = landmark.position.y
             val landmarkType = landmark.type
            // canvas.drawCircle(xCoordinate, yCoordinate, RADIUS, rectPaint)
            drawLandmarkType()
            applyVisionEffects(landmarkType, xCoordinate, yCoordinate)
        }
    }

    private fun drawLandmarkType() {
        fun drawLandmarkType(landmarkType: Int, xCoordinate: Float, yCoordinate: Float) {
            val type = landmarkType.toString()
            rectPaint.textSize = TEXT_SIZE
            // canvas.drawText(type, xCoordinate, yCoordinate, rectPaint)  // draw react angle on face
        }
    }
        private fun applyVisionEffects(landmarkType: Int, xCoordinate: Float, yCoordinate: Float) {
            when (landmarkType) {
                LEFT_EYE -> {
                    // TODO: Optimize so that this calculation is not done for every face
                    /* val scaledWidth = eyePatchBitmap.getScaledWidth(canvas)
                    val scaledHeight = eyePatchBitmap.getScaledHeight(canvas)
                    canvas.drawBitmap(eyePatchBitmap,
                                      xCoordinate - scaledWidth / 2,
                                      yCoordinate - scaledHeight / 2,
                                      null)*/
                }

                Landmark.RIGHT_CHEEK -> {
                   val scaledWidth = rightCheekBitmap.getScaledWidth(canvas)
                    val scaledHeight = rightCheekBitmap.getScaledHeight(canvas)

                    canvas.drawBitmap(rightCheekBitmap,
                        xCoordinate - scaledWidth / 2,
                        yCoordinate - scaledHeight / 2,
                        null
                    )
                    imgFacePic.invalidate()
                }

                Landmark.LEFT_CHEEK -> {
                    val scaledWidth = leftCheekBitmap.getScaledWidth(canvas)
                    val scaledHeight = leftCheekBitmap.getScaledHeight(canvas)
                    canvas.drawBitmap(
                        leftCheekBitmap,
                        xCoordinate - scaledWidth/2,
                        yCoordinate - scaledHeight/2,
                        null
                    )
                    leftCheekBitmap.recycle()
                }
                Landmark.BOTTOM_MOUTH ->{
                   /* val scaledWidth = lipsBitmap.getScaledWidth(canvas)
                    val scaledHeight = lipsBitmap.getScaledHeight(canvas)
                    canvas.drawBitmap(lipsBitmap, xCoordinate - scaledWidth / 2, yCoordinate - scaledHeight / 2, null
                    )*/

                }
            }
        }

    private fun createRectanglePaint() {
        rectPaint.apply {
            strokeWidth = STROKE_WIDTH
            color = Color.CYAN
            style = Paint.Style.STROKE
        }
    }

    private fun initializeBitmap(bitmapOptions: BitmapFactory.Options) {
        defaultBitmap = BitmapFactory.decodeFile(file,bitmapOptions)
        temporaryBitmap = Bitmap.createBitmap(defaultBitmap.width, defaultBitmap.height, Bitmap.Config.ARGB_8888)

    }

    @SuppressLint("SetTextI18n")
    private fun inItView() {
        txtName.text = """Name: ${arguments?.getString(KEY_NAME)}"""
        txtAge.text = """Age: ${arguments?.getString(KEY_AGE)}"""
        if(arguments?.getString(KEY_PATH)!!.isNotEmpty()){
            file = arguments?.getString(KEY_PATH)
           // imgFacePic.setImageBitmap(BitmapFactory.decodeFile(arguments?.getString(KEY_PATH)))
        }
        MediaPlayer.initialize(mContext)
        MediaPlayer.exoPlayer?.preparePlayer(player, false)
        // MediaPlayer.exoPlayer?.setSource(mContext, "")
        // MediaPlayer.exoPlayer?.playWhenReady = false
        player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        //MediaPlayer.exoPlayer?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        // MediaPlayer.startPlayer()

    }

    private fun setData() {
        if(drugList.isNotEmpty()){
            DrugListDialog(mContext,R.style.pullBottomFromTop,R.layout.dialog_options,
                drugList,"Select Drug",object: ItemCallBack.Callback{
                    override fun selected(pos: Int) {
                        editTextDrugName.setText(drugList[pos].dgName)
                        drugName = drugList[pos].dgName
                        if(isPermission){
                            getVideoList()
                        }
                       // spGroupName.setText(drugList[pos].dgName)
                       // groupId = groupList[pos].id.toString()
                        //progress!!.show()
                        //mViewModel!!.groupMembersReq(groupId)
                    }

                }).show()
        }
    }

    private fun getVideoList() {
        val videoItemHashSet = HashSet<String>()
        val projection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.SIZE)
        val selection = MediaStore.Video.Media.DATA + " like?"
        val selectionArgs = arrayOf("%Drug/$drugName%")
        val  videoCursor = mContext.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC")
         try {
            videoCursor!!.moveToFirst()
            do {
                videoItemHashSet.add(videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)))
            } while (videoCursor.moveToNext())
            videoCursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
             videoCursor?.close()
        }
        rvVideo.layoutManager = LinearLayoutManager(mContext)
        val downloadedList: ArrayList<String> = ArrayList(videoItemHashSet)
        val videoAdapter = DrugVideoAdapter(mContext,downloadedList,this)
        rvVideo.adapter = videoAdapter
        val list = arrayListOf<String>()
        /*for (value in downloadedList) {
            if (value==drugName) {
                list.add(value)
                Log.e("======","====="+list.size)
                Log.e("======","====="+value)
               // val videoAdapter = DrugVideoAdapter(mContext,list,this)
               // rvVideo.adapter = videoAdapter
            }
        }*/
    }

    override fun onPause() {
        super.onPause()
        MediaPlayer.pausePlayer()
       // simplePlayer!!.playWhenReady = false
    }

    override fun onDestroy() {
        MediaPlayer.stopPlayer()
       // simplePlayer!!.release()
        super.onDestroy()
    }

    override fun initListeners() {
      editTextDrugName.setOnClickListener {
          setData()
      }
    }

    private fun getStream(path:String): InputStream? {
        return mContext.assets.open(path)
    }

    override fun getPosition(path:String) {
        MediaPlayer.exoPlayer?.setSource(mContext, Uri.parse(path).toString())
        MediaPlayer.startPlayer()
    }

    override fun onPermissionsResult(result: List<PermissionStatus>) {
        when {
            result.anyPermanentlyDenied() -> mContext.showPermanentlyDeniedDialog(result)
            result.anyShouldShowRationale() -> mContext.showRationaleDialog(result, request)
            result.allGranted() -> isPermission = true
        }
    }
}
