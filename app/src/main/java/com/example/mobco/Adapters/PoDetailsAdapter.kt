package com.example.mobco.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobco.Model.PODetailsItem
import com.example.mobco.databinding.ItemLayoutBinding

class PoDetailsAdapter(var onPOLineClicked:OnPOLineClicked) : RecyclerView.Adapter<PoDetailsAdapter.PoDetailsViewHolder>() {
    var poList: List<PODetailsItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class PoDetailsViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoDetailsViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PoDetailsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PoDetailsViewHolder, position: Int) {
        val item = poList[position]
        with(holder){
            binding.itemCode.text = item.itemCode
            binding.itemDescription.text = item.itemDesc
            val receivedQtyPerTotalQty = "${item.itemQtyReceived} / ${item.itemQty}"
            binding.receivedQtyPerTotalQty.text = receivedQtyPerTotalQty
            itemView.setOnClickListener {onPOLineClicked.onPOLineClicked(item) }
        }
    }

    override fun getItemCount(): Int {
        return poList.size
    }

    interface OnPOLineClicked{
        fun onPOLineClicked(po:PODetailsItem)
    }
}