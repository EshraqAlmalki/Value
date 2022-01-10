package com.tuwaiq.value.timer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.tuwaiq.value.R
import com.tuwaiq.value.database.NOTIFICATION_CHANNEL_ID
import com.tuwaiq.value.firestoreUserInfo.personalInfo.PersonalInfoViewModel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.KonfettiView
import java.lang.Exception
import java.util.concurrent.TimeUnit

const val TIMER_RUN = "time-is-up"
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
    private var resources = R.string.notification_channe_name.toString()




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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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

    @SuppressLint("SetTextI18n")
    private fun pauseTimer() {

        startBtn.text = "Start"
        countdown_timer.cancel()
        isRunning = false
        reset.visibility = View.VISIBLE

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(context: Context){

        val channelName = resources

        val channelImportance =NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID
            ,channelName,channelImportance).toString()

        val notificationManager = getSystemService(context,NotificationManager::class.java)

        val builder = context.let {
            NotificationCompat.Builder(it.applicationContext ,channel )
                .setTicker(R.string.timer_end.toString())
                .setSmallIcon(R.drawable.ic_baseline_av_timer_24)
                .setContentTitle(R.string.timer_ends_title.toString())
                .setContentText(R.string.good_jod.toString())
                .setAutoCancel(true)

        }

        val notificationChannel = NotificationChannel(channel ,
            "timer notification" , NotificationManager.IMPORTANCE_DEFAULT)

        notificationManager?.createNotificationChannel(notificationChannel)
        builder.setChannelId(channel)

        val notification = builder.build()

        notificationManager?.notify(1000 , notification)





    }





    @SuppressLint("SetTextI18n")
    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {

                showNotification(context!!)
                loadConfeti()

                Log.e(TAG, "onFinish: error", )

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


    }

}


