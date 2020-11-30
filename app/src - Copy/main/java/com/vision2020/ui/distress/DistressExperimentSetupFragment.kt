package com.vision2020.ui.distress


import BaseFragment
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vision2020.R
import com.vision2020.data.response.DistressExpResponse
import com.vision2020.data.response.GroupListing
import com.vision2020.network.RetrofitClient
import com.vision2020.ui.settings.group.GroupViewModel
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_distress_scenario_step3.*

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class DistressExperimentSetupFragment : BaseFragment<GroupViewModel>() {
    private var groupViewModel: GroupViewModel?=null
    private var groupList = arrayListOf<GroupListing.Data>()

    override val layoutId: Int



        get() = R.layout.fragment_distress_experiment_setup
    override val viewModel: GroupViewModel
        get() = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)

    val groupRequest = ArrayList<String>()
    val narRequest = ArrayList<String>()
    val subjectRequest = ArrayList<String>()


    private var group_id = arrayListOf<String>()

    var jo = JSONObject()
    var ja = JSONArray()


    var obj = JSONObject()

    var array = JSONArray()

    var mainObject = JSONObject() // Host object

    var requestObject = JSONObject()

    var jarray = JSONArray()

    var Objson = JSONArray()



    var grouplisting: ArrayList<GroupDataList> = ArrayList()

    var cal = Calendar.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater!!.inflate(R.layout.fragment_distress_experiment_setup,container,false)




        val expName = view.findViewById<EditText>(R.id.expName)
        val expDate = view.findViewById<EditText>(R.id.expDate)
        val expTime = view.findViewById<EditText>(R.id.expTime)
        val expLength = view.findViewById<EditText>(R.id.expLength)
        val vapeTime = view.findViewById<EditText>(R.id.vapeTime)
        val expCycle = view.findViewById<EditText>(R.id.expCycle)
        val waitTime = view.findViewById<EditText>(R.id.waitTime)
        val drugType = view.findViewById<EditText>(R.id.drugType)
        val drugCalc = view.findViewById<EditText>(R.id.drugCalc)
        val watts = view.findViewById<EditText>(R.id.watts)
        val temprature = view.findViewById<EditText>(R.id.temprature)
        val drugConcetration = view.findViewById<EditText>(R.id.drugConcetration)
        val lungs = view.findViewById<EditText>(R.id.lungs)
        val expEndTime = view.findViewById<EditText>(R.id.expEndTime)


        expLength.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        vapeTime.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        expCycle.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        expLength.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        waitTime.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        watts.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        temprature.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        drugConcetration.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        lungs.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        expEndTime.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )


        expDate.addTextChangedListener(object : TextWatcher {

            private var current = ""
            private val ddmmyyyy = "MMDDYYYY"
            private val cal = Calendar.getInstance()

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != current) {
                    var clean = p0.toString().replace("[^\\d.]|\\.".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]|\\.", "")

                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--

                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        var mon = Integer.parseInt(clean.substring(0, 2))
                        var day = Integer.parseInt(clean.substring(2, 4))
                        var year = Integer.parseInt(clean.substring(4, 8))

                        mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                        cal.set(Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal.set(Calendar.YEAR, year)
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                        clean = String.format("%02d%02d%02d", mon, day, year)
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8))

                    sel = if (sel < 0) 0 else sel
                    current = clean
                    expDate.setText(current)
                    expDate.setSelection(if (sel < current.count()) sel else current.count())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {

            }
        })





        val token = getAppPref().getString(AppConstant.KEY_TOKEN)



        val jsonArrayNew = JSONArray(listOf(""))




        RetrofitClient.instance!!.getListOfGroup(token!!,"").enqueue(object : Callback<GroupListing> {

            override fun onFailure(call: Call<GroupListing>, t: Throwable) {
               // progress!!.dismiss()
                // t.message?.let { mContext.showToastMsg(it,1) }
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<GroupListing>,
                response: Response<GroupListing>) {



                //  languages.add("Test")

                if (response.body()!!.status_code.toInt()== AppConstant.CODE_SUCCESS){

                    val jsonArray = JSONArray(response.body()!!.data)

                    var gData=response.body()!!.data;

                    var totaldata=jsonArray.length()



                    grouplisting.add(GroupDataList("", "Select Group"))

                    //   val jsonArray = jsonObject.optJSONArray("data")



                    val llMain: LinearLayout = view.findViewById(R.id.parentLinear)

                    val btnSubmit = Button(activity)
                    btnSubmit.setText("Submit")
                    // btnSubmit.gravity = Gravity.RIGHT
                    //btnSubmit.setGravity(Gravity.RIGHT)
                    activity?.let { ContextCompat.getColor(it, R.color.colorWhite) }?.let {
                        btnSubmit.setTextColor(
                            it
                        )
                    };
                    //btnSubmit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    btnSubmit.setBackground(getResources().getDrawable(R.drawable.rounded_button_global));
                    btnSubmit.typeface = activity?.let { ResourcesCompat.getFont(it, R.font.lato_regular) }
                    btnSubmit.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                        weight = 1.0f
                        gravity = Gravity.RIGHT
                        topMargin = 10
                    }





                    for (i in 0 until jsonArray.length()) {
                        val gname= gData?.get(i)?.group_name
                        val gid= gData?.get(i)?.id

                        val list1 = JSONObject()

                        if (gname != null) {
                            if (gid != null) {
                                grouplisting.add(GroupDataList(gid.toString(), gname))
                                // grouplisting.add(i,gname)
                            }
                        }

                        val view: View =
                            LayoutInflater.from(activity).inflate(R.layout.distressexplinearlayout, null)
                        if(i%2==0) {
                            view.setBackgroundResource(R.color.colorGroupEven)
                        } else {
                            view.setBackgroundResource(R.color.colorGroupOdd)
                        }


                        val grouplist: Spinner =
                            view.findViewById<View>(R.id.lstGroup) as Spinner


                        val adapter: ArrayAdapter<GroupDataList>? = activity?.let {
                            ArrayAdapter<GroupDataList>(
                                it,
                                android.R.layout.simple_spinner_dropdown_item,
                                grouplisting
                            )
                        }
                        grouplist.setAdapter(adapter)
                        // grouplist.setSelection(adapter.getPosition(myItem))


                        grouplist.setOnItemSelectedListener(object :
                            AdapterView.OnItemSelectedListener {
                            @RequiresApi(Build.VERSION_CODES.N)
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                val grp: GroupDataList = parent.selectedItem as GroupDataList

                                if(position!=0) {


                                    if (groupRequest.contains(grp.id.toString())) {
                                        grouplist.setSelection(0);

                                        activity?.showToastMsg("Already exist",2)
                                    } else {
                                        groupRequest.add(grp.id.toString())

                                        list1.put("group_id", grp.id)

                                        grouplist.setEnabled(false);
                                        grouplist.setClickable(false);



                                    }



                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        })





                        val editNarscan: EditText =
                            view.findViewById<View>(R.id.editNarscan1) as EditText
                        val editSubject: EditText = view.findViewById<View>(R.id.editSubjectPassed) as EditText

                        editNarscan.setInputType(
                            InputType.TYPE_CLASS_NUMBER or
                                    InputType.TYPE_NUMBER_FLAG_SIGNED
                        )
                        editSubject.setInputType(
                            InputType.TYPE_CLASS_NUMBER or
                                    InputType.TYPE_NUMBER_FLAG_SIGNED
                        )


                        editNarscan.setText("")
                        editSubject.setText("")
                        llMain.addView(view)



                        editNarscan.addTextChangedListener(object : TextWatcher {

                            override fun afterTextChanged(s: Editable) {


                            }

                            override fun beforeTextChanged(s: CharSequence, start: Int,
                                                           count: Int, after: Int) {
                            }

                            override fun onTextChanged(s: CharSequence, start: Int,
                                                       before: Int, count: Int) {


                                list1.put("narcan", s.toString())
                                narRequest.add(s.toString())



                            }
                        })


                        editSubject.addTextChangedListener(object : TextWatcher {

                            override fun afterTextChanged(s: Editable) {
                                // activity?.showToastMsg(s.toString(),1)

                            }

                            override fun beforeTextChanged(s: CharSequence, start: Int,
                                                           count: Int, after: Int) {
                            }

                            override fun onTextChanged(s: CharSequence, start: Int,
                                                       before: Int, count: Int) {
                                list1.put("subjectpassed", s.toString())
                                subjectRequest.add(s.toString())


                            }
                        })


                        Objson.put(list1);


                    }


                    llMain.addView(btnSubmit)



                    btnSubmit.setOnClickListener {

                        // findNavController().navigate(R.id.action_distressExperimentSetupFragment_to_distressExperimentSummary)


                        when {


                            expDate.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_expDate), 2)
                            }
                            expName.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_expName), 2)
                            }
                            expTime.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_expTime), 2)
                            }
                            expLength.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_expLength), 2)
                            }
                            vapeTime.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_vapeTime), 2)
                            }
                            expCycle.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_expCycle), 2)
                            }
                            waitTime.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_waitTime), 2)
                            }
                            drugType.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_drugType), 2)
                            }
                            drugCalc.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_drugCalc), 2)
                            }
                            watts.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_watts), 2)
                            }
                            temprature.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_temprature), 2)
                            }
                            drugConcetration.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_drugConcetration), 2)
                            }
                            lungs.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_lungs), 2)
                            }
                            expEndTime.text!!.trim().isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_expEndTime), 2)
                            }
                            groupRequest.isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_groupList), 2)
                            }
                            narRequest.isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_narcan), 2)
                            }
                            subjectRequest.isEmpty() -> {
                                activity?.showToastMsg(getString(R.string.error_subjectpassed), 2)
                            }
                            else -> {


                                val jObj = JsonObject()


                                val jObj1 = JsonObject()

                                jObj1.addProperty("authToken", token!!);
                               // jObj1.addProperty("userid", "1");
                                jObj1.addProperty("expName", expName.getText().toString());
                                jObj1.addProperty("expDate", expDate.getText().toString());
                                jObj1.addProperty("expTime", expTime.getText().toString());
                                jObj1.addProperty("expLength", expLength.getText().toString());
                                jObj1.addProperty("vapeTime", vapeTime.getText().toString());
                                jObj1.addProperty("expCycle", expCycle.getText().toString());
                                jObj1.addProperty("waitTime", waitTime.getText().toString());
                                jObj1.addProperty("drugType", drugType.getText().toString());
                                jObj1.addProperty("drugCalc", drugCalc.getText().toString());
                                jObj1.addProperty("watts", watts.getText().toString());
                                jObj1.addProperty("temprature", temprature.getText().toString());
                                jObj1.addProperty(
                                    "drugConcetration",
                                    drugConcetration.getText().toString()
                                );
                                jObj1.addProperty("lungs", lungs.getText().toString());
                                jObj1.addProperty("expEndTime", expEndTime.getText().toString());



                                jObj1.addProperty("groups", Objson.toString());

                                val json: String = Gson().toJson(jObj1)


                                Log.d("Response::::", json.toString())


                                val jsonParser = JsonParser()
                                val jsonObject = jsonParser.parse(json).asJsonObject


                                RetrofitClient.instance!!.postRawJSON(jsonObject)?.enqueue(
                                    object : Callback<DistressExpResponse> {
                                        override fun onFailure(
                                            call: Call<DistressExpResponse>,
                                            t: Throwable
                                        ) {
                                            t.message?.let { it1 -> activity?.showToastMsg(it1, 2) }

                                        }

                                        override fun onResponse(
                                            call: Call<DistressExpResponse>,
                                            response: Response<DistressExpResponse>
                                        ) {
                                            if (response.body()!!.status_code.toInt()==AppConstant.CODE_SUCCESS){
                                                findNavController().navigate(R.id.action_distressExperimentSetupFragment_to_distressScenarioStep3)
                                                activity?.showToastMsg("Successfully experiment data saved", 1)
                                                // Log.i("app:retro:service", "onResponse true ${response.body()!!.toString()}")
                                            } else {
                                                activity?.showToastMsg(response.body()!!.message, 2)

                                            }
                                        }
                                    }
                                )


                            }

                        }

                    }
                } else {
                    //progress!!.dismiss()
                    // mContext.showToastMsg(response.body()!!.message,1)
                    activity?.showToastMsg(response.body()!!.message, 2)
                    //Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                }
            }



        })


        return view
    }




    override fun onCreateStuff() {



    }




    override fun initListeners() {



    }

}



