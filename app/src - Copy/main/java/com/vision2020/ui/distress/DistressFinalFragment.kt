package com.vision2020.ui.distress

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vision2020.R
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import kotlinx.android.synthetic.main.fragment_distress_scenario_step3.*
import pl.droidsonroids.gif.AnimationListener
import pl.droidsonroids.gif.GifDrawable


class DistressFinalFragment : Fragment(), AnimationListener {

    private lateinit var gifDrawable: GifDrawable

    var grouplisting: ArrayList<GroupDataList> = ArrayList()

    val list = arrayListOf<Int>()

    var totalsubject = 0
    var totalsubjectchk = -1





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {



        return inflater.inflate(R.layout.fragment_distress_final, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

          var grpName: TextView

        grpName = view.findViewById(R.id.grpName);

        gifDrawable = gifImageView.drawable as GifDrawable

        //val subjectpassed = requireArguments().getString("subjectpassed")


        val subjectpassed = getAppPref().getString("subjectpassed")
        val groupName = getAppPref().getString("groupName")

        if (subjectpassed != null) {
            totalsubject = subjectpassed.toInt()
        }
        totalsubjectchk = totalsubject-1

        Log.d("Response::::", subjectpassed.toString())

        grpName.setText(groupName)

        resetAnimation()
        toggleAnimation()





        gifDrawable.addAnimationListener(this)



    }


    private fun resetAnimation() {
        gifDrawable.stop()
        gifDrawable.loopCount = totalsubject
        gifDrawable.seekToFrameAndGet(5)
        //lstGroup.setEnabled(true);
       // lstGroup.setClickable(true);
        //  buttonToggle.isChecked = false

        // lstGroup.setAdapter(adapter);
    }

    private fun toggleAnimation() = when {
        gifDrawable.isPlaying -> gifDrawable.stop()
        else -> gifDrawable.start()
    }

    override fun onDestroyView() {
        gifDrawable.removeAnimationListener(this)
        super.onDestroyView()
    }

    override fun onAnimationCompleted(loopNumber: Int) {
        val view = view
        if (view != null) {

           //  Log.d("Response::::", totalsubjectchk.toString()+"----"+loopNumber+"---"+totalsubject)
           //   Snackbar.make(view, getString(R.string.animation_loop_completed, loopNumber), Snackbar.LENGTH_SHORT).show()
            if(loopNumber==totalsubjectchk){

                resetAnimation()
                val dialogBuilder =
                    activity?.let { AlertDialog.Builder(it).create() }
                val inflater = layoutInflater
                val dialogView: View = inflater.inflate(R.layout.distressfinalpopup, null)

                val btnOk =
                    dialogView.findViewById<View>(R.id.btnOk) as Button



                btnOk.setOnClickListener {
                    if (dialogBuilder != null) {
                        dialogBuilder.dismiss()
                        findNavController().navigate(R.id.action_distressFinalFragment_to_navigation_distress)
                    }
                }




                if (dialogBuilder != null) {
                    dialogBuilder.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialogBuilder.setView(dialogView)
                    dialogBuilder.show()
                    dialogBuilder.setCanceledOnTouchOutside(true)
                };

            }
        }
    }
}