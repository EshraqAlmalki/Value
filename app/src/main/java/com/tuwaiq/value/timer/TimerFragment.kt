package com.tuwaiq.value.timer

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.tuwaiq.value.R
import nl.dionsegijn.konfetti.KonfettiView

private const val TAG = "TimerFragment"
class TimerFragment : Fragment() {

    lateinit var startBtn:Button
    lateinit var reset:Button
    lateinit var time_edit_text:EditText
    lateinit var viewKonfetti:KonfettiView
    lateinit var timer:TextView
    private var START_MILLI_SECONDS = 60000L
    lateinit var countdown_timer: CountDownTimer
    private var isRunning: Boolean = false
    private var time_in_milli_seconds = 0L


    private val timerViewModel by lazy {
        ViewModelProvider(this)[TimerViewModel::class.java]}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.timer_fragment, container, false
        )
        startBtn = view.findViewById(R.id.button)
        reset = view.findViewById(R.id.reset)
        time_edit_text = view.findViewById(R.id.time_edit_text)
        viewKonfetti = view.findViewById(R.id.viewKonfetti)
        timer = view.findViewById(R.id.timer)



        return view


    }


    override fun onStart() {
        super.onStart()

        startBtn.setOnClickListener {

            val time  = time_edit_text.text.toString()
            if (isRunning){
                pauseTimer()
            }else if (time.isEmpty()) {
                showToast("set timer")
            }else{
                time_in_milli_seconds = time.toLong() *60000L
                startTimer(time_in_milli_seconds)
            }


        }

        reset.setOnClickListener {
            resetTimer()
        }
    }



    private fun showToast(msg:String){
        Toast.makeText( requireContext(),
            msg  , Toast.LENGTH_SHORT).show()

    }




    private fun pauseTimer() {

        startBtn.text = "Start"
        countdown_timer.cancel()
        isRunning = false
        reset.visibility = View.VISIBLE

    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                loadConfeti()
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer.start()

        isRunning = true
        startBtn.text = "Pause"
        reset.visibility = View.INVISIBLE

    }

    private fun resetTimer() {
        time_in_milli_seconds = START_MILLI_SECONDS
        updateTextUI()
        reset.visibility = View.INVISIBLE
    }

    private fun updateTextUI() {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60

      timer.text = "$minute:$seconds"
    }


    private fun loadConfeti() {
        viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(nl.dionsegijn.konfetti.models.Shape.RECT,
                nl.dionsegijn.konfetti.models.Shape.CIRCLE)
            .addSizes(nl.dionsegijn.konfetti.models.Size(12))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        findNavController().navigate(R.id.homePageFragment)
//        findNavController().navigate(R.id.stepsCountFragment3)
//        findNavController().navigate(R.id.personalInfoFragment)
    }

}


