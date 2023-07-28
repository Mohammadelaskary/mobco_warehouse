package com.example.mobco.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.mobco.Tools.hideToolBar
import com.example.mobco.R
import com.example.mobco.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class SplashFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding : FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SplashFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    override fun onResume() {
        super.onResume()
        hideToolBar(activity as AppCompatActivity)
//        Handler().postDelayed({
//            view?.findNavController()?.navigate(R.id.action_splashFragment_to_signInFragment)
//        }, 2000)
        runBlocking {
            delay(2000)
            view?.findNavController()?.navigate(R.id.action_splashFragment_to_signInFragment)
        }
    }

}