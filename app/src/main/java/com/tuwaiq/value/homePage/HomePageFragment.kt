package com.tuwaiq.value.homePage

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse

class HomePageFragment : Fragment() {

    lateinit var calorieTV : TextView
    lateinit var fatTV :TextView
    lateinit var carbTV :TextView
    lateinit var proteinTV :TextView

    lateinit var value:Value

    lateinit var rapidResponse: RapidRespnse

    private val args : HomePageFragmentArgs by navArgs()

    companion object {
        fun newInstance() = HomePageFragment()
    }



    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomePageViewModel::class.java]}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.home_page_fragment,
            container, false)


        calorieTV = view.findViewById(R.id.calories_tv)
        fatTV = view.findViewById(R.id.fat_tv)
        carbTV = view.findViewById(R.id.carb_tv)
        proteinTV = view.findViewById(R.id.protein_tv)


        rapidResponse= RapidRespnse()


        return view
    }

//
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    override fun onStart() {
        super.onStart()

        homeViewModel.userInfo.observe(
            this , Observer {

                setTextMacros()


                Log.e(TAG, "onStart: ${value.calor}", )
            }
        )

 }


    fun setTextMacros(){

        calorieTV.text = value.calor
        fatTV.text = value.fat
        proteinTV.text = value.protein
        carbTV.text = value.carb
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}