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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.tuwaiq.value.R
import com.tuwaiq.value.general.GeneralFragment

private const val TAG = "NextRegisterFragment"
class NextRegisterFragment : Fragment() {

    private lateinit var userWeight:EditText
    private lateinit var userHeight:EditText
    private lateinit var userActive:EditText
    private lateinit var stepsGoal:EditText
    private lateinit var weightGoal:EditText
    private lateinit var doneImgV:ImageView

    private lateinit var auth: FirebaseAuth



    private lateinit var viewModel: NextRegisterViewModel

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

        return view
    }





    private fun showToast(msg:String){
        Toast.makeText( requireContext(), msg  , Toast.LENGTH_SHORT).show()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[NextRegisterViewModel::class.java]

    }

    override fun onStart() {
        super.onStart()


        doneImgV.setOnClickListener {

            val weight:String = userWeight.text.toString()
            val height:String = userHeight.text.toString()
            val active:String = userActive.text.toString()
            val stepsG:String = stepsGoal.text.toString()
            val weightG:String = weightGoal.text.toString()

            when{
                weight.isEmpty() -> showToast("Enter weight!")
                height.isEmpty() -> showToast("Enter height!")
                active.isEmpty() -> showToast("Enter active!")
                stepsG.isEmpty() -> showToast("Enter steps goals!")
                weightG.isEmpty() -> showToast("Enter weight goals!")

                else -> {
                    val fragment = GeneralFragment()
                    activity?.let {
                        it.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container,fragment)
                            .addToBackStack(null)
                            .commit()}
                }
            }


        }
    }

}