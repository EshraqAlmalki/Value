package com.tuwaiq.value.steps

import android.content.Context
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
import androidx.navigation.fragment.findNavController
import com.tuwaiq.value.R

class StepsCountFragment : Fragment() , SensorEventListener{

    lateinit var stepsTV:TextView
    lateinit var stepsCounter:TextView

    var running = false
    var sensorManager:SensorManager? = null

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
        stepsTV = view.findViewById(R.id.stepsLbl)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = requireActivity().getSystemService(Context
            .SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            showToast("No Step Counter Sensor !")
        } else {
            sensorManager?.registerListener(this, stepsSensor,
                SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause(){
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            if (event != null) {
                stepsCounter.text = " ${event.values[0]}"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun showToast(msg:String){
        Toast.makeText( requireContext(), msg  , Toast.LENGTH_SHORT).show()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        findNavController().navigate(R.id.homePageFragment)
//        findNavController().navigate(R.id.timerFragment3)
//        findNavController().navigate(R.id.personalInfoFragment)
    }

}