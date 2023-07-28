package com.example.mobco.Ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.mobco.Adapters.PurchaseOrdersInspectionAdapter
import com.example.mobco.BundleKeys.PO_DETAILS_ITEM_2_Key
import com.example.mobco.MainActivity
import com.example.mobco.Model.PODetailsItem2
import com.example.mobco.Tools
import com.example.mobco.R
import com.example.mobco.ViewModel.InspectionViewModel
import com.example.mobco.ViewModel.ViewModelFactories.InspectionViewModelFactory
import com.example.mobco.databinding.FragmentInspectionBinding

class InspectionFragment : BaseFragment(),View.OnClickListener,PurchaseOrdersInspectionAdapter.POInspectionItemClick {

    companion object {
        fun newInstance() = InspectionFragment()
    }

    private lateinit var viewModel: InspectionViewModel
    private lateinit var binding: FragmentInspectionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInspectionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            InspectionViewModelFactory(
                activity?.application!!,
                activity
            )
        )[InspectionViewModel::class.java]
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

    private lateinit var poAdapter: PurchaseOrdersInspectionAdapter
    private fun setUpRecyclerView() {
        poAdapter = PurchaseOrdersInspectionAdapter(
            this
        )
        binding.poList.adapter = poAdapter
    }

    override fun onResume() {
        super.onResume()
        Tools.changeTitle(getString(R.string.inspection), activity as MainActivity)
    }

    override fun inspectionClicked(poDetailsItem2: PODetailsItem2) {
        val bundle = Bundle()
        bundle.putString(PO_DETAILS_ITEM_2_Key,PODetailsItem2.toJson(poDetailsItem2))
        view?.findNavController()?.navigate(R.id.action_inspectionFragment_to_startInspectionFragment,bundle)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.search ->{
                val organizationNum = binding.organizationNumber.editText?.text.toString().trim()
                val poNum           = binding.poNumber.editText?.text.toString().trim()
                val receiptNum      = binding.receiptNo.editText?.text.toString().trim()
                if (organizationNum.isNotEmpty()){
                    viewModel.getPurchaseOrderReceiptNoList(loadingDialog,organizationNum,poNum,receiptNum)
                } else {
                    binding.organizationNumber.error = getString(R.string.please_enter_organization_number)
                }
            }
        }
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