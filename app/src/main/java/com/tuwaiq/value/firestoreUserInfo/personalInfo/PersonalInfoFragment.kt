package com.tuwaiq.value.firestoreUserInfo.personalInfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.tuwaiq.value.R
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.homePage.HomePageFragmentArgs

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
    lateinit var editBtn:Button
    lateinit var value: Value
    lateinit var auth: FirebaseAuth




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
        editBtn = view.findViewById(R.id.edit_btn)


        return view
    }



    override fun onStart() {
        super.onStart()

        value=Value()



            Log.d(TAG, "email: ${Firebase.auth.currentUser?.email.toString()}")
            personalInfoViewModel.retrieverUserInfo(Firebase.auth.currentUser?.email.toString())
                .observe(viewLifecycleOwner){
                    it?.let {
                        weightTV.text = it.weight
                        heightTV.text = it.height
                        ageTV.text = it.age
                        genderTV.text = it.gender
                        activeTV.text = it.active
                        goalTV.text = it.weightGoal
                    }

                   
                }

            personalInfoViewModel.getUserInfo(Firebase.auth.currentUser?.uid.toString())

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
                    Log.e(TAG, "onStart: if $value", )
                }
            
                       
            )

        Log.e(TAG, "onStart:docId ${value.documentId}", )


        editBtn.setOnClickListener {


          Log.e(TAG, "onStart: $value 2", )
//            Log.d(TAG, "idd ${value.documentId}")
          personalInfoViewModel.updateFirestore(value.documentId, value)
            personalInfoViewModel.updateUserInfo(value)
        }

        val watcher = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                value.gender = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        genderTV.addTextChangedListener(watcher)

        val watcher1 = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                value.age = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        ageTV.addTextChangedListener(watcher1)

        val watcher2 = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                value.weight = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        weightTV.addTextChangedListener(watcher2)

        val watcher3 = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                value.height = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        heightTV.addTextChangedListener(watcher3)

        val watcher4 = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                value.active = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        activeTV.addTextChangedListener(watcher4)

        val watcher5 = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                value.weightGoal = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        goalTV.addTextChangedListener(watcher5)
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
                        auth.signOut()
                        findNavController().navigate(R.id.loginFragment)
                    }
                    it.setNegativeButton("No"){dialog , id -> dialog.dismiss()}
                    val alert = builder.create()
                    alert.show()
                }

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

        auth = FirebaseAuth.getInstance()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personalInfoViewModel.userInfo.observe(
            viewLifecycleOwner , Observer {
                it?.let {
                    it.gender = genderTV.toString()
                    it.height = heightTV.toString()
                    it.age = ageTV.toString()
                    it.weight = weightTV.toString()
                    it.active = activeTV.toString()
                    it.weightGoal = goalTV.toString()
                }
                Log.e(TAG, "onViewCreated: $value", )
            }
        )

    }




}