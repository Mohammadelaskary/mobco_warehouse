package com.example.mobco.Ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobco.MainActivity
import com.example.mobco.Tools.changeTitle
import com.example.mobco.ViewModel.CountingViewModel
import com.example.mobco.databinding.FragmentCountingBinding
import com.honeywell.aidc.BarcodeFailureEvent
import com.honeywell.aidc.BarcodeReadEvent
import com.honeywell.aidc.BarcodeReader.BarcodeListener
import com.honeywell.aidc.BarcodeReader.TriggerListener
import com.honeywell.aidc.TriggerStateChangeEvent

class CountingFragment : Fragment(),BarcodeListener,TriggerListener {

    companion object {
        fun newInstance() = CountingFragment()
    }

    private lateinit var viewModel: CountingViewModel
    private lateinit var binding: FragmentCountingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CountingViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onBarcodeEvent(p0: BarcodeReadEvent?) {
        TODO("Not yet implemented")
    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {
        TODO("Not yet implemented")
    }

    override fun onTriggerEvent(p0: TriggerStateChangeEvent?) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        changeTitle("Counting",activity as MainActivity)
    }
}