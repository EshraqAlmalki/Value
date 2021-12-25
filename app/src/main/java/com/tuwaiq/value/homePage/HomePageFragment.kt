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
import androidx.navigation.fragment.navArgs
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse

class HomePageFragment : Fragment() {


    lateinit var calorieTV : TextView
    lateinit var fatTV :TextView
    lateinit var carbTV :TextView
    lateinit var proteinTV :TextView

    private lateinit var value:Value
    lateinit var rapidResponse: RapidRespnse

    private val args : HomePageFragmentArgs by navArgs()

    companion object {
        fun newInstance() = HomePageFragment()
    }



    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomePageViewModel::class.java]}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



//        value=Value()
//        val userInfo = args.email
//
//        if (userInfo != null){
//            homeViewModel.getUserInfo(userInfo)
//        }
//
//        Log.e(TAG, "onCreate: $value", )
//



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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onStart() {
        super.onStart()

        homeViewModel.userInfo.observe(
            viewLifecycleOwner , Observer {
                value?.let {
                    bind(value)
                }
            }
        )
    }

    fun bind(value: Value){
        this.value = value
        calorieTV.text = value.calor
        carbTV.text = value.carb
        fatTV.text = value.fat
        proteinTV.text = value.protein
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}