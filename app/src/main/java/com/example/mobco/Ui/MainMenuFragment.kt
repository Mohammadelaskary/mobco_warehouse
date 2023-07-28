package com.example.mobco.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobco.MainActivity
import com.example.mobco.Tools
import com.example.mobco.R
import com.example.mobco.databinding.FragmentMainMenuBinding


class MainMenuFragment : Fragment() ,OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    lateinit var binding: FragmentMainMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Tools.attachButtonsToListener(this, binding.receiving,binding.issue,binding.transfer,binding.audit,binding.reports,binding.returns)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.receiving -> v.findNavController().navigate(R.id.action_mainMenuFragment_to_receivingMainMenuFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        Tools.changeTitle(getString(R.string.main_menu), activity as MainActivity)
    }
}