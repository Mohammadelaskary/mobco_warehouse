package com.example.mobco.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mobco.Model.PODetailsItem
import com.example.mobco.databinding.ItemLayoutBinding
import com.example.mobco.databinding.ReceivedPoLineLayoutBinding

class ReceivedPOItemAdapter(private val linesList:List<PODetailsItem>,private val onPOLineItemRemoved: OnPOLineItemRemoved) :
    RecyclerView.Adapter<ReceivedPOItemAdapter.ReceivedPOItemViewHolder>() {
    inner class ReceivedPOItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val binding = ReceivedPoLineLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedPOItemViewHolder {
        val binding = ReceivedPoLineLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReceivedPOItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return linesList.size
    }
    var receivedQty = ""
    override fun onBindViewHolder(holder: ReceivedPOItemViewHolder, position: Int) {
        val poDetailsItem = linesList[position]
        with(holder){
            binding.itemCode.text = poDetailsItem.itemCode
            binding.itemDescription.text= poDetailsItem.itemDesc
            val receivedPerTotalPOQty = "${poDetailsItem.itemQtyReceived} / ${poDetailsItem.itemQty}"
            binding.receivedQtyPerTotalQty.text = receivedPerTotalPOQty
            binding.currentReceivedQty.text = receivedQty
            binding.remove.setOnClickListener { onPOLineItemRemoved.onPOLineItemRemoved(position) }
        }
    }

    interface OnPOLineItemRemoved {
        fun onPOLineItemRemoved(position: Int)
    }
}