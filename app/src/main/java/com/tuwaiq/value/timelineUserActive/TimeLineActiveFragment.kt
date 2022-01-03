package com.tuwaiq.value.timelineUserActive

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.databinding.TimeLineActiveFragmentBinding
import com.tuwaiq.value.databinding.UserActiveTimelineItemsBinding
import java.sql.Time
import java.util.*

class TimeLineActiveFragment : Fragment() {

    private lateinit var timelineUserActiveRV:RecyclerView


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


    }




    private fun showToast(msg:String){
        Toast.makeText( requireContext(),
            msg  , Toast.LENGTH_SHORT).show()

    }

    private inner class TimelineActiveHolder(view: View):RecyclerView.ViewHolder(view){

        private lateinit var value : Value
        private val stepsGoalTV :TextView = itemView.findViewById(R.id.user_activity_sp)
        private val shareActive :ImageView = itemView.findViewById(R.id.share_user_steps)


        fun bind(value:Value){
            this.value = value

                if (value.steps == value.stGoal){
                    stepsGoalTV.text = value.stGoal
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