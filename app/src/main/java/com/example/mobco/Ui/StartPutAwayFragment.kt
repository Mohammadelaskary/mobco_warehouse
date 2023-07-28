package com.example.mobco.Ui

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import com.example.mobco.BundleKeys.PO_DETAILS_ITEM_2_Key
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.DatePicker
import com.example.mobco.BarcodeReader
import com.example.mobco.Model.PODetailsItem2
import com.example.mobco.R
import com.example.mobco.Tools
import com.example.mobco.Tools.attachButtonsToListener
import com.example.mobco.Tools.clearInputLayoutError
import com.example.mobco.Util.EditTextActionHandler.OnEnterKeyPressed
import com.example.mobco.ViewModel.StartPutAwayViewModel
import com.example.mobco.ViewModel.ViewModelFactories.StartPutAwayViewModelFactory
import com.example.mobco.databinding.FragmentStartPutAwayBinding
import com.honeywell.aidc.BarcodeFailureEvent
import com.honeywell.aidc.BarcodeReadEvent
import com.honeywell.aidc.BarcodeReader.BarcodeListener
import com.honeywell.aidc.BarcodeReader.TriggerListener
import com.honeywell.aidc.TriggerStateChangeEvent
import java.util.Calendar

class StartPutAwayFragment : BaseFragment(),BarcodeListener,TriggerListener,OnClickListener {

    companion object {
        fun newInstance() = StartPutAwayFragment()
    }

    private lateinit var viewModel: StartPutAwayViewModel
    private lateinit var binding:FragmentStartPutAwayBinding
    private lateinit var barcodeReader: BarcodeReader
    private lateinit var poDetailsItem: PODetailsItem2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartPutAwayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            StartPutAwayViewModelFactory(
                activity?.application!!,
                activity
            )
        )[StartPutAwayViewModel::class.java]
        barcodeReader = BarcodeReader(this,this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poDetailsItem = PODetailsItem2.fromJson(arguments?.getString(PO_DETAILS_ITEM_2_Key)!!)
        fillData()
        binding.itemCode.editText?.requestFocus()
        OnEnterKeyPressed(binding.itemCode){
            val scannedItemCode = binding.itemCode.editText?.text.toString().trim()
            if (scannedItemCode==poDetailsItem.itemcode){
                binding.itemCode.editText?.setText(scannedItemCode)
                binding.subInventory.editText?.requestFocus()
            } else {
                binding.itemCode.editText?.setText("")
                binding.itemCode.error = getString(R.string.please_scan_item_code)
            }
        }
        attachButtonsToListener(this,binding.putAway,binding.putAwayList)
        checkFocusedField()
        observePutAwayResponse()
        clearInputLayoutError(binding.itemCode,binding.subInventory,binding.locator)
        binding.dateEditText.setOnClickListener {
            showDatePicker(requireContext())
        }
    }

    private fun observePutAwayResponse() {
        viewModel.newPutAwayQtyValue.observe(requireActivity()){

        }
    }

    private var itemCodeFocused = false
    private var subinventoryFocused = false
    private var locatorFocused = false
    private fun checkFocusedField() {
        binding.itemCode.editText?.setOnFocusChangeListener { view, b ->  itemCodeFocused = b}
        binding.subInventory.editText?.setOnFocusChangeListener { view, b ->  subinventoryFocused = b}
        binding.locator.editText?.setOnFocusChangeListener { view, b ->  locatorFocused = b}
    }

    private fun fillData() {
        binding.vendor.text = poDetailsItem.supplier
        binding.poNumber.text = poDetailsItem.pono
        binding.receiptNumber.text = poDetailsItem.receiptno
        binding.date.text = poDetailsItem.receiptdate?.substring(0,10)
        binding.itemDescription.text = poDetailsItem.itemdesc
        binding.qty.editText?.setText(poDetailsItem.itemqtyaccepted.toString())
        binding.poQty.text = poDetailsItem.itemqty.toString()
        binding.putAwayQty.text = poDetailsItem.itemqtyaccepted.toString()
    }

    override fun onBarcodeEvent(p0: BarcodeReadEvent?) {
        activity?.runOnUiThread {
            val scannedCode = barcodeReader.scannedData(p0!!)
            if (itemCodeFocused) {
                if (scannedCode == poDetailsItem.itemcode) {
                    binding.itemCode.editText?.setText(scannedCode)
                    binding.subInventory.editText?.requestFocus()
                } else {
                    binding.itemCode.editText?.setText("")
                    binding.itemCode.error = getString(R.string.wrong_item_code)
                }
            } else {
                if (subinventoryFocused) {
                    binding.subInventory.editText?.setText(scannedCode)
                    binding.locator.editText?.requestFocus()
                } else {
                    if (locatorFocused) {
                        binding.locator.editText?.setText(scannedCode)
                    }
                }
            }
        }
    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {
    }

    override fun onTriggerEvent(p0: TriggerStateChangeEvent?) {
        barcodeReader.onTrigger(p0!!)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.put_away ->{
                if(readyToSave()){
                    val subInventory = binding.subInventory.editText?.text.toString().trim()
                    val locator      = binding.locator.editText?.text.toString().trim()
                    viewModel.PutAwayMaterial(loadingDialog,
                        poHeaderId = poDetailsItem.poHeaderId!!,
                        poLineId = poDetailsItem.poLineId!!,
                        locator_id = locator,
                        subinventory_code = subInventory,
                        shipToOrganizationId = poDetailsItem.shipToOrganizationId!!,
                        receiptNo = poDetailsItem.receiptno!!,
                        transactionDate = selectedDate!!,
                        startPutAwayFragment = this
                    )
                }

            }
        }
    }

    private fun readyToSave(): Boolean {
        val itemCode = binding.itemCode.editText?.text.toString().trim()
        val subInventory = binding.subInventory.editText?.text.toString().trim()
        val locator      = binding.locator.editText?.text.toString().trim()
        var isReady = true
        if (itemCode.isEmpty()){
            binding.itemCode.error = getString(R.string.please_scan_item_code)
            isReady =  false
        }
        if (subInventory.isEmpty()){
            binding.subInventory.error = getString(R.string.please_scan_subinventory_code)
            isReady =  false
        }
        if (locator.isEmpty()){
            binding.locator.error = getString(R.string.please_scan_locator_code)
            isReady =  false
        }
        return isReady
    }

    override fun onResume() {
        super.onResume()
        barcodeReader.onResume()
    }

    override fun onPause() {
        super.onPause()
        barcodeReader.onPause()
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