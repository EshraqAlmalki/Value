package com.tuwaiq.value.firestoreUserInfo.personalInfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.R

const val INFO_KYE = "user-info"
private const val TAG = "InfoFragment"
class PersonalInfoFragment : Fragment() {

//    lateinit var usernameET :EditText
//    lateinit var emailET :EditText
//    lateinit var passwordET: EditText
    lateinit var weightTV:TextView
    lateinit var heightTV:TextView
    lateinit var ageTV:TextView
    lateinit var genderTV:TextView
    lateinit var activeTV:TextView
    lateinit var goalTV:TextView


    companion object {
        fun newInstance() = PersonalInfoFragment()
    }


    private val personalInfoViewModel by lazy {
        ViewModelProvider(this)[PersonalInfoViewModel::class.java]}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.personal_info_fragment, container, false)


        weightTV = view.findViewById(R.id.weight_info)
        heightTV = view.findViewById(R.id.height_info)
        ageTV = view.findViewById(R.id.age_info)
        genderTV = view.findViewById(R.id.gender_info)
        activeTV = view.findViewById(R.id.active_level_info)
        goalTV = view.findViewById(R.id.weight_goal_info)
//        usernameET = view.findViewById(R.id.username_info)
//        emailET = view.findViewById(R.id.email_info)
//        passwordET = view.findViewById(R.id.password_info)

        return view
    }

    override fun onStart() {
        super.onStart()

        personalInfoViewModel.userInfo.observe(
            viewLifecycleOwner , Observer {
               it?.let {
                  weightTV.text = it.weight
                  heightTV.text = it.height
                  ageTV.text = it.age
                  genderTV.text = it.gender
                  activeTV.text = it.active
                  goalTV.text = it.weightGoal

               }

            }
        )

        personalInfoViewModel.retrieverUserInfo(Firebase.auth.currentUser?.email.toString())
            .observe(viewLifecycleOwner){
                weightTV.text = it.weight
                heightTV.text = it.height
                ageTV.text = it.age
                genderTV.text = it.gender
                activeTV.text = it.active
                goalTV.text = it.weightGoal
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout,menu)

    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logout_menu -> {

                val builder = context?.let { it -> AlertDialog.Builder(it) }
                builder?.let {
                    it.setMessage("are sure you want to sign out?")
                    it.setCancelable(false)
                    it.setPositiveButton("Yes I'm"){ _ ,_ ->
                        FirebaseAuth.getInstance().signOut()
                        findNavController().navigate(R.id.loginFragment)
                    }
                    it.setNegativeButton("No"){dialog , id -> dialog.dismiss()}
                    val alert = builder.create()
                    alert.show()
                }



                Log.e(TAG, "onOptionsItemSelected: ${FirebaseAuth.getInstance().signOut()}", )


                true
            }else->super.onContextItemSelected(item)
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }





}