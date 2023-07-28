package com.example.mobco.Ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobco.ViewModel.ChangePasswordViewModel
import com.example.mobco.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel
    private lateinit var binding: FragmentChangePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
    }

}