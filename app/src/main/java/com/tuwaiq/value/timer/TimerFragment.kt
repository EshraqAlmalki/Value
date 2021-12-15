package com.tuwaiq.value.timer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.tuwaiq.value.R

private const val TAG = "TimerFragment"
class TimerFragment : Fragment() {

    var counter = 0

    companion object {
        fun newInstance() = TimerFragment()
    }

    private lateinit var viewModel: TimerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val  view =  inflater.inflate(R.layout.timer_fragment, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[TimerViewModel::class.java]

    }

    fun startTimeCounter(view: View) {
        val countTime:TextView =view.findViewById(R.id.countTime)
       val startCount:Button = view.findViewById(R.id.button)


            object : CountDownTimer(50000, 1000),View.OnClickListener {
                override fun onTick(millisUntilFinished: Long) {

                    countTime.text = counter.toString()

                    counter++


                }
                override fun onFinish() {
                    countTime.text = "Finished"
                }

                override fun onClick(v: View?) {

                    startTimeCounter(view)
                }


            }.start()
        }

    }

