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
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value

private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {

    private lateinit var usernameET:EditText
    private lateinit var passwordET:EditText
    private lateinit var confirmPass: EditText
    private lateinit var emailET:EditText
    private lateinit var nextImgV:ImageView
    private lateinit var editNextImgV:ImageView

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
        editNextImgV = view.findViewById(R.id.edit_next_iv)
        value = Value()



        return view
    }

    private fun registerUser(username:String,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    showToast("good job")

                }else{
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
            val username:String = usernameET.text.toString()
           value.email = emailET.text.toString()
            value.password = passwordET.text.toString()
            val confirmPassword = confirmPass.text.toString()



            when{
                username.isEmpty() -> showToast("Enter username please")
                value.email.isEmpty() || !value.email.contains("@")-> showToast("Enter Email please")
                value.password.isEmpty() -> showToast("Enter password please")
                value.password != confirmPassword -> showToast("passwords must be matched" )




                else ->{

                    registerUser(username,value.email,value.password)
                    registerViewModel.addNewUser(value)


                    findNavController().navigate(R.id.nextRegisterFragment)

                }
            }
        }

        editNextImgV.setOnClickListener {
            val username:String = usernameET.text.toString()
            val email:String = emailET.text.toString()
            val password:String = passwordET.text.toString()
            val confirmPassword = confirmPass.text.toString()

            when{
                username.isEmpty() -> showToast("Enter username please")
                email.isEmpty() || !email.contains("@")-> showToast("Enter Email please")
                password.isEmpty() -> showToast("Enter password please")
                password != confirmPassword -> showToast("passwords must be matched" )




                else ->{



                    findNavController().navigate(R.id.nextRegisterFragment)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}