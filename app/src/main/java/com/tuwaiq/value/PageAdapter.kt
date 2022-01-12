package com.tuwaiq.value

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tuwaiq.value.authentication.logIn.LoginFragment
import com.tuwaiq.value.authentication.register.NextRegisterFragment
import com.tuwaiq.value.authentication.register.RegisterFragment

class PageAdapter(fm:FragmentManager):FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            1 -> {
                return LoginFragment()
            }

            2 -> {
                return RegisterFragment()
            }
//
//            3 -> {
//                return NextRegisterFragment()
//            }
            else ->{
                return LoginFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        when(position){
            0 ->{
                return "Login"
            }

            1 ->{
                return "Register "
            }
//
//            2 -> {
//                return "continue Register"
//            }


        }
        return super.getPageTitle(position)

    }
}