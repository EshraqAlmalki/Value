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
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse

class HomePageFragment : Fragment() {

    private lateinit var calorieTV : TextView
    lateinit var fatTV :TextView
    lateinit var carbTV :TextView
    lateinit var proteinTV :TextView

    companion object {
        fun newInstance() = HomePageFragment()
    }



    val fragmentViewModel by lazy {
        ViewModelProvider(this)[HomePageViewModel::class.java]}



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


        return view
    }


    fun setTextMacros(rapidResponse: RapidRespnse){

        calorieTV.text= rapidResponse.data.calorie.toString()
        fatTV.text= rapidResponse.data.balanced.fat.toString()
        carbTV.text= rapidResponse.data.balanced.carbs.toString()
        proteinTV.text = rapidResponse.data.balanced.protein.toString()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentViewModel.getAllInfo().observe(
            viewLifecycleOwner , Observer {
                Log.e(TAG, "onCreate: there is somthing wrong")
                setTextMacros(it)
            }
        )
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}