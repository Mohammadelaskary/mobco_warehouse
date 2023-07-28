package com.example.mobco.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobco.Model.PurchaseOrder
import com.example.mobco.databinding.PoItemLayoutBinding

class PurchaseOrdersAdapter(private val poActionsButtonsClicked:POActionButtonsClick): RecyclerView.Adapter<PurchaseOrdersAdapter.PurchaseOrderViewHolder>() {
    var poList: List<PurchaseOrder> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class PurchaseOrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PoItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseOrderViewHolder {
        val binding = PoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PurchaseOrderViewHolder(binding.root)
    }
    private var selectedPosition = -1
    override fun onBindViewHolder(holder: PurchaseOrderViewHolder, position: Int) {
        val currentPosition = holder.adapterPosition
        val purchaseOrder = poList[currentPosition]
        with(holder) {
//            binding.poNumber.text = purchaseOrder.ponumber!!.toString()
//            binding.vendor.text = purchaseOrder.vendorName!!
//            binding.operatingUnit.text = purchaseOrder.operatingUnitDesc!!
//            binding.poDate.text = purchaseOrder.podate!!.substring(0,10)
//            binding.poType.text = purchaseOrder.orderType!!
            binding.actionButtons.visibility = if (selectedPosition==currentPosition)
                 View.VISIBLE
             else
                View.GONE

            itemView.setOnClickListener {
                selectedPosition = currentPosition
                notifyDataSetChanged()
            }
            binding.startReceive.setOnClickListener {
                poActionsButtonsClicked.startReceivingClicked(purchaseOrder)
            }
            binding.poDetails.setOnClickListener {
                poActionsButtonsClicked.poDetailsClicked(purchaseOrder)
            }
        }
    }

    override fun getItemCount(): Int = poList.size

    interface POActionButtonsClick {
        fun startReceivingClicked (purchaseOrder: PurchaseOrder)
        fun poDetailsClicked (purchaseOrder: PurchaseOrder)
    }
}