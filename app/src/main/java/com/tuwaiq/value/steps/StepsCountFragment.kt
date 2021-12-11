package com.tuwaiq.value.steps

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tuwaiq.value.R

class StepsCountFragment : Fragment() {

    companion object {
        fun newInstance() = StepsCountFragment()
    }

    private lateinit var viewModel: StepsCountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.steps_count_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StepsCountViewModel::class.java)
        // TODO: Use the ViewModel
    }

}