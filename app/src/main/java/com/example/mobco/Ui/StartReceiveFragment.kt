package com.example.mobco.Ui

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.example.mobco.Adapters.PoDetailsAdapter
import com.example.mobco.Adapters.ReceivedPOItemAdapter
import com.example.mobco.BarcodeReader
import com.example.mobco.BundleKeys.ORGANIZATION_NUM_KEY
import com.example.mobco.BundleKeys.PURCHASE_ORDER_KEY
import com.example.mobco.MainActivity
import com.example.mobco.Model.ApiRequestBody.PoLine
import com.example.mobco.Model.PODetailsItem
import com.example.mobco.Model.PurchaseOrder
import com.example.mobco.Tools
import com.example.mobco.R
import com.example.mobco.Tools.attachButtonsToListener
import com.example.mobco.Tools.clearInputLayoutError
import com.example.mobco.Tools.containsOnlyDigits
import com.example.mobco.Tools.warningDialog
import com.example.mobco.Util.EditTextActionHandler.OnEnterKeyPressed
import com.example.mobco.Util.LoadingDialog
import com.example.mobco.ViewModel.StartReceiveViewModel
import com.example.mobco.ViewModel.ViewModelFactories.StartReceiveViewModelFactory
import com.example.mobco.databinding.FragmentStartReceiveBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.honeywell.aidc.BarcodeFailureEvent
import com.honeywell.aidc.BarcodeReadEvent
import com.honeywell.aidc.BarcodeReader.BarcodeListener
import com.honeywell.aidc.BarcodeReader.TriggerListener
import com.honeywell.aidc.TriggerStateChangeEvent
import java.util.Calendar

class StartReceiveFragment : Fragment(),View.OnClickListener,BarcodeListener,TriggerListener,PoDetailsAdapter.OnPOLineClicked,ReceivedPOItemAdapter.OnPOLineItemRemoved {

    companion object {
        fun newInstance() = StartReceiveFragment()
    }

    private lateinit var viewModel: StartReceiveViewModel
    private lateinit var binding:FragmentStartReceiveBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartReceiveBinding.inflate(inflater,container,false)
        return binding.root
    }

    private lateinit var purchaseOrder: PurchaseOrder
    private lateinit var organizationNum : String
    private lateinit var loadingDialog:LoadingDialog
    private lateinit var barcodeReader: BarcodeReader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            StartReceiveViewModelFactory(
                activity?.application!!,
                activity
            )
        )[StartReceiveViewModel::class.java]
        barcodeReader = BarcodeReader(this,this)
        loadingDialog = LoadingDialog(requireActivity())
        itemsDialog = PoItemsDialog(requireContext(),this)
        purchaseOrder = Gson().fromJson(requireArguments().getString(PURCHASE_ORDER_KEY),PurchaseOrder::class.java)
        organizationNum = requireArguments().getString(ORGANIZATION_NUM_KEY).toString()
    }
    private lateinit var itemsDialog: PoItemsDialog
    private var itemsList: MutableList<PODetailsItem> = mutableListOf()
    private lateinit var receivedPOItemAdapter: ReceivedPOItemAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachButtonsToListener(this, binding.save, binding.poDetails, binding.receivedList,binding.receivedList, binding.itemsList)
        fillHeaderData()
        binding.qty.editText?.requestFocus()
        viewModel.getPurchaseOrderDetailsList(loadingDialog,organizationNum,purchaseOrder.poHeaderId.toString())
        observeItemsList()
        clearInputLayoutError(binding.qty,binding.itemCode)
        OnEnterKeyPressed(binding.itemCode) {
            val itemCode = binding.itemCode.editText?.text.toString().trim()
            val receivedQty = binding.qty.editText?.text.toString().trim()
            binding.itemCode.error = null
            if (receivedQty.isNotEmpty()) {
                if (vaildItemCode(itemCode,receivedQty)) {
                    handleDataFound(receivedQty)
                }
            } else {
                binding.itemCode.error = getString(R.string.please_enter_received_quantity_first)
            }
        }
        setUpReceivedLinesRecyclerView()
        binding.dateEditText.setOnClickListener {
           showDatePicker(requireContext())
        }
    }
    private val receivedLines: MutableList<PODetailsItem> = mutableListOf()
    private fun setUpReceivedLinesRecyclerView() {
        receivedPOItemAdapter = ReceivedPOItemAdapter(receivedLines,this)
        binding.receivedPoLines.adapter = receivedPOItemAdapter
    }


    private fun observeItemsList() {
        viewModel.itemsList.observe(viewLifecycleOwner){
            itemsList = it as MutableList<PODetailsItem>
            itemsDialog.itemsList = it
        }
    }

    private fun fillHeaderData() {
        binding.poHeader.vendor.text = purchaseOrder.supplier
        binding.poHeader.poNumber.text = purchaseOrder.poNo.toString()
        binding.poHeader.date.text = purchaseOrder.creationDate?.substring(0,10)
        binding.poHeader.operatingUnit.text = purchaseOrder.operatingUnit
        binding.poHeader.poType.text = purchaseOrder.poType
//        if (purchaseOrder.receiptNo!=null){
//            binding.receiptNo.isEnabled = false
//            binding.receiptNo.editText?.setText(purchaseOrder.receiptNo)
//        } else {
//            binding.receiptNo.isEnabled = true
//            binding.receiptNo.editText?.setText("")
//        }
    }


    private val bundle = Bundle()
    private val poLines:MutableList<PoLine> = mutableListOf()
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.save -> {
                if (poLines.isNotEmpty()){
                    viewModel.ItemsReceiving(this,
                        loadingDialog = loadingDialog,
                        poHeaderId = purchaseOrder.poHeaderId!!,
                        poLines,organizationNum, transactionDate = selectedDate!!
                    )
                } else {
                    warningDialog(requireContext(),getString(R.string.enter_receiving_data_first))
                }
            }
            R.id.po_details -> {
                itemsDialog.show()
            }
            R.id.items_list -> {
                itemsDialog.show()
            }
        }
    }

    private fun isReadyToSave(): Boolean {
        var isReady = false
        val itemCode = binding.itemCode.editText?.text.toString().trim()
        val receivedQty = binding.qty.editText?.text.toString().trim()
        if (itemCode.isEmpty()){
            binding.itemCode.error = getString(R.string.please_scan_item_code)
            isReady = false
        }
        if (receivedQty.isEmpty()){
            binding.qty.error = getString(R.string.please_enter_receive_qty)
            isReady = false
        }
        if (!containsOnlyDigits(receivedQty)){
            binding.qty.error = getString(R.string.please_enter_valid_receive_qty)
            isReady = false
        }
        try {
            val receivedQtyInt = Integer.parseInt(receivedQty)
            if (receivedQtyInt>scannedItem!!.remainingQty){
                binding.qty.error = getString(R.string.please_enter_valid_receive_qty)
                isReady = false
            }
        } catch (ex: java.lang.Exception){
            binding.qty.error = getString(R.string.please_enter_valid_receive_qty)
            isReady = false
        }

        if (itemCode.isNotEmpty()&&receivedQty.isNotEmpty()&&containsOnlyDigits(receivedQty)){
            val receivedQtyInt = Integer.parseInt(receivedQty)
            if (receivedQtyInt<=scannedItem!!.remainingQty){
                isReady = true
            }
        }
        return  isReady
    }

    override fun onResume() {
        super.onResume()
        Tools.changeTitle(getString(R.string.start_receive), activity as MainActivity)
        barcodeReader.onResume()
    }

    override fun onPause() {
        barcodeReader.onPause()
        super.onPause()
    }

    override fun onBarcodeEvent(p0: BarcodeReadEvent?) {
        activity?.runOnUiThread {
            val scannedText = barcodeReader.scannedData(p0!!)
            val receivedQty = binding.qty.editText?.text.toString().trim()
            binding.itemCode.error = null
            if (receivedQty.isNotEmpty()) {
                if (vaildItemCode(scannedText,receivedQty)) {
                    handleDataFound(receivedQty)
                }
            } else {
                binding.itemCode.error = getString(R.string.please_enter_received_quantity_first)
            }
        }
    }

    private fun handleDataFound(receivedQty:String) {
        scannedItem?.let {
            receivedLines.add(it)
            receivedPOItemAdapter.receivedQty = receivedQty
            binding.itemCode.editText?.setText(scannedItem?.itemCode!!)
            receivedPOItemAdapter.notifyDataSetChanged()
            val poLine = PoLine(
                po_line_id = it.poLineId!!,
                quantity_received = receivedQty.toInt(),
                ship_to_organization_id = it.shipToOrganizationId!!,
                ship_to_location_id = it.shipToLocationId!!
            )
            poLines.add(poLine)
        }
    }

    private var scannedItem :PODetailsItem? = null
    private fun vaildItemCode(itemCode: String,receivedQty: String): Boolean {
        var isValid = false
        for (position in itemsList.indices){
            if (itemsList[position].itemCode == itemCode){
                if (itemsList[position].remainingQty>0) {
                    if (itemsList[position].remainingQty>=receivedQty.toInt()) {
                        if (!addedBefore(itemsList[position].itemCode)) {
                            scannedItem = itemsList[position]
                            isValid = true
                            break
                        } else {
                            binding.itemCode.error = getString(R.string.added_before)
                            isValid = false
                        }
                    } else {
                        binding.qty.error = getString(R.string.received_qty_must_be_less_or_equal_remaining_qty)
                        isValid = false
                    }
                } else {
                    scannedItem= null
                    binding.itemCode.error = getString(R.string.scanned_item_is_fully_received)
                    isValid = false
                }
            } else {
                scannedItem= null
                binding.itemCode.error = getString(R.string.wrong_item_code)
                isValid = false
            }
        }
        return isValid
    }

    private fun addedBefore(itemCode: String?): Boolean {
        var addedBefore = false
        for (po in receivedLines){
            if (po.itemCode==itemCode){
                addedBefore = true
                break
            } else {
                addedBefore = false
            }
        }
        return addedBefore
    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {
        TODO("Not yet implemented")
    }

    override fun onTriggerEvent(p0: TriggerStateChangeEvent?) {
        barcodeReader.onTrigger(p0!!)
    }

    override fun onPOLineClicked(po: PODetailsItem) {
        val receivedQty = binding.qty.editText?.text.toString().trim()
        itemsDialog.dismiss()
        binding.itemCode.error = null
        if (receivedQty.isNotEmpty()) {
            if (vaildItemCode(po.itemCode!!,receivedQty)) {
                handleDataFound(receivedQty)
            }
        } else {
            binding.itemCode.error = getString(R.string.please_enter_received_quantity_first)
        }
    }


    override fun onPOLineItemRemoved(position: Int) {
        receivedLines.removeAt(position)
        poLines.removeAt(position)
        receivedPOItemAdapter.notifyItemRemoved(position)
    }
    var selectedDate : String? = null
    fun showDatePicker(context: Context) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val yearString = year.toString().substring(2,4)
                val monthName = Tools.monthNumToAbbreviatedName(month+1)
                selectedDate = "${dayOfMonth}-$monthName-$yearString"
                binding.dateEditText.setText(selectedDate)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }
}