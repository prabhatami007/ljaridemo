package com.vision2020.ui.settings
import BaseFragment
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.vision2020.R
import com.vision2020.ui.BaseViewModel
import com.vision2020.ui.user.LoginActivity
import com.vision2020.utils.getAppPref
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment<BaseViewModel>() {
    override val layoutId: Int
        get() =R.layout.fragment_settings
    override val viewModel: BaseViewModel
        get() = ViewModelProvider(this).get(BaseViewModel::class.java)

    override fun onCreateStuff() {
    }

    override fun initListeners() {
       btnGroup.setOnClickListener {
        findNavController().navigate(R.id.action_navigation_setting_to_createGroupFragment)
       }

        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_setting_to_profileFragment)
        }

        btnLockUnlock.setOnClickListener {
          //  findNavController().navigate(R.id.action_navigation_setting_to_tabletLock)
        }

     /*   btnSyncData.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_setting_to_syncData)
        }
*/

        btnLogout.setOnClickListener {
            val intent = Intent(mContext,LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }



    }


}
