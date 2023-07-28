package com.example.mobco.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mobco.BundleKeys.ORGANIZATION_NUM_KEY
import com.example.mobco.MainActivity
import com.example.mobco.Tools.changeTitle
import com.example.mobco.BundleKeys.PURCHASE_ORDER_KEY
import com.example.mobco.Model.PurchaseOrder
import com.example.mobco.R
import com.example.mobco.Tools.clearInputLayoutError
import com.example.mobco.Util.LoadingDialog
import com.example.mobco.ViewModel.ReceivePOViewModel
import com.example.mobco.ViewModel.ViewModelFactories.ReceivePoViewModelFactory
import com.example.mobco.databinding.FragmentReceivePOBinding
import com.google.gson.Gson

class ReceivePOFragment : BaseFragment(),View.OnClickListener {
    companion object {
        fun newInstance() = ReceivePOFragment()
    }

    private lateinit var viewModel: ReceivePOViewModel
    private lateinit var binding: FragmentReceivePOBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReceivePOBinding.inflate(inflater,container,false)
        return binding.root
    }
    private var purchaseOrder: PurchaseOrder?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            ReceivePoViewModelFactory(
                activity?.application!!,
                activity
            )
        )[ReceivePOViewModel::class.java]
        loadingDialog = LoadingDialog(requireActivity())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachButtonsToListener()
        observeData()
        clearInputLayoutError(binding.poNumber,binding.organizationNumber)
    }
    private fun observeData(){
        viewModel.purchaseOrderLiveData.observe(viewLifecycleOwner) {
            purchaseOrder = it
            fillData(purchaseOrder)
        }
    }

    private fun fillData(purchaseOrder: PurchaseOrder?) {
        binding.poHeaderData.vendor.text = purchaseOrder?.supplier
        binding.poHeaderData.date.text = purchaseOrder?.creationDate?.substring(0,10)
        binding.poHeaderData.poNumber.text = purchaseOrder?.poNo
        binding.poHeaderData.operatingUnit.text = purchaseOrder?.operatingUnit
        binding.poHeaderData.poType.text = purchaseOrder?.poType
    }

    private fun attachButtonsToListener() {
        binding.search.setOnClickListener(this)
        binding.startReceiving.setOnClickListener(this)
        binding.itemsList.setOnClickListener(this)
    }



    override fun onResume() {
        super.onResume()
        changeTitle(getString(R.string.purchase_orders_list), activity as MainActivity)
        if (viewModel.organizationNumber!=null&&viewModel.poNumber!=null){
            binding.poNumber.editText?.setText(viewModel.poNumber)
            binding.organizationNumber.editText?.setText(viewModel.organizationNumber)
            viewModel.getPurchaseOrdersList(loadingDialog,binding.dataGroup, viewModel.organizationNumber!!,viewModel.poNumber!!)
        }
    }
    private var bundle = Bundle()
    override fun onClick(p0: View) {
        val orgNum = binding.organizationNumber.editText?.text.toString().trim()
        val poNum  = binding.poNumber.editText?.text.toString().trim()
        when(p0.id){
            R.id.search -> {
                if (orgNum.isNotEmpty()){
                    if (poNum.isNotEmpty()){
                        viewModel.getPurchaseOrdersList(loadingDialog,binding.dataGroup, orgNum,poNum)
                    } else {
                        binding.poNumber.error = getString(R.string.please_enter_po_number)
                    }
                } else {
                    binding.organizationNumber.error = getString(R.string.please_enter_organization_number)
                }
            }
            R.id.start_receiving ->{
                bundle.putString(PURCHASE_ORDER_KEY, Gson().toJson(purchaseOrder).toString())
                bundle.putString(ORGANIZATION_NUM_KEY,orgNum)
                view?.findNavController()
                    ?.navigate(R.id.action_receivePOFragment_to_startReceiveFragment, bundle)
            }
            R.id.items_list ->{
                bundle.putString(PURCHASE_ORDER_KEY,Gson().toJson(purchaseOrder).toString())
                bundle.putString(ORGANIZATION_NUM_KEY,orgNum)
                view?.findNavController()?.navigate(R.id.action_receivePOFragment_to_PODetailsFragment,bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val organizationNum = binding.organizationNumber.editText?.text.toString().trim()
        val poNumber        = binding.poNumber.editText?.text.toString().trim()
        if (purchaseOrder!=null){
            viewModel.organizationNumber = organizationNum
            viewModel.poNumber = poNumber
        }
    }


}

