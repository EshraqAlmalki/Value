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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.value.R
import com.tuwaiq.value.WebViewNewsActivity
import com.tuwaiq.value.newsOfHealthApi.models.HealthNewsItem
const val NEWS_URL = "news"
class TimeLineActiveFragment : Fragment() {

    private lateinit var timelineUserActiveRV:RecyclerView
    private lateinit var activeAdapter: TimelineActiveAdapter


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

    private fun updateUI(news:List<HealthNewsItem>){
        val newAdapter = TimelineActiveAdapter(news)
        timelineUserActiveRV.adapter=newAdapter
        activeAdapter = TimelineActiveAdapter(news)
    }


    private inner class TimelineActiveHolder(view: View):RecyclerView.ViewHolder(view){


        private val shareActive :ImageView = itemView.findViewById(R.id.share_user_steps)
        private val newsTitle:TextView = itemView.findViewById(R.id.news_title)
        private val newsSource:TextView = itemView.findViewById(R.id.news_source)
        private val learnMore:TextView =itemView.findViewById(R.id.learn_more)



        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(news: HealthNewsItem){
          newsTitle.text = news.title
            newsSource.text = news.source


            learnMore.setOnClickListener {
                val newsUrl = news.url


                val intent = Intent(requireContext() , WebViewNewsActivity::class.java).apply {
                    putExtra(NEWS_URL , newsUrl)
                }
                startActivity(intent)
            }



                shareActive.setOnClickListener {

                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT , shareNews())
                    putExtra(Intent.EXTRA_SUBJECT,"share with other")
                    type = "TEXT/plain"
                }.also {
                    val chooserIntent = Intent.createChooser(it,"share it")
                    startActivity(chooserIntent)
                }
            }


        }

        fun shareNews():String{
            val news= HealthNewsItem()
            val healthNews = "here is the news : ${news.title} from ${news.source}"

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

}