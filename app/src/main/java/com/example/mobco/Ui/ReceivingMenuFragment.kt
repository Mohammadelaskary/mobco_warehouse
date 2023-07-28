package com.example.mobco.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobco.MainActivity
import com.example.mobco.Tools.attachButtonsToListener
import com.example.mobco.Tools.changeTitle
import com.example.mobco.Tools.showToolBar
import com.example.mobco.R
import com.example.mobco.databinding.FragmentReceivingMenuBinding


class ReceivingMenuFragment : Fragment(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    private lateinit var binding: FragmentReceivingMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReceivingMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ReceivingMenuFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachButtonsToListener(this,binding.receivePo,binding.inspection,binding.putAway)
    }



    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.receive_po -> p0.findNavController().navigate(R.id.action_mainMenuFragment_to_receivePOFragment)
            R.id.inspection -> p0.findNavController().navigate(R.id.action_mainMenuFragment_to_inspectionFragment)
            R.id.put_away -> p0.findNavController().navigate(R.id.action_mainMenuFragment_to_putAwayFragment)
//            R.id.counting -> p0.findNavController().navigate(R.id.action_mainMenuFragment_to_countingFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        showToolBar(activity as MainActivity)
        changeTitle(getString(R.string.receiving_menu),activity as MainActivity)
    }
}