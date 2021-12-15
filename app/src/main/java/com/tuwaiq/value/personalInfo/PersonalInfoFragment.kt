package com.tuwaiq.value.personalInfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tuwaiq.value.R

const val INFO_KYE = "user-info"
private const val TAG = "InfoFragment"
class PersonalInfoFragment : Fragment() {



    companion object {
        fun newInstance() = PersonalInfoFragment()
    }

    private lateinit var personalInfoViewModel: PersonalInfoViewModel



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
        personalInfoViewModel = ViewModelProvider(this)[PersonalInfoViewModel::class.java]

    }

     fun onClick(menu: Menu) {
//         val args = Bundle()
//         args.putString(INFO_KYE, "user-info")
//
//         val fragment = NextRegisterFragment()
//         fragment.arguments = args


     }

}