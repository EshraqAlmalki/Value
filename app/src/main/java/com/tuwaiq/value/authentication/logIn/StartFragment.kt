package com.tuwaiq.value.authentication.logIn

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.tuwaiq.value.R
import com.tuwaiq.value.authentication.register.RegisterFragment
import com.tuwaiq.value.general.GeneralFragment

private const val TAG = "StartFragment"
class StartFragment : Fragment() {

    private lateinit var loginBtn:Button
    private lateinit var emailLoginET:EditText
    private lateinit var passwordLogin:EditText
    private lateinit var registerTV:TextView


    private lateinit var auth: FirebaseAuth




    private lateinit var viewModel: StartViewModel





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view = inflater.inflate(R.layout.start_fragment, container, false)
        loginBtn = view.findViewById(R.id.login_btn)
        emailLoginET = view.findViewById(R.id.email_login)
        passwordLogin = view.findViewById(R.id.pas_login)
        registerTV = view.findViewById(R.id.register_click)



        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            val email: String = emailLoginET.text.toString()
            val password: String = passwordLogin.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                showToast("Enter email and password")
            } else{

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task->
                        if (task.isSuccessful){
                            showToast("good job")
                            val fragment = GeneralFragment()
            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit()}
                        }else{
                            showToast("email or password is wrong")
                            Log.e(TAG , "there was something wrong",task.exception)
                        }

                    }
            }



        }

        registerTV.setOnClickListener {
           // Log.e(TAG ,"there is someyhing wrong")
//           val register = RegisterFragment()
//            register.fragmentManager
            val fragment = RegisterFragment()
            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit()}
        }

    }
    private fun showToast(msg:String){
        Toast.makeText( requireContext(), msg  ,Toast.LENGTH_LONG).show()

    }



}