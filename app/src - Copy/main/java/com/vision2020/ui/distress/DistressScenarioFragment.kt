package com.vision2020.ui.distress

import `in`.srain.cube.views.GridViewWithHeaderAndFooter
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.vision2020.R
import com.vision2020.adapter.DistressScenarioGridListAdapter
import com.vision2020.data.response.CalLoginDistressResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.showToastMsg
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAdjusters.firstInMonth
import java.util.*
import kotlin.collections.ArrayList


class DistressScenarioFragment : Fragment() {

    val itemList: MutableList<String> = ArrayList()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        return inflater.inflate(R.layout.fragment_distress_scenario, container, false)
    }


    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val year = Calendar.getInstance().get(Calendar.YEAR);
        val now = LocalDate.of(year, Month.JANUARY, 1)
        // Find the first Sunday of the year
        // Find the first Sunday of the year
        var sunday = now.with(firstInMonth(DayOfWeek.SUNDAY))
        var i = 1
        var inc = 1
        do {
            // Loop to get every Sunday by adding Period.ofDays(7) the the current Sunday.
            //println(sunday.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)))
            itemList.add("Week of "+sunday.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))+" \n"+"(Week "+i+")")
            sunday = sunday.plus(Period.ofDays(7))
            i += inc
        } while (sunday.year == year)


        // val gridview = view.findViewById<GridView>(R.id.gridview)
        val gridView =
            view.findViewById(R.id.gridview) as GridViewWithHeaderAndFooter

        val layoutInflater = LayoutInflater.from(activity?.applicationContext)
        val headerView: View = layoutInflater.inflate(R.layout.distress_grid_header, null)

        gridView.addHeaderView(headerView)





        val adapter = activity?.applicationContext?.let { DistressScenarioGridListAdapter(it, R.layout.item_list_distressscenario, itemList) }
        gridView.adapter = adapter


        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            //  DialogWithData().show(ft, DialogWithData.TAG)
            //(activity as DistressScenarioFragment?)?.showEditTextAlert()

            val builder =
                AlertDialog.Builder(view.context)


            val dialog: AlertDialog = builder.create()
            val inflater = layoutInflater
            val dialogView1: View = inflater.inflate(R.layout.fragment_dialog_with_data, null)

            val editText1=
                dialogView1.findViewById(R.id.etPassword) as EditText

            editText1.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

            val button2 =
                dialogView1.findViewById(R.id.btnSubmit) as Button


            button2.setOnClickListener { // DO SOMETHINGS

                //val email = getAppPref().getString(AppConstant.KEY_EMAIL)
                val token = getAppPref().getString(AppConstant.KEY_TOKEN)

                RetrofitClient.instance!!.userDistressLogin(
                    token!!, editText1.text.toString()

                ).enqueue(object : Callback<CalLoginDistressResponse> {
                    override fun onFailure(call: Call<CalLoginDistressResponse>, t: Throwable) {
                        // returnValue(UpdateProfileResponse(t.message!!))
                        // Toast.makeText(activity, t.message!!, Toast.LENGTH_LONG).show()
                        activity?.showToastMsg(t.message!!, 2)
                    }

                    override fun onResponse(
                        call: Call<CalLoginDistressResponse>,
                        response: Response<CalLoginDistressResponse>
                    ) {
                        if (response.body()!!.status_code.toInt() == AppConstant.CODE_SUCCESS) {

                            ///Toast.makeText(activity, "Login Successfully", Toast.LENGTH_LONG).show()

                            activity?.showToastMsg("Login Successfully", 1)

                            // findNavController().navigate(R.id.distressExperimentSetupFragment)
                            // findNavController().navigate(R.id.action_navigation_distress_to_distressScenarioStep3)


                            if(response.body()!!.data.user_type=="2") {
                                findNavController().navigate(R.id.action_navigation_distress_to_distressExperimentSetupFragment)
                            } else {
                                findNavController().navigate(R.id.action_navigation_distress_to_distressScenarioStep3)

                            }




                            dialog.dismiss()
                        } else {
                            //progress!!.dismiss()
                            // mContext.showToastMsg(response.body()!!.message,1)

                            activity?.showToastMsg(response.body()!!.message, 2)
                            //Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        }
                    }

                })

            }

            dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //  dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

            //editText1.requestFocus()



            dialog.setView(dialogView1)
            dialog.show()


        }


    }





}
