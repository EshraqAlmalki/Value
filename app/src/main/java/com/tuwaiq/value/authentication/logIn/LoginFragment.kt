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
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value

private const val TAG = "StartFragment"
class LoginFragment : Fragment() {

    private lateinit var loginBtn:Button
    private lateinit var emailLoginET:EditText
    private lateinit var passwordLogin:EditText
    private lateinit var registerTV:TextView

    private lateinit var value:Value

    private lateinit var auth: FirebaseAuth


    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view = inflater.inflate(R.layout.login_fragment,
           container, false)
        loginBtn = view.findViewById(R.id.login_btn)
        emailLoginET = view.findViewById(R.id.email_login)
        passwordLogin = view.findViewById(R.id.pas_login)
        registerTV = view.findViewById(R.id.register_click)



            return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        value=Value()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
         value.email = emailLoginET.text.toString()
         value.password= passwordLogin.text.toString()

            if(TextUtils.isEmpty(value.email) || TextUtils.isEmpty(value.password)) {
                showToast("Enter email and password")
            } else{

                auth.signInWithEmailAndPassword(value.email, value.password)
                    .addOnCompleteListener{ task->
                        if (task.isSuccessful){
                            showToast("good job")


                            viewModel.getUserInfo(email = value.email)
                            //viewModel.saveFirestore(value)


                            findNavController().navigate(R.id.homePageFragment)
                        }else{
                            showToast("email or password is wrong")
                            Log.e(TAG , "there was something wrong",task.exception)
                        }

                    }
            }

        }

        registerTV.setOnClickListener {

         findNavController().navigate(R.id.registerFragment)
        }

    }
    private fun showToast(msg:String){
        Toast.makeText( requireContext(), msg  ,Toast.LENGTH_LONG).show()

    }



}