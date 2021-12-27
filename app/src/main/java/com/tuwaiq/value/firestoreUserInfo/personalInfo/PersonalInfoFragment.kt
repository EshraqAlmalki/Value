package com.tuwaiq.value.firestoreUserInfo.personalInfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.value.R

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