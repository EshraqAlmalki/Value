package com.tuwaiq.value.timer

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
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
import androidx.lifecycle.Observer
import androidx.work.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.PolylineOptions
import com.tuwaiq.value.R
import com.tuwaiq.value.database.NOTIFICATION_CHANNEL_ID
import nl.dionsegijn.konfetti.KonfettiView
import pub.devrel.easypermissions.EasyPermissions
import kotlin.math.roundToInt

const val TIMER_RUN = "time-is-up"
private const val TAG = "TimerFragment"
private const val REQUEST_CODE_LOCATION_PERMISSION = 0
private const val MAP_ZOOM = 15f

class TimerFragment : Fragment(){


    private var START_MILLI_SECONDS = 60000L
    private var isRunning: Boolean = false
    private var time_in_milli_seconds = 0L
    private var resources = R.string.notification_channe_name.toString()
    private var time = 0.0
    private var map:GoogleMap? = null
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var curTimeInMilli = 0L


    private lateinit var startBtn:Button
    private lateinit var reset:Button
    private lateinit var time_edit_text:EditText
    private lateinit var viewKonfetti:KonfettiView
    private lateinit var timer:TextView
    private lateinit var countdown_timer: CountDownTimer
    private lateinit var mapView: MapView
    private lateinit var toggleButton:Button
    private lateinit var serviceIntent: Intent





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
        mapView = view.findViewById(R.id.mapView)
        toggleButton = view.findViewById(R.id.toggle_btn)

        return view


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleButton.setOnClickListener {
           toggleRun()
        }


        mapView?.onCreate(savedInstanceState)


        mapView?.getMapAsync {
            map = it
            addAllPolyline()
        }

        subscribeToObservers()
    }


    private fun sendCommandToService(action :String) =
        Intent(requireContext() , TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)

        }

    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(viewLifecycleOwner , Observer {
            updateTracking(it)})


        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
           pathPoints = it
            addLatestPolyline()
            moveCamToUser()

        })

        TrackingService.timeRunInMil.observe(viewLifecycleOwner, Observer {
            curTimeInMilli = it
            val formattedTime = TrackingUtility.FormattedStopWatchTime(curTimeInMilli,
            true)
            timer.text = formattedTime
        })
    }

    private fun toggleRun(){
        if (isTracking){
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking:Boolean){
      this.isTracking = isTracking
        if (!isTracking){
            toggleButton.text = "start"
            toggleButton.visibility = View.VISIBLE
        }else{
            toggleButton.text = "stop"
            toggleButton.visibility = View.GONE
        }

    }

    private fun moveCamToUser(){
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(), MAP_ZOOM
                )
            )
        }
    }


    private fun addAllPolyline(){
        for (polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(R.color.value_green)
                .width(8f)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {


       if (pathPoints.isNotEmpty() && pathPoints.last().size > 1 ){
           val preLastLatLng = pathPoints.last()[pathPoints.last().size -2]
           val lastLatLng = pathPoints.last().last()
           val polylineOptions = PolylineOptions()
               .color(R.color.value_green)
               .width(8f)
               .add(preLastLatLng)
               .add(lastLatLng)
           map?.addPolyline(polylineOptions)
       }
    }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()



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


    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
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

        val resources = getResources()
        val builder = context.let {
            NotificationCompat.Builder(it.applicationContext ,channel )
                .setTicker(R.string.timer_end.toString())
                .setSmallIcon(R.drawable.ic_baseline_av_timer_24)
                .setContentTitle(resources.getString(R.string.timer_ends_title))
                .setContentText(resources.getString(R.string.good_jod))
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

                Log.e(TAG, "onFinish: error")

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

//    private fun requestPermissions() {
//        if(TrackingUtility.locationPer(requireContext())) {
//            return
//        }
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            EasyPermissions.requestPermissions(
//                this,
//                "You need to accept location permissions to use this app.",
//                REQUEST_CODE_LOCATION_PERMISSION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        } else {
//            EasyPermissions.requestPermissions(
//                this,
//                "You need to accept location permissions to use this app.",
//                REQUEST_CODE_LOCATION_PERMISSION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            )
//        }
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            AppSettingsDialog.Builder(this).build().show()
//        } else {
//            requestPermissions()
//        }
//    }
//
//    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }




    private fun getTimeStringFromDouble(time: Double): String
    {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String
    = String.format("%02d:%02d:%02d", hour, min, sec)




//   companion object TrackingUtility {
//        fun locationPer(context: Context):Boolean{
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
//                EasyPermissions.hasPermissions(context
//                , Manifest.permission.ACCESS_FINE_LOCATION
//                , Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            }else{
//                EasyPermissions.hasPermissions(context
//                    , Manifest.permission.ACCESS_FINE_LOCATION
//                    , Manifest.permission.ACCESS_COARSE_LOCATION
//                    , Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                )
//            }
//
//            return true
//        }
//    }
}


