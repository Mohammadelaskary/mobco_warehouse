package com.example.mobco.Ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobco.Adapters.PurchaseOrdersAdapter
import com.example.mobco.Adapters.PurchaseOrdersPutAwayAdapter
import com.example.mobco.BundleKeys.PO_DETAILS_ITEM_2_Key
import com.example.mobco.MainActivity
import com.example.mobco.Model.PODetailsItem2
import com.example.mobco.Model.PurchaseOrder
import com.example.mobco.Tools
import com.example.mobco.R
import com.example.mobco.ViewModel.InspectionViewModel
import com.example.mobco.ViewModel.PutAwayViewModel
import com.example.mobco.ViewModel.ViewModelFactories.InspectionViewModelFactory
import com.example.mobco.ViewModel.ViewModelFactories.PutAwayViewModelFactory
import com.example.mobco.databinding.FragmentPutAwayBinding

class PutAwayFragment : BaseFragment(),View.OnClickListener,PurchaseOrdersPutAwayAdapter.PutAwayItemClick {

    companion object {
        fun newInstance() = PutAwayFragment()
    }

    private lateinit var viewModel: PutAwayViewModel
    private lateinit var binding: FragmentPutAwayBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPutAwayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            PutAwayViewModelFactory(
                activity?.application!!,
                activity
            )
        )[PutAwayViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeGettingPOs()
        Tools.attachButtonsToListener(this,binding.search)
        if (viewModel.orgNum!=null){
            binding.organizationNumber.editText?.setText(viewModel.orgNum)
            binding.poNumber.editText?.setText(viewModel.poNum)
            binding.receiptNo.editText?.setText(viewModel.receiptNo)
            viewModel.getPurchaseOrderReceiptNoList(loadingDialog,viewModel.orgNum!!,viewModel.poNum!!,viewModel.receiptNo!!)
        }
    }
    private fun observeGettingPOs() {
        viewModel.poDetailsItemsLiveData.observe(requireActivity()) {
            poAdapter.poList = it.toTypedArray()
        }
    }
    private lateinit var poAdapter: PurchaseOrdersPutAwayAdapter
    private fun setUpRecyclerView() {
        poAdapter = PurchaseOrdersPutAwayAdapter(this)
        binding.poList.adapter = poAdapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.search ->{
                val organizationNum = binding.organizationNumber.editText?.text.toString().trim()
                val poNumber        = binding.poNumber.editText?.text.toString().trim()
                val receiptNo       = binding.receiptNo.editText?.text.toString().trim()
                if (organizationNum.isNotEmpty()){
                    viewModel.getPurchaseOrderReceiptNoList(loadingDialog,organizationNum,poNumber,receiptNo)
                } else {
                    binding.organizationNumber.error = getString(R.string.please_enter_organization_number)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Tools.changeTitle(getString(R.string.put_away), activity as MainActivity)
    }

    val bundle = Bundle()
    override fun putAwayItemClicked(poDetailsItem2: PODetailsItem2) {
        bundle.putString(PO_DETAILS_ITEM_2_Key,PODetailsItem2.toJson(poDetailsItem2))
        view?.findNavController()?.navigate(R.id.action_putAwayFragment_to_startPutAwayFragment,bundle)
    }
    var organizationNum:String? = null
    override fun onDestroyView() {
        super.onDestroyView()
        organizationNum = binding.organizationNumber.editText?.text.toString().trim()
        val poNum = binding.poNumber.editText?.text.toString().trim()
        val receiptNo = binding.receiptNo.editText?.text.toString().trim()
        viewModel.orgNum = organizationNum
        viewModel.poNum = poNum
        viewModel.receiptNo = receiptNo
    }

}