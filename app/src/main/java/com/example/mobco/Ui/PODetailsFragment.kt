package com.example.mobco.Ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobco.Adapters.PoDetailsAdapter
import com.example.mobco.BundleKeys.ORGANIZATION_NUM_KEY
import com.example.mobco.BundleKeys.PURCHASE_ORDER_KEY
import com.example.mobco.MainActivity
import com.example.mobco.Model.PODetailsItem
import com.example.mobco.Model.PurchaseOrder
import com.example.mobco.Tools
import com.example.mobco.R
import com.example.mobco.Util.LoadingDialog
import com.example.mobco.ViewModel.PODetailsViewModel
import com.example.mobco.ViewModel.ViewModelFactories.PODetailsViewModelFactory
import com.example.mobco.databinding.FragmentPODetailsBinding
import com.google.gson.Gson

class PODetailsFragment :
    Fragment(),PoDetailsAdapter.OnPOLineClicked {

    companion object {
        fun newInstance() = PODetailsFragment()
    }

    private lateinit var viewModel: PODetailsViewModel
    private lateinit var binding: FragmentPODetailsBinding
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPODetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
    lateinit var purchaseOrder : PurchaseOrder
    private lateinit var organizationNum :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            PODetailsViewModelFactory(
                activity?.application!!,
                activity
            )
        )[PODetailsViewModel::class.java]
        loadingDialog = LoadingDialog(requireActivity())
        purchaseOrder = Gson().fromJson(requireArguments().getString(PURCHASE_ORDER_KEY),PurchaseOrder::class.java)
        organizationNum = requireArguments().getString(ORGANIZATION_NUM_KEY).toString()
    }

    override fun onResume() {
        super.onResume()
        Tools.changeTitle(getString(R.string.p_o_details),activity as MainActivity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillHeaderData()
        viewModel.getPurchaseOrderDetailsList(loadingDialog,poDetailsAdapter,organizationNum,purchaseOrder.poHeaderId!!.toString())
        setUpRecyclerView()

    }

    private fun fillHeaderData() {
        binding.poHeader.vendor.text = purchaseOrder.supplier
        binding.poHeader.poNumber.text = purchaseOrder.poNo.toString()
        binding.poHeader.operatingUnit.text = purchaseOrder.operatingUnit
        binding.poHeader.poType.text = purchaseOrder.poType
    }

    private var poDetailsAdapter: PoDetailsAdapter = PoDetailsAdapter(this)
    private fun setUpRecyclerView() {
        binding.itemsList.adapter = poDetailsAdapter
    }

    override fun onPOLineClicked(po: PODetailsItem) {
        TODO("Not yet implemented")
    }
}