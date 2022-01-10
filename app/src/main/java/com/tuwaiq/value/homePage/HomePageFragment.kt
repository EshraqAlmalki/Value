package com.tuwaiq.value.homePage

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import java.math.RoundingMode
import java.text.DecimalFormat

class HomePageFragment : Fragment() {


    lateinit var calorieTV : TextView
    lateinit var fatTV :TextView
    lateinit var carbTV :TextView
    lateinit var proteinTV :TextView
    lateinit var stepsTV : TextView

    lateinit var rapidResponse: RapidRespnse


    private val args : HomePageFragmentArgs by navArgs()

    companion object {
        fun newInstance() = HomePageFragment()
    }



    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomePageViewModel::class.java]}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



       // value=Value()
        val userInfo = args.email

        homeViewModel.getUserInfo(userInfo)
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
        stepsTV = view.findViewById(R.id.steps_tv)


        rapidResponse= RapidRespnse()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    override fun onStart() {
        super.onStart()




        if(args.email != "-1"){

            homeViewModel.retrieverUserInfo(Firebase.auth.currentUser?.uid.toString())
                .observe(viewLifecycleOwner){

                    calorieTV.text = it.calor
                    carbTV.text = it.carb
                    fatTV.text = it.fat
                    proteinTV.text = it.protein
                    stepsTV.text = it.stGoal
                    Log.e(TAG, "onStart: $it observer" , )

                }

            homeViewModel.getUserInfo(Firebase.auth.currentUser?.uid.toString())

            homeViewModel.userInfo.observe(
                viewLifecycleOwner, Observer {
                    it?.let {


                            calorieTV.text = it.calor
                            carbTV.text = it.carb
                            fatTV.text = it.fat
                            proteinTV.text = it.protein
                            stepsTV.text= it.stGoal



                    }
                    Log.e(TAG, "if onStart: $it",)

                }
            )

        }else{

            homeViewModel.userInfo.observe(
                viewLifecycleOwner, Observer {
                    it?.let {

                        calorieTV.text = it.calor

                        carbTV.text = it.carb
                        fatTV.text = it.fat
                        proteinTV.text = it.protein
                        stepsTV.text = it.stGoal
                    }
                    Log.e(TAG, "else onStart: $it",)
                    Log.e(TAG, "onStart: ${args.email} ${it?.email}", )
                }
            )
            
        }
    }


//    private fun bind(value: Value){
//        this.value = value
//        calorieTV.text = value.calor
//        carbTV.text = value.carb
//        fatTV.text = value.fat
//        proteinTV.text = value.protein
//    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}