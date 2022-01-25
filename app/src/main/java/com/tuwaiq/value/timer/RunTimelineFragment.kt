package com.tuwaiq.value.timer

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Run
import com.tuwaiq.value.timelineUserActive.TimeLineActiveFragment
import com.tuwaiq.value.timelineUserActive.TimeLineActiveViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE_LOCATION_PERMISSION = 0

class RunTimelineFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    lateinit var runActivityRV:RecyclerView
    private lateinit var runActiveAdapter:RunTimelineAdapter

    private val runTimeLineViewModel by lazy {
        ViewModelProvider(this)[RunTimelineViewModel::class.java]
    }


    lateinit var floatingBtn:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onStart() {
        super.onStart()
        runTimeLineViewModel.getAllActivities.observe(
            viewLifecycleOwner , Observer {
                updateUI(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_run_timeline, container, false
        )

        floatingBtn = view.findViewById(R.id.run_floating)

        runActivityRV=  view.findViewById(R.id.user_run_active_rv)
        val linearLayoutManager = LinearLayoutManager(context)
        runActivityRV.layoutManager = linearLayoutManager



        return view
    }

    fun updateUI(run:List<Run>){
        val newRunAdapter = RunTimelineAdapter(run)
        runActivityRV.adapter= newRunAdapter
        runActiveAdapter = RunTimelineAdapter(run)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()

        floatingBtn.setOnClickListener {
            findNavController().navigate(R.id.timerFragment3)
        }

    }

    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }



    private inner class RunTimelineHolder(view: View):RecyclerView.ViewHolder(view){
        private val zoomOutPolyline:ImageView = itemView.findViewById(R.id.zoom_out_map_iv)
        private val caloriesBurned:TextView = itemView.findViewById(R.id.calories_burned_tv)
        private val distance : TextView = view.findViewById(R.id.distance_tv)


        @SuppressLint("SimpleDateFormat")
        fun bind(run: Run){
            caloriesBurned.text = run.caloriesBurned.toString()
            distance.text = run.distanceInMeters.toString()
//
//            var currentDate = Date()
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
//            currentDate = dateFormat.parse(dateFormat.format(currentDate))
//            runDate.text = currentDate.toString()

                Glide.with(requireContext()).load(run.imgL).into(zoomOutPolyline)



        }
    }

    private inner class RunTimelineAdapter(val run : List<Run>):RecyclerView.Adapter<RunTimelineHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunTimelineHolder {

            val view = layoutInflater.inflate(R.layout.run_user_active_item,parent,false)
            return RunTimelineHolder(view)

        }

        override fun onBindViewHolder(holder: RunTimelineHolder, position: Int) {

            val run = run[position]
            holder.bind(run)
        }

        override fun getItemCount(): Int = run.size

    }


}