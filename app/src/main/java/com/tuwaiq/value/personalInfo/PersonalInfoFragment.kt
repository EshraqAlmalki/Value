package com.tuwaiq.value.personalInfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.tuwaiq.value.R
import com.tuwaiq.value.steps.StepsCountViewModel

const val INFO_KYE = "user-info"
private const val TAG = "InfoFragment"
class PersonalInfoFragment : Fragment() {



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


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout,menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logout_menu -> {

                FirebaseAuth.getInstance().signOut()
                Log.e(TAG, "onOptionsItemSelected: ${FirebaseAuth.getInstance().signOut()}", )

                findNavController().navigate(R.id.loginFragment)
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