package com.vision2020.ui.result

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.vision2020.R
import com.vision2020.data.response.ExpCalcResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_calculated_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CalculatedResultFragment : Fragment() {


    private lateinit var linlay_parent: LinearLayout

    var expDatelist = ArrayList<String>()
    var selDate=""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        return inflater.inflate(R.layout.fragment_calculated_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedweek = getAppPref().getString("selectedweek")

        var day0 = view.findViewById<TextView>(R.id.sWeek)
        day0.setText(selectedweek)






        var experimentId = getAppPref().getString("experimentId")
        var expdates = getAppPref().getString("expDates")
       // var expdatenew = getAppPref().getString("expDates")
        linlay_parent=view.findViewById(R.id.layoutForTeacher1)


        //expDatelist.add(0, "Select Date")



        if (expdates != null) {
            expdates = expdates.replace("[", "")
            expdates = expdates.replace("]", "")
            val expdatelist = expdates.split(",").toTypedArray()

            for (i in 0 until expdatelist.size) {

                expDatelist.add(i, expdatelist[i].toString().trim())
                Log.d("Response::::", expdatelist[i].toString())

            }


            selDate = expDatelist[1].toString().trim()

            val datelist: Spinner =
                view.findViewById<View>(R.id.datelist) as Spinner
            //Log.d("Response::::", expdatelist[i].toString())

            if (expDatelist != null) {

                val expDatelist = expDatelist.distinct().toList()



                Log.d("Response1::::", expDatelist.toString())

                val adapter = activity?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item,
                        expDatelist
                    )
                }


                datelist.setAdapter(adapter)

              //  datelist.setSelection(7);

                datelist?.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        //if (position > 0) {



                                linlay_parent.visibility = View.VISIBLE

                                selDate = expDatelist.get(position)

                                val token = getAppPref().getString(AppConstant.KEY_TOKEN)

                                if (selDate != null) {
                                    RetrofitClient.instance!!.fetchExpResultbyDate(
                                        token!!, selDate.toString(), experimentId.toString()

                                    ).enqueue(object : Callback<ExpCalcResponse> {
                                        override fun onFailure(
                                            call: Call<ExpCalcResponse>,
                                            t: Throwable
                                        ) {
                                            // returnValue(UpdateProfileResponse(t.message!!))
                                            // Toast.makeText(activity, t.message!!, Toast.LENGTH_LONG).show()
                                            activity?.showToastMsg(t.message!!, 2)
                                        }

                                        override fun onResponse(
                                            call: Call<ExpCalcResponse>,
                                            response: Response<ExpCalcResponse>
                                        ) {


                                            if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {


                                                textView2.text = response.body()!!.data.expLength
                                                textView4.text = response.body()!!.data.cycles
                                                textView6.text = response.body()!!.data.totalHits
                                                textView8.text =
                                                    response.body()!!.data.totalVapeJuice
                                                textView10.text =
                                                    response.body()!!.data.drugConcentration
                                                textView12.text =
                                                    response.body()!!.data.drugDispensed
                                                textView14.text = response.body()!!.data.drugLungs
                                                textView16.text =
                                                    response.body()!!.data.dispersedRestBody
                                                textView18.text =
                                                    response.body()!!.data.drugdispersedRestBody

                                                Log.d(
                                                    "Response::::",
                                                    response.body()!!.data.toString()
                                                )


                                            }


                                        }

                                    })
                                }



                      //  }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                })

            }


        }



        btnWeekResult.setOnClickListener {
            getAppPref().setString("selDate",selDate.toString())
           // getAppPref().setString("expDates",expdates.toString())
            getAppPref().setString("experimentId",experimentId.toString())

            findNavController().navigate(R.id.action_calculatedResultFragment_to_expResultWeeklyFragment)
        }





    }




}