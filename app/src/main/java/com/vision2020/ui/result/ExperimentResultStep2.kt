package com.vision2020.ui.result

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import com.vision2020.R
import com.vision2020.data.response.CalLoginDistressResponse
import com.vision2020.data.response.DistressExpResponse
import com.vision2020.data.response.ExpResultEditResponse
import com.vision2020.data.response.ExpResultResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_experiment_result_step2.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ExperimentResultStep2 : Fragment() {



    var rejultObj1 = JsonObject()


    var experimentId=""



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        return inflater.inflate(R.layout.fragment_experiment_result_step2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        calculateResult.setEnabled(false);

        val selectedweek = getAppPref().getString("selectedweek")
        val selDate = getAppPref().getString("selDate")


        val dt = selDate // Start date

       /* val hday1= dt?.let { addDay(it, 1) }
        val hday2= dt?.let { addDay(it, 2) }
        val hday3= dt?.let { addDay(it, 3) }
        val hday4= dt?.let { addDay(it, 4) }
        val hday5= dt?.let { addDay(it, 5) }
        val hday6= dt?.let { addDay(it, 6) }
        val hday7= dt?.let { addDay(it, 7) }*/

        val hday1= 1
        val hday2= 2
        val hday3= 3
        val hday4= 4
        val hday5= 5
        val hday6= 6
        val hday7= 7

        val hdate0= dt?.let { addDate(it, 0) }

        val hdate1= dt?.let { addDate(it, 0) }
        val hdate2= dt?.let { addDate(it, 1) }
        val hdate3= dt?.let { addDate(it, 2) }
        val hdate4= dt?.let { addDate(it, 3) }
        val hdate5= dt?.let { addDate(it, 4) }
        val hdate6= dt?.let { addDate(it, 5) }
        val hdate7= dt?.let { addDate(it, 6) }

        var day0 = view.findViewById<TextView>(R.id.textView21)
        day0.setText(selectedweek)



  /*              var day0 = view.findViewById<TextView>(R.id.textView21)
                var day1 = view.findViewById<TextView>(R.id.textView22)
                var day2 = view.findViewById<TextView>(R.id.textView23)
                var day3 = view.findViewById<TextView>(R.id.textView24)
                var day4 = view.findViewById<TextView>(R.id.textView25)
                var day5 = view.findViewById<TextView>(R.id.textView26)
                var day6 = view.findViewById<TextView>(R.id.textView27)
                var day7 = view.findViewById<TextView>(R.id.textView28)


        day1.setText("Day "+hday1)
        day2.setText("Day "+hday2)
        day3.setText("Day "+hday3)
        day4.setText("Day "+hday4)
        day5.setText("Day "+hday5)
        day6.setText("Day "+hday6)
        day7.setText("Day "+hday7)*/




                val expDate1 = view.findViewById<EditText>(R.id.expDate1)
                val expDate2 = view.findViewById<EditText>(R.id.expDate2)
                val expDate3 = view.findViewById<EditText>(R.id.expDate3)
                val expDate4 = view.findViewById<EditText>(R.id.expDate4)
                val expDate5 = view.findViewById<EditText>(R.id.expDate5)
                val expDate6 = view.findViewById<EditText>(R.id.expDate6)
                val expDate7 = view.findViewById<EditText>(R.id.expDate7)



        expDate1.setText(hdate1)
        expDate2.setText(hdate2)
        expDate3.setText(hdate3)
        expDate4.setText(hdate4)
        expDate5.setText(hdate5)
        expDate6.setText(hdate6)
        expDate7.setText(hdate7)


                val expTime1 = view.findViewById<EditText>(R.id.expTime1)
                val expTime2 = view.findViewById<EditText>(R.id.expTime2)
                val expTime3 = view.findViewById<EditText>(R.id.expTime3)
                val expTime4 = view.findViewById<EditText>(R.id.expTime4)
                val expTime5 = view.findViewById<EditText>(R.id.expTime5)
                val expTime6 = view.findViewById<EditText>(R.id.expTime6)


        val expLength1 = view.findViewById<EditText>(R.id.expLength1)
        val expLength2 = view.findViewById<EditText>(R.id.expLength2)
        val expLength3 = view.findViewById<EditText>(R.id.expLength3)
        val expLength4 = view.findViewById<EditText>(R.id.expLength4)
        val expLength5 = view.findViewById<EditText>(R.id.expLength5)
        val expLength6 = view.findViewById<EditText>(R.id.expLength6)
        val expLength7 = view.findViewById<EditText>(R.id.expLength7)


        val waitTime1 = view.findViewById<EditText>(R.id.waitTime1)
        val waitTime2 = view.findViewById<EditText>(R.id.waitTime2)
        val waitTime3 = view.findViewById<EditText>(R.id.waitTime3)
        val waitTime4 = view.findViewById<EditText>(R.id.waitTime4)
        val waitTime5 = view.findViewById<EditText>(R.id.waitTime5)
        val waitTime6 = view.findViewById<EditText>(R.id.waitTime6)
        val waitTime7 = view.findViewById<EditText>(R.id.waitTime7)




        val clickListener = View.OnClickListener { v ->

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)

        val mTimePicker = MyTimePickerDialog(
            activity,
            MyTimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds -> // TODO Auto-generated method stub
                when (v.getId()) {
                    R.id.expTime1 -> expTime1.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expTime2 -> expTime2.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expTime3 -> expTime3.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expTime4 -> expTime4.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expTime5 -> expTime5.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expTime6 -> expTime6.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )

                    R.id.expTime7 -> expTime7.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expLength1 -> expLength1.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expLength2 -> expLength2.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expLength3 -> expLength3.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expLength4 -> expLength4.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expLength5 -> expLength5.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expLength6 -> expLength6.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )

                    R.id.expLength7 -> expLength7.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expEndTime1 -> expEndTime1.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expEndTime2 -> expEndTime2.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expEndTime3 -> expEndTime3.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expEndTime4 -> expEndTime4.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expEndTime5 -> expEndTime5.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.expEndTime6 -> expEndTime6.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )

                    R.id.expEndTime7 -> expEndTime7.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.waitTime1 -> waitTime1.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.waitTime2 -> waitTime2.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.waitTime3 -> waitTime3.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.waitTime4 -> waitTime4.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.waitTime5 -> waitTime5.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )
                    R.id.waitTime6 -> waitTime6.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )

                    R.id.waitTime7 -> waitTime7.setText(
                        String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", seconds)
                    )



                }
            },
            currentHour,
            currentMinute,
            currentSecond,
            true
        )
        mTimePicker.show()

        }
      /*  var timePickerDialog: TimePickerDialog? = null

        val clickListener = View.OnClickListener { v ->

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)
            var amPm: String



            timePickerDialog =
                TimePickerDialog(
                    activity,
                    OnTimeSetListener { timePicker, hourOfDay, minutes ->
                        if (hourOfDay >= 12) {
                            amPm = "pm"
                        } else {
                            amPm = "am"
                        }
                        when (v.getId()) {
                            R.id.expTime1 -> expTime1.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expTime2 -> expTime2.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expTime3 -> expTime3.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expTime4 -> expTime4.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expTime5 -> expTime5.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expTime6 -> expTime6.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )

                            R.id.expTime7 -> expTime7.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expEndTime1 -> expEndTime1.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expEndTime2 -> expEndTime2.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expEndTime3 -> expEndTime3.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expEndTime4 -> expEndTime4.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expEndTime5 -> expEndTime5.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )
                            R.id.expEndTime6 -> expEndTime6.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )

                            R.id.expEndTime7 -> expEndTime7.setText(
                                String.format(
                                    "%02d:%02d ",
                                    hourOfDay,
                                    minutes
                                ) + amPm
                            )

                        }





                    }, currentHour, currentMinute, false
                )
            timePickerDialog!!.show()
        }
*/

  expTime1.setKeyListener(null)
    expTime2.setKeyListener(null)
    expTime3.setKeyListener(null)
    expTime4.setKeyListener(null)
    expTime5.setKeyListener(null)
    expTime6.setKeyListener(null)
    expTime7.setKeyListener(null)

   expLength1.setKeyListener(null)
    expLength2.setKeyListener(null)
    expLength3.setKeyListener(null)
    expLength4.setKeyListener(null)
    expLength5.setKeyListener(null)
    expLength6.setKeyListener(null)
    expLength7.setKeyListener(null)

    expEndTime1.setKeyListener(null)
    expEndTime2.setKeyListener(null)
    expEndTime3.setKeyListener(null)
    expEndTime4.setKeyListener(null)
    expEndTime5.setKeyListener(null)
    expEndTime6.setKeyListener(null)
    expEndTime7.setKeyListener(null)

    waitTime1.setKeyListener(null)
    waitTime2.setKeyListener(null)
    waitTime3.setKeyListener(null)
    waitTime4.setKeyListener(null)
    waitTime5.setKeyListener(null)
    waitTime6.setKeyListener(null)
    waitTime7.setKeyListener(null)


    expTime1.setOnClickListener(clickListener)
    expTime2.setOnClickListener(clickListener)
    expTime3.setOnClickListener(clickListener)
    expTime4.setOnClickListener(clickListener)
    expTime5.setOnClickListener(clickListener)
    expTime6.setOnClickListener(clickListener)
    expTime7.setOnClickListener(clickListener)

    expLength1.setOnClickListener(clickListener)
    expLength2.setOnClickListener(clickListener)
    expLength3.setOnClickListener(clickListener)
    expLength4.setOnClickListener(clickListener)
    expLength5.setOnClickListener(clickListener)
    expLength6.setOnClickListener(clickListener)
    expLength7.setOnClickListener(clickListener)


    expEndTime1.setOnClickListener(clickListener)
    expEndTime2.setOnClickListener(clickListener)
    expEndTime3.setOnClickListener(clickListener)
    expEndTime4.setOnClickListener(clickListener)
    expEndTime5.setOnClickListener(clickListener)
    expEndTime6.setOnClickListener(clickListener)
    expEndTime7.setOnClickListener(clickListener)

    waitTime1.setOnClickListener(clickListener)
    waitTime2.setOnClickListener(clickListener)
    waitTime3.setOnClickListener(clickListener)
    waitTime4.setOnClickListener(clickListener)
    waitTime5.setOnClickListener(clickListener)
    waitTime6.setOnClickListener(clickListener)
    waitTime7.setOnClickListener(clickListener)



            val vapeTime1 = view.findViewById<EditText>(R.id.vapeTime1)
            val vapeTime2 = view.findViewById<EditText>(R.id.vapeTime2)
            val vapeTime3 = view.findViewById<EditText>(R.id.vapeTime3)
            val vapeTime4 = view.findViewById<EditText>(R.id.vapeTime4)
            val vapeTime5 = view.findViewById<EditText>(R.id.vapeTime5)
            val vapeTime6 = view.findViewById<EditText>(R.id.vapeTime6)
            val vapeTime7 = view.findViewById<EditText>(R.id.vapeTime7)

            val totalVapes1 = view.findViewById<EditText>(R.id.totalVapes1)
            val totalVapes2 = view.findViewById<EditText>(R.id.totalVapes2)
            val totalVapes3 = view.findViewById<EditText>(R.id.totalVapes3)
            val totalVapes4 = view.findViewById<EditText>(R.id.totalVapes4)
            val totalVapes5 = view.findViewById<EditText>(R.id.totalVapes5)
            val totalVapes6 = view.findViewById<EditText>(R.id.totalVapes6)
            val totalVapes7 = view.findViewById<EditText>(R.id.totalVapes7)

            val expCycle1 = view.findViewById<EditText>(R.id.expCycle1)
            val expCycle2 = view.findViewById<EditText>(R.id.expCycle2)
            val expCycle3 = view.findViewById<EditText>(R.id.expCycle3)
            val expCycle4 = view.findViewById<EditText>(R.id.expCycle4)
            val expCycle5 = view.findViewById<EditText>(R.id.expCycle5)
            val expCycle6 = view.findViewById<EditText>(R.id.expCycle6)
            val expCycle7 = view.findViewById<EditText>(R.id.expCycle7)

        expCycle1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)



            val drugType1 = view.findViewById<EditText>(R.id.drugType1)
            val drugType2 = view.findViewById<EditText>(R.id.drugType2)
            val drugType3 = view.findViewById<EditText>(R.id.drugType3)
            val drugType4 = view.findViewById<EditText>(R.id.drugType4)
            val drugType5 = view.findViewById<EditText>(R.id.drugType5)
            val drugType6 = view.findViewById<EditText>(R.id.drugType6)
            val drugType7 = view.findViewById<EditText>(R.id.drugType7)




            val drugCalc1 = view.findViewById<EditText>(R.id.drugCalc1)
            val drugCalc2 = view.findViewById<EditText>(R.id.drugCalc2)
            val drugCalc3 = view.findViewById<EditText>(R.id.drugCalc3)
            val drugCalc4 = view.findViewById<EditText>(R.id.drugCalc4)
            val drugCalc5 = view.findViewById<EditText>(R.id.drugCalc5)
            val drugCalc6 = view.findViewById<EditText>(R.id.drugCalc6)
            val drugCalc7 = view.findViewById<EditText>(R.id.drugCalc7)


       /* drugCalc1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugCalc2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugCalc3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugCalc4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugCalc5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugCalc6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugCalc7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)*/

            val watts1 = view.findViewById<EditText>(R.id.watts1)
            val watts2 = view.findViewById<EditText>(R.id.watts2)
            val watts3 = view.findViewById<EditText>(R.id.watts3)
            val watts4 = view.findViewById<EditText>(R.id.watts4)
            val watts5 = view.findViewById<EditText>(R.id.watts5)
            val watts6 = view.findViewById<EditText>(R.id.watts6)
            val watts7 = view.findViewById<EditText>(R.id.watts7)

        watts1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)

            val temprature1 = view.findViewById<EditText>(R.id.temprature1)
            val temprature2 = view.findViewById<EditText>(R.id.temprature2)
            val temprature3 = view.findViewById<EditText>(R.id.temprature3)
            val temprature4 = view.findViewById<EditText>(R.id.temprature4)
            val temprature5 = view.findViewById<EditText>(R.id.temprature5)
            val temprature6 = view.findViewById<EditText>(R.id.temprature6)
            val temprature7 = view.findViewById<EditText>(R.id.temprature7)


            temprature1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)


            val drugConcetration1 = view.findViewById<EditText>(R.id.drugConcetration1)
            val drugConcetration2 = view.findViewById<EditText>(R.id.drugConcetration2)
            val drugConcetration3 = view.findViewById<EditText>(R.id.drugConcetration3)
            val drugConcetration4 = view.findViewById<EditText>(R.id.drugConcetration4)
            val drugConcetration5 = view.findViewById<EditText>(R.id.drugConcetration5)
            val drugConcetration6 = view.findViewById<EditText>(R.id.drugConcetration6)
            val drugConcetration7 = view.findViewById<EditText>(R.id.drugConcetration7)


        drugConcetration1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugConcetration2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugConcetration3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugConcetration4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugConcetration5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugConcetration6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        drugConcetration7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)


            val lungs1 = view.findViewById<EditText>(R.id.lungs1)
            val lungs2 = view.findViewById<EditText>(R.id.lungs2)
            val lungs3 = view.findViewById<EditText>(R.id.lungs3)
            val lungs4 = view.findViewById<EditText>(R.id.lungs4)
            val lungs5 = view.findViewById<EditText>(R.id.lungs5)
            val lungs6 = view.findViewById<EditText>(R.id.lungs6)
            val lungs7 = view.findViewById<EditText>(R.id.lungs7)

        lungs1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        lungs2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        lungs3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        lungs4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        lungs5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        lungs6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        lungs7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)


            val expEndTime1 = view.findViewById<EditText>(R.id.expEndTime1)
            val expEndTime2 = view.findViewById<EditText>(R.id.expEndTime2)
            val expEndTime3 = view.findViewById<EditText>(R.id.expEndTime3)
            val expEndTime4 = view.findViewById<EditText>(R.id.expEndTime4)
            val expEndTime5 = view.findViewById<EditText>(R.id.expEndTime5)
            val expEndTime6 = view.findViewById<EditText>(R.id.expEndTime6)
            val expEndTime7 = view.findViewById<EditText>(R.id.expEndTime7)



    val token = getAppPref().getString(AppConstant.KEY_TOKEN)

    if (hdate0 != null) {
        RetrofitClient.instance!!.fetchExpResult(
            token!!,hdate1.toString()

        ).enqueue(object : Callback<ExpResultResponse> {
            override fun onFailure(call: Call<ExpResultResponse>, t: Throwable) {
                // returnValue(UpdateProfileResponse(t.message!!))
                // Toast.makeText(activity, t.message!!, Toast.LENGTH_LONG).show()
                activity?.showToastMsg(t.message!!, 2)
            }

            override fun onResponse(
                call: Call<ExpResultResponse>,
                response: Response<ExpResultResponse>
            ) {




                 val jsonArray = JSONArray(response.body()!!.data)

                var exData=response.body()!!.data;

                var totaldata=jsonArray.length()

                if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {

                    Log.d("Response::::", response.body()!!.message.toString())
                    if(response.body()!!.message.equals("1")) {
                        calculateResult.setEnabled(true);

                        for (i in 0 until jsonArray.length()) {
                            val id = exData?.get(i)?.id
                            val userId = exData?.get(i)?.userId
                            val expId = exData?.get(i)?.expId
                            val labelId = exData?.get(i)?.labelId
                            val startDate = exData?.get(i)?.startDate
                            val day_1 = exData?.get(i)?.day_1
                            val day_2 = exData?.get(i)?.day_2
                            val day_3 = exData?.get(i)?.day_3
                            val day_4 = exData?.get(i)?.day_4
                            val day_5 = exData?.get(i)?.day_5
                            val day_6 = exData?.get(i)?.day_6
                            val day_7 = exData?.get(i)?.day_7

                            if (expId != null) {
                                experimentId = expId
                            }

                            if (labelId == "1") {

                            }
                            if (labelId == "2") { /// Exp Time
                                expTime1.setText(day_1)
                                expTime2.setText(day_2)
                                expTime3.setText(day_3)
                                expTime4.setText(day_4)
                                expTime5.setText(day_5)
                                expTime6.setText(day_6)
                                expTime7.setText(day_7)
                            }

                            if (labelId == "3") { /// Exp Length
                                expLength1.setText(day_1)
                                expLength2.setText(day_2)
                                expLength3.setText(day_3)
                                expLength4.setText(day_4)
                                expLength5.setText(day_5)
                                expLength6.setText(day_6)
                                expLength7.setText(day_7)
                            }
                            if (labelId == "4") { /// Vape Time
                                vapeTime1.setText(day_1)
                                vapeTime2.setText(day_2)
                                vapeTime3.setText(day_3)
                                vapeTime4.setText(day_4)
                                vapeTime5.setText(day_5)
                                vapeTime6.setText(day_6)
                                vapeTime7.setText(day_7)
                            }

                            if (labelId == "5") { /// Total Vapes
                                totalVapes1.setText(day_1)
                                totalVapes2.setText(day_2)
                                totalVapes3.setText(day_3)
                                totalVapes4.setText(day_4)
                                totalVapes5.setText(day_5)
                                totalVapes6.setText(day_6)
                                totalVapes7.setText(day_7)
                            }

                            if (labelId == "6") { /// Exp Cycles
                                expCycle1.setText(day_1)
                                expCycle2.setText(day_2)
                                expCycle3.setText(day_3)
                                expCycle4.setText(day_4)
                                expCycle5.setText(day_5)
                                expCycle6.setText(day_6)
                                expCycle7.setText(day_7)
                            }

                            if (labelId == "7") { /// Wait Time
                                waitTime1.setText(day_1)
                                waitTime2.setText(day_2)
                                waitTime3.setText(day_3)
                                waitTime4.setText(day_4)
                                waitTime5.setText(day_5)
                                waitTime6.setText(day_6)
                                waitTime7.setText(day_7)
                            }

                            if (labelId == "8") { /// Drug Type
                                drugType1.setText(day_1)
                                drugType2.setText(day_2)
                                drugType3.setText(day_3)
                                drugType4.setText(day_4)
                                drugType5.setText(day_5)
                                drugType6.setText(day_6)
                                drugType7.setText(day_7)
                            }

                            if (labelId == "9") { /// Drug Calc
                                drugCalc1.setText(day_1)
                                drugCalc2.setText(day_2)
                                drugCalc3.setText(day_3)
                                drugCalc4.setText(day_4)
                                drugCalc5.setText(day_5)
                                drugCalc6.setText(day_6)
                                drugCalc7.setText(day_7)
                            }

                            if (labelId == "10") { /// Watts
                                watts1.setText(day_1)
                                watts2.setText(day_2)
                                watts3.setText(day_3)
                                watts4.setText(day_4)
                                watts5.setText(day_5)
                                watts6.setText(day_6)
                                watts7.setText(day_7)
                            }

                            if (labelId == "11") { /// Temperature
                                temprature1.setText(day_1)
                                temprature2.setText(day_2)
                                temprature3.setText(day_3)
                                temprature4.setText(day_4)
                                temprature5.setText(day_5)
                                temprature6.setText(day_6)
                                temprature7.setText(day_7)
                            }

                            if (labelId == "12") { /// Drug Concentration
                                drugConcetration1.setText(day_1)
                                drugConcetration2.setText(day_2)
                                drugConcetration3.setText(day_3)
                                drugConcetration4.setText(day_4)
                                drugConcetration5.setText(day_5)
                                drugConcetration6.setText(day_6)
                                drugConcetration7.setText(day_7)
                            }

                            if (labelId == "13") { /// Lungs
                                lungs1.setText(day_1)
                                lungs2.setText(day_2)
                                lungs3.setText(day_3)
                                lungs4.setText(day_4)
                                lungs5.setText(day_5)
                                lungs6.setText(day_6)
                                lungs7.setText(day_7)
                            }

                            if (labelId == "14") { /// Exp End Time
                                expEndTime1.setText(day_1)
                                expEndTime2.setText(day_2)
                                expEndTime3.setText(day_3)
                                expEndTime4.setText(day_4)
                                expEndTime5.setText(day_5)
                                expEndTime6.setText(day_6)
                                expEndTime7.setText(day_7)
                            }


                        }

                    }
                    Log.d("Response::::", response.body()!!.data.toString())


                }


            }

        })
    }





    btnSubmit.setOnClickListener {


        when {
            expDate1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Date of Day "+hday1, 2)
            }
            expDate2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Date of Day "+hday2, 2)
            }
            expDate3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Date of Day "+hday3, 2)
            }
            expDate4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Date of Day "+hday4, 2)
            }
            expDate5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Date of Day "+hday5, 2)
            }
            expDate6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Date of Day "+hday6, 2)
            }
            expDate7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Date of Day "+hday7, 2)
            }

            expTime1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Start Time of Day "+hday1, 2)
            }
            expTime2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Start Time of Day "+hday2, 2)
            }
            expTime3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Start Time of Day "+hday3, 2)
            }
            expTime4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Start Time of Day "+hday4, 2)
            }
            expTime5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Start Time of Day "+hday5, 2)
            }
            expTime6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Start Time of Day "+hday6, 2)
            }
            expTime7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Start Time of Day "+hday7, 2)
            }


            expLength1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Length of Day "+hday1, 2)
            }
            expLength2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Length of Day "+hday2, 2)
            }
            expLength3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Length of Day "+hday3, 2)
            }
            expLength4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Length of Day "+hday4, 2)
            }
            expLength5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Length of Day "+hday5, 2)
            }
            expLength6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Length of Day "+hday6, 2)
            }
            expLength7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp Length of Day "+hday7, 2)
            }

            vapeTime1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Vape Time of Day "+hday1, 2)
            }
            vapeTime2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Vape Time of Day "+hday2, 2)
            }
            vapeTime3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Vape Time of Day "+hday3, 2)
            }
            vapeTime4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Vape Time of Day "+hday4, 2)
            }
            vapeTime5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Vape Time of Day "+hday5, 2)
            }
            vapeTime6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Vape Time of Day "+hday6, 2)
            }
            vapeTime7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Vape Time of Day "+hday7, 2)
            }

            totalVapes1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Total Vapes of Day "+hday1, 2)
            }
            totalVapes2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Total Vapes of Day "+hday2, 2)
            }
            totalVapes3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Total Vapes of Day "+hday3, 2)
            }
            totalVapes4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Total Vapes of Day "+hday4, 2)
            }
            totalVapes5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Total Vapes of Day "+hday5, 2)
            }
            totalVapes6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Total Vapes of Day "+hday6, 2)
            }
            totalVapes7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Total Vapes of Day "+hday7, 2)
            }

            expCycle1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # Hits/Cycle of Day "+hday1, 2)
            }
            expCycle2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # Hits/Cycle of Day "+hday2, 2)
            }
            expCycle3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # Hits/Cycle of Day "+hday3, 2)
            }
            expCycle4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # Hits/Cycle of Day "+hday4, 2)
            }
            expCycle5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # Hits/Cycle of Day "+hday5, 2)
            }
            expCycle6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # Hits/Cycle of Day "+hday6, 2)
            }
            expCycle7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # Hits/Cycle of Day "+hday7, 2)
            }

            waitTime1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Wait Time of Day "+hday1, 2)
            }
            waitTime2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Wait Time of Day "+hday2, 2)
            }
            waitTime3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Wait Time of Day "+hday3, 2)
            }
            waitTime4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Wait Time of Day "+hday4, 2)
            }
            waitTime5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Wait Time of Day "+hday5, 2)
            }
            waitTime6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Wait Time of Day "+hday6, 2)
            }
            waitTime7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Wait Time of Day "+hday7, 2)
            }

            drugType1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Type of Day "+hday1, 2)
            }
            drugType2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Type of Day "+hday2, 2)
            }
            drugType3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Type of Day "+hday3, 2)
            }
            drugType4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Type of Day "+hday4, 2)
            }
            drugType5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Type of Day "+hday5, 2)
            }
            drugType6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Type of Day "+hday6, 2)
            }
            drugType7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Type of Day "+hday7, 2)
            }

            drugCalc1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Calc of Day "+hday1, 2)
            }
            drugCalc2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Calc of Day "+hday2, 2)
            }
            drugCalc3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Calc of Day "+hday3, 2)
            }
            drugCalc4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Calc of Day "+hday4, 2)
            }
            drugCalc5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Calc of Day "+hday5, 2)
            }
            drugCalc6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Calc of Day "+hday6, 2)
            }
            drugCalc7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Calc of Day "+hday7, 2)
            }

            watts1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Watts of Day "+hday1, 2)
            }
            watts2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Watts of Day "+hday2, 2)
            }
            watts3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Watts of Day "+hday3, 2)
            }
            watts4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Watts of Day "+hday4, 2)
            }
            watts5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Watts of Day "+hday5, 2)
            }
            watts6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Watts of Day "+hday6, 2)
            }
            watts7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Watts of Day "+hday7, 2)
            }

            temprature1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Temperature of Day "+hday1, 2)
            }
            temprature2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Temperature of Day "+hday2, 2)
            }
            temprature3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Temperature of Day "+hday3, 2)
            }
            temprature4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Temperature of Day "+hday4, 2)
            }
            temprature5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Temperature of Day "+hday5, 2)
            }
            temprature6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Temperature of Day "+hday6, 2)
            }
            temprature7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Temperature of Day "+hday7, 2)
            }

            drugConcetration1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Concentration of Day "+hday1, 2)
            }
            drugConcetration2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Concentration of Day "+hday2, 2)
            }
            drugConcetration3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Concentration of Day "+hday3, 2)
            }
            drugConcetration4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Concentration of Day "+hday4, 2)
            }
            drugConcetration5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Concentration of Day "+hday5, 2)
            }
            drugConcetration6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Concentration of Day "+hday6, 2)
            }
            drugConcetration7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Drug Concentration of Day "+hday7, 2)
            }

            lungs1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # of mL that remain in the lungs of Day "+hday1, 2)
            }
            lungs2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # of mL that remain in the lungs of Day "+hday2, 2)
            }
            lungs3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # of mL that remain in the lungs of Day "+hday3, 2)
            }
            lungs4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # of mL that remain in the lungs of Day "+hday4, 2)
            }
            lungs5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # of mL that remain in the lungs of Day "+hday5, 2)
            }
            lungs6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # of mL that remain in the lungs of Day "+hday6, 2)
            }
            lungs7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter # of mL that remain in the lungs of Day "+hday7, 2)
            }


            expEndTime1.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp End Time of Day "+hday1, 2)
            }
            expEndTime2.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp End Time of Day "+hday2, 2)
            }
            expEndTime3.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp End Time of Day "+hday3, 2)
            }
            expEndTime4.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp End Time of Day "+hday4, 2)
            }
            expEndTime5.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp End Time of Day "+hday5, 2)
            }
            expEndTime6.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp End Time of Day "+hday6, 2)
            }
            expEndTime7.text!!.trim().isEmpty() -> {
                activity?.showToastMsg("Please Enter Exp End Time of Day "+hday7, 2)
            }




            else -> {


                val expDate = arrayListOf<Any>()
                val expTime = arrayListOf<Any>()
                val expLength = arrayListOf<Any>()
                val vapeTime = arrayListOf<Any>()
                val totalVapes = arrayListOf<Any>()
                val expCycle = arrayListOf<Any>()
                val waitTime = arrayListOf<Any>()
                val drugType = arrayListOf<Any>()
                val drugCalc = arrayListOf<Any>()
                val watts = arrayListOf<Any>()
                val temprature = arrayListOf<Any>()
                val drugConcetration = arrayListOf<Any>()
                val lungs = arrayListOf<Any>()
                val expEndTime = arrayListOf<Any>()




                expDate.addAll(listOf(expDate1.getText().toString(), expDate2.getText().toString(), expDate3.getText().toString(),expDate4.getText().toString(),expDate5.getText().toString(),expDate6.getText().toString(),expDate7.getText().toString()))

                expTime.addAll(listOf(expTime1.getText().toString(), expTime2.getText().toString(), expTime3.getText().toString(),expTime4.getText().toString(),expTime5.getText().toString(),expTime6.getText().toString(),expTime7.getText().toString()))

                expLength.addAll(listOf(expLength1.getText().toString(), expLength2.getText().toString(), expLength3.getText().toString(),expLength4.getText().toString(),expLength5.getText().toString(),expLength6.getText().toString(),expLength7.getText().toString()))

                vapeTime.addAll(listOf(vapeTime1.getText().toString(), vapeTime2.getText().toString(), vapeTime3.getText().toString(),vapeTime4.getText().toString(),vapeTime5.getText().toString(),vapeTime6.getText().toString(),vapeTime7.getText().toString()))

                totalVapes.addAll(listOf(totalVapes1.getText().toString(), totalVapes2.getText().toString(), totalVapes3.getText().toString(),totalVapes4.getText().toString(),totalVapes5.getText().toString(),totalVapes6.getText().toString(),totalVapes7.getText().toString()))

                expCycle.addAll(listOf(expCycle1.getText().toString(), expCycle2.getText().toString(), expCycle3.getText().toString(),expCycle4.getText().toString(),expCycle5.getText().toString(),expCycle6.getText().toString(),expCycle7.getText().toString()))

                waitTime.addAll(listOf(waitTime1.getText().toString(), waitTime2.getText().toString(), waitTime3.getText().toString(),waitTime4.getText().toString(),waitTime5.getText().toString(),waitTime6.getText().toString(),waitTime7.getText().toString()))

                drugType.addAll(listOf(drugType1.getText().toString(), drugType2.getText().toString(), drugType3.getText().toString(),drugType4.getText().toString(),drugType5.getText().toString(),drugType6.getText().toString(),drugType7.getText().toString()))

                drugCalc.addAll(listOf(drugCalc1.getText().toString(), drugCalc2.getText().toString(), drugCalc3.getText().toString(),drugCalc4.getText().toString(),drugCalc5.getText().toString(),drugCalc6.getText().toString(),drugCalc7.getText().toString()))

                watts.addAll(listOf(watts1.getText().toString(), watts2.getText().toString(), watts3.getText().toString(),watts4.getText().toString(),watts5.getText().toString(),watts6.getText().toString(),watts7.getText().toString()))

                temprature.addAll(listOf(temprature1.getText().toString(), temprature2.getText().toString(), temprature3.getText().toString(),temprature4.getText().toString(),temprature5.getText().toString(),temprature6.getText().toString(),temprature7.getText().toString()))

                drugConcetration.addAll(listOf(drugConcetration1.getText().toString(), drugConcetration2.getText().toString(), drugConcetration3.getText().toString(),drugConcetration4.getText().toString(),drugConcetration5.getText().toString(),drugConcetration6.getText().toString(),drugConcetration7.getText().toString()))

                lungs.addAll(listOf(lungs1.getText().toString(), lungs2.getText().toString(), lungs3.getText().toString(),lungs4.getText().toString(),lungs5.getText().toString(),lungs6.getText().toString(),lungs7.getText().toString()))

                expEndTime.addAll(listOf(expEndTime1.getText().toString(), expEndTime2.getText().toString(), expEndTime3.getText().toString(),expEndTime4.getText().toString(),expEndTime5.getText().toString(),expEndTime6.getText().toString(),expEndTime7.getText().toString()))





                rejultObj1.addProperty("authToken", token!!);
                rejultObj1.addProperty("calDate", hdate0.toString());
                rejultObj1.addProperty("expDate", expDate.toString());
                rejultObj1.addProperty("expTime", expTime.toString());
                rejultObj1.addProperty("expLength", expLength.toString());
                rejultObj1.addProperty("vapeTime", vapeTime.toString());
                rejultObj1.addProperty("totalVapes", totalVapes.toString());
                rejultObj1.addProperty("expCycle", expCycle.toString());
                rejultObj1.addProperty("waitTime", waitTime.toString());
                rejultObj1.addProperty("drugType", drugType.toString());
                rejultObj1.addProperty("drugCalc", drugCalc.toString());
                rejultObj1.addProperty("watts", watts.toString());
                rejultObj1.addProperty("temprature", temprature.toString());
                rejultObj1.addProperty("drugConcetration", drugConcetration.toString());
                rejultObj1.addProperty("lungs", lungs.toString());
                rejultObj1.addProperty("expEndTime", expEndTime.toString());



                val json: String = Gson().toJson(rejultObj1)


                Log.d("Response::::", json.toString())


                val jsonParser = JsonParser()
                val jsonObject = jsonParser.parse(json).asJsonObject



                RetrofitClient.instance!!.postExpRawJSON(jsonObject)?.enqueue(
                    object : Callback<ExpResultEditResponse> {
                        override fun onFailure(
                            call: Call<ExpResultEditResponse>,
                            t: Throwable
                        ) {
                            t.message?.let { it1 -> activity?.showToastMsg(it1, 2) }

                        }

                        override fun onResponse(
                            call: Call<ExpResultEditResponse>,
                            response: Response<ExpResultEditResponse>
                        ) {
                            if (response.body()!!.status_code==AppConstant.CODE_SUCCESS){

                                calculateResult.setEnabled(true);
                                getAppPref().setString("expDates",expDate.toString())
                                getAppPref().setString("experimentId",response.body()!!.experId)

                                experimentId = response.body()!!.experId;

                               // findNavController().navigate(R.id.action_distressExperimentSetupFragment_to_distressScenarioStep3)
                                activity?.showToastMsg(response.body()!!.message, 1)
                                // Log.i("app:retro:service", "onResponse true ${response.body()!!.toString()}")
                            } else {
                                activity?.showToastMsg(response.body()!!.message, 2)

                            }
                        }
                    }
                )

                // jObj1

                Log.d("Response::::", jsonObject.toString())

            }
        }
    }


        calculateResult.setOnClickListener {



        val builder =
                AlertDialog.Builder(view.context)


            val dialog: AlertDialog = builder.create()
            val inflater = layoutInflater
            val dialogView1: View = inflater.inflate(R.layout.fragment_dialog_with_weeklydata, null)

            val editText1=
                dialogView1.findViewById(R.id.etPassword) as EditText

            editText1.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

            val button2 =
                dialogView1.findViewById(R.id.btnSubmit) as Button


            button2.setOnClickListener { // DO SOMETHINGS

                val progressDialog = ProgressDialog(activity)
                // progressDialog.setTitle("Kotlin Progress Bar")
                progressDialog.setMessage("Loading, please wait")
                progressDialog.show()


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



                            val expDate = arrayListOf<Any>()
                            expDate.addAll(listOf(expDate1.getText().toString(), expDate2.getText().toString(), expDate3.getText().toString(),expDate4.getText().toString(),expDate5.getText().toString(),expDate6.getText().toString(),expDate7.getText().toString()))

                            getAppPref().setString("expDates",expDate.toString())
                            getAppPref().setString("experimentId",experimentId.toString())
                            findNavController().navigate(R.id.action_experimentResultStep2_to_calculatedResultFragment)






                        } else {
                            //progress!!.dismiss()
                            // mContext.showToastMsg(response.body()!!.message,1)

                            activity?.showToastMsg(response.body()!!.message, 2)
                            //Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()

                        }
                        dialog.dismiss()
                        progressDialog.dismiss()
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


fun addDay(date: String, i: Int): String? {
    val dt = date // Start date

    val sdf = SimpleDateFormat("MM/dd/yy")
    val c = Calendar.getInstance()
    try {
        c.time = sdf.parse(dt)
    } catch (e: ParseException) {
        //e.printStackTrace()
    }

    val sdf1 = SimpleDateFormat("dd")


    c.add(Calendar.DATE,i) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

    val day = sdf1.format(c.time)

    return day

}

fun addDate(date: String, i: Int): String? {
    val dt = date // Start date

    val sdf = SimpleDateFormat("MM/dd/yy")
    val c = Calendar.getInstance()
    try {
        c.time = sdf.parse(dt)
    } catch (e: ParseException) {
        //e.printStackTrace()
    }

    val sdf1 = SimpleDateFormat("dd-MM-yyyy")


    c.add(Calendar.DATE,i) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

    val day = sdf1.format(c.time)

    return day

}



}