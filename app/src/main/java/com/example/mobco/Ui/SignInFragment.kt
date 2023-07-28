package com.example.mobco.Ui

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.mobco.MainActivity
import com.example.mobco.Tools.attachButtonsToListener
import com.example.mobco.Tools.hideToolBar
import com.example.mobco.R
import com.example.mobco.ViewModel.SignInViewModel
import com.example.mobco.databinding.FragmentSignInBinding

class SignInFragment : Fragment(),View.OnClickListener {

    companion object {
        fun newInstance() = SignInFragment()
        var EMPLOYEE_ID = 32849
        var USER_ID = 1014743
    }

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding : FragmentSignInBinding
    private lateinit var settingsDialog: ChangeSettingsDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        settingsDialog = ChangeSettingsDialog(requireContext(), requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        attachButtonsToListener(this,binding.signIn,binding.settings,binding.location)
    }



    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.sign_in -> {
//                val userName = binding.userName.editText?.text.toString().trim()
//                val password = binding.password.editText?.text.toString().trim()
//                if (userName.isNotEmpty()){
//                    if (password.isNotEmpty()){
////                        viewModel.SignIn(userName,password)
//                    } else binding.password.error = getString(R.string.please_enter_your_password)
//                } else binding.userName.error = getString(R.string.please_enter_your_username)
                val intent = Intent(activity,MainActivity::class.java)
                activity?.startActivity(intent)
            }
            R.id.settings -> settingsDialog.show()

        }
    }

    override fun onResume() {
        super.onResume()
        hideToolBar(activity as StartActivity)
    }
}