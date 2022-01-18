package com.tuwaiq.value.authentication.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextSwitcher
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.PageAdapter
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.homePage.HomePageFragmentArgs
import java.util.regex.Pattern

private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {

    private lateinit var usernameET:EditText
    private lateinit var passwordET:EditText
    private lateinit var confirmPass: EditText
    private lateinit var emailET:EditText
    private lateinit var nextImgV:ImageView
  //  private lateinit var editNextImgV:ImageView

    lateinit var value:Value
    private lateinit var auth: FirebaseAuth



    private val registerViewModel by lazy { ViewModelProvider(this)[RegisterViewModel::class.java]}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_fragment, container, false)
        usernameET = view.findViewById(R.id.username_et)
        passwordET = view.findViewById(R.id.pass_et)
        confirmPass = view.findViewById(R.id.confirm_et)
        emailET = view.findViewById(R.id.email_et)
        nextImgV = view.findViewById(R.id.next_iv)
       // editNextImgV = view.findViewById(R.id.edit_next_iv)
        value = Value()
//
//        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
//        viewPager.adapter = fragmentManager?.let { PageAdapter(it) }
//
//        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
//        tabLayout.setupWithViewPager(viewPager)



        return view
    }

    private fun registerUser(username:String,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    
                    showToast("good job")
                    Log.e(TAG, "registerUser: wahoooo", )

                }else{
                       showToast("email address already taken")
                    Log.e(TAG , "there was something wrong",task.exception)
                }

            }
        val updateProfile = userProfileChangeRequest{
            displayName = username
        }

        auth.currentUser?.updateProfile(updateProfile)

    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            Log.d(TAG , "hi ${currentUser.displayName}")
        }




        nextImgV.setOnClickListener {
           
            value.name = usernameET.text.toString()
           value.email = emailET.text.toString()
            value.password = passwordET.text.toString()
            val confirmPassword = confirmPass.text.toString()
            
           val tempEmail = value.email

            when{

                value.name.isEmpty() -> showToast("Enter username please")

                value.email.isEmpty() || !value.email.contains("@") -> showToast("Enter Email please")
                value.password.isEmpty() -> showToast("Enter password please")
                value.password != confirmPassword -> showToast("passwords must be matched" )


                else ->{



                    registerUser(value.name,value.email,value.password)


                    registerViewModel.addNewUser(value)
                   
                    val action = RegisterFragmentDirections
                        .actionRegisterFragmentToNextRegisterFragment(email = tempEmail)
                    Log.e(TAG, "onStart: $tempEmail", )
                    findNavController().navigate(action)

                }
            }
        }



    }

    private fun showToast(msg:String){
        Toast.makeText( requireContext(), msg  , Toast.LENGTH_LONG).show()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

//    private fun check():Boolean{
//
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        registerViewModel.userInfo.observe(
            viewLifecycleOwner , Observer {
                if (it != null) {
                    emailET.setText(it.email)
                }
            }
        )

    }

}