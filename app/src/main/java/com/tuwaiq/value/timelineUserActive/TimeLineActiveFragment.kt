package com.tuwaiq.value.timelineUserActive

import android.app.ProgressDialog.show
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value

import java.sql.Time
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class TimeLineActiveFragment : Fragment() {

    private lateinit var timelineUserActiveRV:RecyclerView
    private lateinit var activeAdapter: TimelineActiveAdapter






    companion object {
        fun newInstance() = TimeLineActiveFragment()
    }

   private val timeLineActiveViewModel by lazy {
       ViewModelProvider(this)[TimeLineActiveViewModel::class.java]
   }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view = inflater.inflate(R.layout.time_line_active_fragment,container,false)

        timelineUserActiveRV=view.findViewById(R.id.user_active_rv)
        val linearLayoutManager = LinearLayoutManager(context)
        timelineUserActiveRV.layoutManager = linearLayoutManager



        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun updateUI(value:List<Value>){
        val valueAdapter = TimelineActiveAdapter(value)
        timelineUserActiveRV.adapter=valueAdapter
        activeAdapter = TimelineActiveAdapter(value)
    }




    private fun showToast(msg:String){
        Toast.makeText( requireContext(),
            msg  , Toast.LENGTH_SHORT).show()

    }

    override fun onStart() {
        super.onStart()

        swipeToDel()
    }
    fun swipeToDel(){
        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val active = activeAdapter.value[position]
                    timeLineActiveViewModel.deleteUserInfo(active)
                    Snackbar.make(
                        requireView(), "delete" , Snackbar.LENGTH_SHORT
                    ).show()

            }


        }).attachToRecyclerView(timelineUserActiveRV)
    }



    private inner class TimelineActiveHolder(view: View):RecyclerView.ViewHolder(view){

        private lateinit var value : Value
        private val stepsGoalTV :TextView = itemView.findViewById(R.id.user_activity_sp)
        private val shareActive :ImageView = itemView.findViewById(R.id.share_user_steps)



        fun bind(value:Value){
            //this.value = value





            Log.e(TAG, "bind: ${value.stGoal}", )


            val shareActiveTime = Instant.parse("2021-01-03T14:00:00.013678Z")
            val now = Instant.now()
            val duration = Duration.between(shareActiveTime , now)
            val showActivity = duration.toHours()
            Log.e(TAG, "bind: $shareActiveTime", )

            if (showActivity >= 24L ) {
                if (value.stGoal >= value.steps) {
                    stepsGoalTV.text = "you reach the goal"
                } else {
                    stepsGoalTV.text = "try to reach the goal"
                }
            }






                shareActive.setOnClickListener {
                showToast("share it now")
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT , shareSteps())
                    putExtra(Intent.EXTRA_SUBJECT,"share with other")
                }.also {
                    val chooserIntent = Intent.createChooser(it,"share it")
                    startActivity(chooserIntent)
                }
            }


        }

        fun shareSteps():String{
            val stepsActive = if (value.stGoal.isNotBlank()){
                "this user reach the goal by ${value.stGoal}"
            }else{
                "lets work together and reach the goal!"
            }

            return "try this out i already $stepsActive"
        }



    }

    private inner class TimelineActiveAdapter(val value: List<Value>):RecyclerView
    .Adapter<TimelineActiveHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineActiveHolder {

            val view = layoutInflater.inflate(R.layout.user_active_timeline_items,parent,false)
            return TimelineActiveHolder(view)
        }

        override fun onBindViewHolder(holder: TimelineActiveHolder, position: Int) {

           val value = value[position]
            holder.bind(value)



        }

        override fun getItemCount(): Int = value.size

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeLineActiveViewModel.getAllUserInfo().observe(
            viewLifecycleOwner, Observer {
                updateUI(it)
            }
        )
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }




}