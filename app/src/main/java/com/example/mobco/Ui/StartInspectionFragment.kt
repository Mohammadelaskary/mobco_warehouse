package com.example.mobco.Ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mobco.BarcodeReader
import com.example.mobco.BundleKeys.PO_DETAILS_ITEM_2_Key
import com.example.mobco.Model.PODetailsItem2
import com.example.mobco.R
import com.example.mobco.Tools
import com.example.mobco.ViewModel.StartInspectionViewModel
import com.example.mobco.ViewModel.ViewModelFactories.StartInspectionViewModelFactory
import com.example.mobco.databinding.FragmentStartInspectionBinding
import com.honeywell.aidc.BarcodeFailureEvent
import com.honeywell.aidc.BarcodeReadEvent
import com.honeywell.aidc.BarcodeReader.BarcodeListener
import com.honeywell.aidc.BarcodeReader.TriggerListener
import com.honeywell.aidc.TriggerStateChangeEvent
import java.util.Calendar

class StartInspectionFragment : BaseFragment(),View.OnClickListener,BarcodeListener,TriggerListener {

    companion object {
        fun newInstance() = StartInspectionFragment()
    }

    private lateinit var viewModel: StartInspectionViewModel
    private lateinit var binding: FragmentStartInspectionBinding
    private lateinit var barcodeReader: BarcodeReader
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartInspectionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            StartInspectionViewModelFactory(
                activity?.application!!,
                activity
            )
        )[StartInspectionViewModel::class.java]
        barcodeReader = BarcodeReader(this,this)

    }
    private lateinit var poDetailsItem2: PODetailsItem2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poDetailsItem2 = PODetailsItem2.fromJson(arguments?.getString(PO_DETAILS_ITEM_2_Key)!!)
        fillPOData()
        handleAcceptedQtyTextChange()
        Tools.attachButtonsToListener(this,binding.save,binding.poDetails)
        binding.dateEditText.setOnClickListener {
            showDatePicker(requireContext())
        }
    }

    private fun handleAcceptedQtyTextChange() {
        binding.acceptedQty.editText?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.acceptedQty.error = null
                if (binding.acceptedQty.editText?.text.toString().isNotEmpty()) {
                    val acceptedQty = binding.acceptedQty.editText?.text.toString().toInt()
                    if (acceptedQty <= poDetailsItem2.itemqtyreceived!!) {
                        val rejectedQty = poDetailsItem2.itemqtyreceived!! - acceptedQty
                        binding.rejectedQty.editText?.setText(rejectedQty.toString())
                    } else {
                        binding.rejectedQty.editText?.setText("0")
                        binding.acceptedQty.error =
                            getString(R.string.accepted_qty_must_be_less_or_equal_to_received_qty)
                    }
                } else {
                    binding.acceptedQty.editText?.setText("0")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun fillPOData() {
        binding.poNumber.text = poDetailsItem2.pono.toString()
        binding.vendor.text = poDetailsItem2.supplier.toString()
        binding.date.text = poDetailsItem2.receiptdate.toString().substring(0,10)
        binding.itemDescription.text = poDetailsItem2.itemdesc.toString()
        binding.receivedQty.text = poDetailsItem2.itemqtyreceived.toString()
        binding.poQty.text = poDetailsItem2.itemqty.toString()
        binding.acceptedQty.editText?.setText(poDetailsItem2.itemqtyreceived.toString())
        binding.rejectedQty.editText?.setText("0")
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.save ->{
                val itemCode = binding.itemCode.editText?.text.toString().trim()
                val acceptedQty = binding.acceptedQty.editText?.text.toString().trim()
                if (itemCode.isNotEmpty()) {
                    if (acceptedQty.isNotEmpty()){
                        if (Tools.containsOnlyDigits(acceptedQty)){
                            viewModel.InspectMaterial(
                                loadingDialog,
                                poHeaderId = poDetailsItem2.poHeaderId!!,
                                poLineId = poDetailsItem2.poLineId!!,
                                receiptNo = poDetailsItem2.receiptno!!,
                                shipToOrganizationId = poDetailsItem2.shipToOrganizationId!!,
                                acceptedQty= acceptedQty.toInt(),
                                transactionDate =selectedDate!!,
                                this
                            )
                        } else
                            binding.acceptedQty.error = getString(R.string.please_enter_valid_accepted_qty)
                    } else {
                        binding.acceptedQty.error = getString(R.string.please_enter_accepted_qty)
                    }
                } else {
                    binding.itemCode.error = getString(R.string.please_scan_item_code)
                }
            }
            R.id.po_details -> v.findNavController().navigate(R.id.action_startInspectionFragment_to_PODetailsFragment)
        }
    }

    override fun onBarcodeEvent(p0: BarcodeReadEvent?) {
        requireActivity().runOnUiThread {
            val itemCode = poDetailsItem2.itemcode
            if (barcodeReader.scannedData(p0!!) == itemCode){
                binding.itemCode.editText?.setText(itemCode)
                binding.itemCode.error = getString(R.string.wrong_item_code)
            } else {
                binding.itemCode.editText?.setText("")
                binding.itemCode.error = null
            }
        }
    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {}

    override fun onTriggerEvent(p0: TriggerStateChangeEvent?) {
        barcodeReader.onTrigger(p0!!)
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