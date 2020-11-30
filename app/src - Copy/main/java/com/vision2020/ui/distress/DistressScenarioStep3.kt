package com.vision2020.ui.distress

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vision2020.R
import com.vision2020.data.response.DistressGroupDataListing
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import kotlinx.android.synthetic.main.fragment_distress_scenario_step3.*
import org.json.JSONArray
import pl.droidsonroids.gif.AnimationListener
import pl.droidsonroids.gif.GifDrawable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DistressScenarioStep3 : Fragment(), AnimationListener {

    private lateinit var gifDrawable: GifDrawable

    var grouplisting: ArrayList<GroupDataList> = ArrayList()

    val list = arrayListOf<Int>()
    val listsubject = arrayListOf<Int>()
    val listGrp = arrayListOf<String>()

    var totalnarcan = 0
    var totalnarcanchk = -1

    var totalSubject = 0
    var totalSubjectchk = -1

    var groupName=""



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_distress_scenario_step3, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  var grouplist: Spinner

       // grouplist = view.findViewById(R.id.lstGroup);

        gifDrawable = gifImageView.drawable as GifDrawable


        gifDrawable.addAnimationListener(this)

        gifDrawable.stop()

        val token = getAppPref().getString(AppConstant.KEY_TOKEN)

        val userType = getAppPref().getString(AppConstant.KEY_USER_TYPE)

        RetrofitClient.instance!!.getdistressGroupList(token!!).enqueue(object :
            Callback<DistressGroupDataListing> {

            override fun onFailure(call: Call<DistressGroupDataListing>, t: Throwable) {
                ///  progress!!.dismiss()
                // t.message?.let { mContext.showToastMsg(it,1) }

                // Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<DistressGroupDataListing>,
                response: Response<DistressGroupDataListing>
            ) {

                if (response.body()!!.status_code.toInt()== AppConstant.CODE_SUCCESS) {

                    Log.d("Response::::", response.body()!!.data.toString())


                    val jsonArray = JSONArray(response.body()!!.data)
                    if(userType=="2") {
                        grouplisting.add(GroupDataList("", "Select Group"))
                    }
                    //   val jsonArray = jsonObject.optJSONArray("data")
                    var gData=response.body()!!.data;

                    var totaldata=jsonArray.length()

                    for (i in 0 until jsonArray.length()) {
                        val gname = gData?.get(i)?.group_name
                        val gid = gData?.get(i)?.id
                        val narscan = gData?.get(i)?.narcan
                        val subjectpassed = gData?.get(i)?.subjectpassed

                        listGrp.add(gname.toString())

                        if (narscan != null) {
                            list.add(narscan.toInt())
                        }



                        if (subjectpassed != null) {
                            listsubject.add(subjectpassed.toInt())
                        }

                        if (gname != null) {
                            if (gid != null) {
                                grouplisting.add(GroupDataList(gid.toString(), gname))
                                // grouplisting.add(i,gname)
                                //Log.d("Response::::", gname.toString())
                            }
                        }

                    }


                    val adapter: ArrayAdapter<GroupDataList>? = activity?.let {
                        ArrayAdapter<GroupDataList>(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            grouplisting
                        )
                    }
                    lstGroup.setAdapter(adapter)

                    // grouplisting.add(GroupDataList("", "Select Group"))

                    //  Log.d("Response::::", response.body()!!.status_code.toString())




                } else {
                    //progress!!.dismiss()
                    // mContext.showToastMsg(response.body()!!.message,1)
                    Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG).show()
                    //Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                }

            }



        })


        lstGroup.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {


                if(userType=="2") {

                    if (position != 0) {

                        totalnarcan = list.get(position - 1) * 10
                        totalnarcanchk = (list.get(position - 1) * 10) - 1

                        totalSubject = listsubject.get(position - 1) * 10
                        totalSubjectchk = totalSubject - totalnarcan



                        groupName = listGrp.get(position - 1)

                        resetAnimation()
                        toggleAnimation()

                        Log.d("Response::::", groupName.toString())

                        lstGroup.setEnabled(false);
                        lstGroup.setClickable(false);


                    }
                } else {
                    totalnarcan = list.get(position) * 10
                    totalnarcanchk = (list.get(position) * 10) - 1

                    totalSubject = listsubject.get(position) * 10
                    totalSubjectchk = totalSubject - totalnarcan

                    groupName = listGrp.get(position)

                    resetAnimation()
                    toggleAnimation()

                    Log.d("Response::::", groupName.toString())

                    lstGroup.setEnabled(false);
                    lstGroup.setClickable(false);

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


      //  buttonToggle.setOnClickListener { toggleAnimation() }


    }


    private fun resetAnimation() {
        gifDrawable.stop()
        gifDrawable.loopCount = totalnarcan
        gifDrawable.seekToFrameAndGet(5)
        lstGroup.setEnabled(true);
        lstGroup.setClickable(true);
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

           // Log.d("Response::::", totalnarcanchk.toString()+"----"+loopNumber+"---"+totalnarcan)
            //Snackbar.make(view, getString(R.string.animation_loop_completed, loopNumber), Snackbar.LENGTH_SHORT).show()
            if(loopNumber==totalnarcanchk){
               // Log.d("Response::::", groupName.toString())

                resetAnimation()
                val dialogBuilder =
                    activity?.let { AlertDialog.Builder(it).create() }
                val inflater = layoutInflater
                val dialogView: View = inflater.inflate(R.layout.distresssummarypopuplinearlayout, null)

                val btnYes =
                    dialogView.findViewById<View>(R.id.btnYes) as Button
                val btnNo =
                    dialogView.findViewById<View>(R.id.btnNo) as Button

               // btnYes.setBackground(getResources().getDrawable(R.drawable.rounded_button_global));

                btnYes.setOnClickListener {
                    if (dialogBuilder != null) {
                        dialogBuilder.dismiss()
                        findNavController().navigate(R.id.action_distressScenarioStep3_to_navigation_distress)
                    }
                }

                btnNo.setOnClickListener {
                    if (dialogBuilder != null) {
                        dialogBuilder.dismiss()

                        getAppPref().setString("subjectpassed",totalSubjectchk.toString())
                        getAppPref().setString("groupName",groupName)



                        /*      val intent = Intent(
                            requireActivity().baseContext,
                            DistressFinalFragment::class.java
                        )



                        intent.putExtra("subjectpassed", totalSubject)
                        intent.putExtra("gropname", groupName)
                        requireActivity().startActivity(intent)*/

                        findNavController().navigate(R.id.action_distressScenarioStep3_to_distressFinalFragment)
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