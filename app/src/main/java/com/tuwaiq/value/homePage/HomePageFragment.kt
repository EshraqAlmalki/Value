package com.tuwaiq.value.homePage

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.R
import com.tuwaiq.value.authentication.register.NextRegisterFragmentDirections
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import java.math.RoundingMode
import java.text.DecimalFormat

class HomePageFragment : Fragment() , SensorEventListener {


    lateinit var calorieTV : TextView
    lateinit var fatTV :TextView
    lateinit var carbTV :TextView
    lateinit var proteinTV :TextView
    lateinit var stepsTV : TextView
    lateinit var stepsGoal : TextView
   // lateinit var usernameTV:TextView

    private var running = false
    private var sensorManager: SensorManager? = null
    private var totalSteps = 0f
    private var previousTotalSteps = 0f


    lateinit var value: Value
    lateinit var rapidResponse: RapidRespnse


    private val args : HomePageFragmentArgs by navArgs()

    companion object {
        fun newInstance() = HomePageFragment()
    }



    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomePageViewModel::class.java]}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        val userInfo = args.email

        homeViewModel.getUserInfo(userInfo)

        onCreateStepsCount()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.home_page_fragment,
            container, false)


        calorieTV = view.findViewById(R.id.calories_tv)
        fatTV = view.findViewById(R.id.fat_tv)
        carbTV = view.findViewById(R.id.carb_tv)
        proteinTV = view.findViewById(R.id.protein_tv)
        stepsTV = view.findViewById(R.id.steps_tv)
        stepsGoal = view.findViewById(R.id.steps_goalEt)
       // usernameTV = view.findViewById(R.id.username_tv)



        rapidResponse= RapidRespnse()


        return view
    }



    override fun onStart() {
        super.onStart()


        //value=Value()

        if(args.email != "-1"){


                    homeViewModel.retrieverUserInfo(Firebase.auth.currentUser?.uid.toString())
                        .observe(viewLifecycleOwner){
                            it?.let {
                                calorieTV.text = it.calor.toDouble().toInt().toString()
                                carbTV.text = it.carb.toDouble().toInt().toString()
                                fatTV.text = it.fat.toDouble().toInt().toString()
                                proteinTV.text = it.protein
//                                stepsTV.text= it.steps
                                stepsGoal.text = it.stGoal
                               // usernameTV.text = it.name



                                Log.e(TAG, "onStart: retriever $it", )
                            }



                        }

            homeViewModel.getUserInfo(Firebase.auth.currentUser?.uid.toString())

                    homeViewModel.userInfo.observe(
                        viewLifecycleOwner, Observer {
                            it?.let {
                                calorieTV.text = it.calor.toDouble().toInt().toString()
                                carbTV.text = it.carb.toDouble().toInt().toString()
                                fatTV.text = it.fat.toDouble().toInt().toString()
                                proteinTV.text = it.protein
//                                stepsTV.text= it.steps
                                stepsGoal.text = it.stGoal
                               // usernameTV.text = it.name

                            }
                            Log.e(TAG, "if onStart: $it",)

                        }
                    )



        }else{

            homeViewModel.userInfo.observe(
                viewLifecycleOwner, Observer {
                    it?.let {

                        calorieTV.text = it.calor.split(".").toString()
                        carbTV.text = it.carb
                        fatTV.text = it.fat
                        proteinTV.text = it.protein
//                        stepsTV.text = it.steps
                        stepsGoal.text = it.stGoal
                       // usernameTV.text = it.name

                        it.calor = calorieTV.text.toString()
                        it.fat = fatTV.text.toString()
                        it.carb = carbTV.text.toString()
                        it.protein = proteinTV.text.toString()

                    }
                    Log.e(TAG, "else onStart: $it",)
                    Log.e(TAG, "onStart: ${args.email} ${it?.email}", )
                }
            )
            
        }
    }

    override fun onResume() {
        super.onResume()
        onResumeStepsCount()
    }

    override fun onPause() {
        super.onPause()
        onPauseStepsCount()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun onCreateStepsCount() {
        value = Value()


        sensorManager = requireActivity().getSystemService(
            Context
                .SENSOR_SERVICE
        ) as SensorManager
    }


    private fun onResumeStepsCount() {
        var stepsSensor = sensorManager?.getDefaultSensor(
            Sensor
                .TYPE_STEP_COUNTER
        )

        if (stepsSensor == null) {
            running = false

        } else {
            sensorManager?.registerListener(
                this, stepsSensor,
                SensorManager.SENSOR_DELAY_UI
            )
            value.steps = stepsTV.text.toString()
            running = true
        }
    }

    private fun onPauseStepsCount() {
        running = false
        sensorManager?.unregisterListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {

            stepsTV.text = ""+ event?.values?.get(0)
//
//            totalSteps = event!!.values[0]
//
//            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
//            stepsCounter.text = ("$currentSteps")
            stepsTV.text = value.steps
            homeViewModel.updateUserInfo(value)

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun showToast(msg:String){
        Toast.makeText( requireContext(), msg  , Toast.LENGTH_SHORT).show()

    }

}