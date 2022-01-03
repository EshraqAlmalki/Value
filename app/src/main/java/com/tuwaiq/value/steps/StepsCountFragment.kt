package com.tuwaiq.value.steps

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value

class StepsCountFragment : Fragment() , SensorEventListener{


    lateinit var stepsCounter:TextView
    private var running = false
    private var sensorManager:SensorManager? = null
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    companion object {
        fun newInstance() = StepsCountFragment()
    }


    private val stepsCountViewModel by lazy {
        ViewModelProvider(this)[StepsCountViewModel::class.java]}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.steps_count_fragment,
            container, false)

        stepsCounter = view.findViewById(R.id.stepsValue)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        sensorManager = requireActivity().getSystemService(Context
            .SENSOR_SERVICE) as SensorManager
    }





    override fun onStart() {
        super.onStart()

        resetSteps()

        stepsCountViewModel.getUserInfo(Firebase
            .auth.currentUser?.email.toString())


    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor
            .TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            showToast("No Step Counter Sensor !")
        } else {
            sensorManager?.registerListener(this, stepsSensor,
                SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun resetSteps(){
        stepsCounter.setOnClickListener {
            showToast("long tap to reset steps")
        }

        stepsCounter.setOnLongClickListener {
            previousTotalSteps = totalSteps
            stepsCounter.text = 0.toString()
            showToast("done")


            true
        }
    }




    override fun onPause(){
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {

            totalSteps = event!!.values[0]

            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            stepsCounter.text = ("$currentSteps")

        }
    }




    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }




    private fun showToast(msg:String){
        Toast.makeText( requireContext(), msg  , Toast.LENGTH_SHORT).show()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }



}