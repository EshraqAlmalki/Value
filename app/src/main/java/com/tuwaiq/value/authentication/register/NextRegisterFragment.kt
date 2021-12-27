package com.tuwaiq.value.authentication.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import com.tuwaiq.value.homePage.HomePageFragmentArgs


private const val INFO_KYE = "user-info"
private const val TAG = "NextRegisterFragment"
class NextRegisterFragment : Fragment() {

    private lateinit var userWeight:EditText
    private lateinit var userHeight:EditText
    private lateinit var userActive:EditText
    private lateinit var stepsGoal:EditText
    private lateinit var weightGoal:EditText
    private lateinit var doneImgV:ImageView
    private lateinit var ageET:EditText
    private lateinit var genderET:EditText
    lateinit var rapidResponse:RapidRespnse


    lateinit var value:Value

    private lateinit var auth: FirebaseAuth


    private val args : NextRegisterFragmentArgs by navArgs()


    private val nextRegisterViewModel by lazy { ViewModelProvider(this)[NextRegisterViewModel::class.java]}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.next_register_fragment
            , container, false)

        userWeight = view.findViewById(R.id.weight_et)
        userHeight = view.findViewById(R.id.height_et)
        userActive = view.findViewById(R.id.active_et)
        stepsGoal = view.findViewById(R.id.steps_goal)
        weightGoal = view.findViewById(R.id.weight_goals)
        doneImgV = view.findViewById(R.id.done_iv)
        ageET = view.findViewById(R.id.age_et)
        genderET = view.findViewById(R.id.gender_et)


        rapidResponse=RapidRespnse()

        return view
    }





    private fun showToast(msg:String){
        Toast.makeText( requireContext(),
            msg  , Toast.LENGTH_SHORT).show()

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        value = Value()

        val userInfo = args.email

        nextRegisterViewModel.getUserInfo(userInfo)
    }


    override fun onStart() {
        super.onStart()


        doneImgV.setOnClickListener {


            value.weight = userWeight.text.toString()
            value.height = userHeight.text.toString()
            value.active = userActive.text.toString()
            value.stGoal = stepsGoal.text.toString()
            value.weightGoal= weightGoal.text.toString()
            value.age= ageET.text.toString()
            value.gender = genderET.text.toString()




            when{
                value.weight.isEmpty() -> showToast("Enter weight!")
                value.height.isEmpty() -> showToast("Enter height!")
                value.active.isEmpty() -> showToast("Enter active!")
                value.stGoal.isEmpty() -> showToast("Enter steps goals!")
                value.weightGoal.isEmpty() -> showToast("Enter weight goals!")
                value.age.isEmpty() -> showToast("Enter age!")
                value.gender.isEmpty() -> showToast("Enter gender!")

                else -> {



                    nextRegisterViewModel.macrosCount(age = value.age , gender = value.gender ,
                    weight = value.weight ,height = value.height, goal = value.weightGoal ,
                    activityLevel = value.active)
                        .observe(
                        viewLifecycleOwner , Observer {

                                rapidResponse ->


                                value.calor = rapidResponse.data?.calorie.toString()
                                value.fat = rapidResponse.data?.balanced?.fat.toString()
                                value.carb = rapidResponse.data?.balanced?.protein.toString()
                                value.protein = rapidResponse.data?.balanced?.protein.toString()
                                value.email = args.email


                                Log.e(TAG, "onStart: $value")
                                Log.e(TAG, "onStart fat: ${value.fat}" )
                                nextRegisterViewModel.updateUserInfo(value)
                                nextRegisterViewModel.saveFirestore(value)
                                Log.e(TAG, "onStart: firestore: ${nextRegisterViewModel.saveFirestore(value)}", )
                                val action = NextRegisterFragmentDirections
                                    .actionNextRegisterFragmentToHomePageFragment(email = args.email)
                                Log.e(TAG, "onStart: ${args.email}", )

                                findNavController().navigate(action)
                        }

                   )


                }

            }

        }

    }

}