package com.tuwaiq.value.timelineUserActive

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.R
import com.tuwaiq.value.WebViewNewsActivity
import com.tuwaiq.value.newsOfHealthApi.models.HealthNewsItem
const val NEWS_URL = "news"
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


        timeLineActiveViewModel.getNews().observe(
            this , Observer {
                updateUI(it)
            }
        )
    }

    fun updateUI(news:List<HealthNewsItem>){
        val newAdapter = TimelineActiveAdapter(news)
        timelineUserActiveRV.adapter=newAdapter
        activeAdapter = TimelineActiveAdapter(news)
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
                val active = activeAdapter.news[position]
                   // timeLineActiveViewModel.deleteUserInfo()
                    Snackbar.make(
                        requireView(), "delete" , Snackbar.LENGTH_SHORT).apply {
                            setAction("undo"){
                                timeLineActiveViewModel.getUserInfo(
                                    Firebase.auth.currentUser?.email.toString()
                                )
                            }
                        show()
                    }


            }


        }).attachToRecyclerView(timelineUserActiveRV)
    }



    private inner class TimelineActiveHolder(view: View):RecyclerView.ViewHolder(view){

      // private lateinit var news: News
//        private val stepsGoalTV :TextView = itemView.findViewById(R.id.user_activity_sp)
        private val shareActive :ImageView = itemView.findViewById(R.id.share_user_steps)
        private val newsTitle:TextView = itemView.findViewById(R.id.news_title)
        private val newsSource:TextView = itemView.findViewById(R.id.news_source)
       // private val newUrl:TextView = itemView.findViewById(R.id.news_url)
        private val learnMore:TextView =itemView.findViewById(R.id.learn_more)



        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(news: HealthNewsItem){
          newsTitle.text = news.title
            newsSource.text = news.source
           // newUrl.text = news.url
            Log.e(TAG, "bind:${news.url} ", )

            learnMore.setOnClickListener {
                val newsUrl = news.url


                val intent = Intent(requireContext() , WebViewNewsActivity::class.java).apply {
                    putExtra(NEWS_URL , newsUrl)
                }
                startActivity(intent)
            }










//            val shareActiveTime = Instant.parse("2021-01-03T14:00:00.013678Z")
//            val now = Instant.now()
//            val duration = Duration.between(shareActiveTime , now)
//            val showActivity = duration.toHours()
//            Log.e(TAG, "bind: $shareActiveTime", )
//
//            if (showActivity >= 24L ) {
//                if (value.stGoal >= value.steps) {
//                    stepsGoalTV.text = "you reach the goal"
//                } else {
//                    stepsGoalTV.text = "try to reach the goal"
//                }
//            }






                shareActive.setOnClickListener {
                showToast("share it now")
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT , shareNews())
                    putExtra(Intent.EXTRA_SUBJECT,"share with other")
                    setType("TEXT/plain")
                }.also {
                    val chooserIntent = Intent.createChooser(it,"share it")
                    startActivity(chooserIntent)
                }
            }


        }

        fun shareNews():String{
            val news= HealthNewsItem()
            val healthNews = "here is the news : ${news.title}"
//            val getNews = if (news){
//                "this user reach the goal by ${value.stGoal}"
//            }else{
//                "lets work together and reach the goal!"
//            }
//
//            return "try this out i already $getNews"
            return healthNews
        }



    }

    private inner class TimelineActiveAdapter(val news: List<HealthNewsItem>):RecyclerView
    .Adapter<TimelineActiveHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineActiveHolder {

            val view = layoutInflater.inflate(R.layout.user_active_timeline_items,parent,false)
            return TimelineActiveHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: TimelineActiveHolder, position: Int) {

           val news = news[position]
            holder.bind(news)



        }

        override fun getItemCount(): Int = news.size

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }




}