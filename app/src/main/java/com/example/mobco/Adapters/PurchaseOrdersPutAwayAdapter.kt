package com.example.mobco.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobco.Model.PODetailsItem2
import com.example.mobco.Model.PurchaseOrder
import com.example.mobco.databinding.PoItemPutAwayBinding
import com.example.mobco.databinding.PoItemLayoutBinding

class PurchaseOrdersPutAwayAdapter (private val putAwayItemClicked:PutAwayItemClick): RecyclerView.Adapter<PurchaseOrdersPutAwayAdapter.PurchaseOrderPutAwayViewHolder>() {
    var poList: Array<PODetailsItem2> = arrayOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PurchaseOrderPutAwayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PoItemPutAwayBinding.bind(view)
    }



    override fun onBindViewHolder(holder: PurchaseOrderPutAwayViewHolder, position: Int) {
        val currentPosition = holder.adapterPosition
        val poDetailsItem = poList[currentPosition]
        with(holder) {
            if (poDetailsItem.isinspected.toBoolean())
                itemView.visibility = View.VISIBLE
            else
                itemView.visibility = View.GONE
            binding.poNumber.text = poDetailsItem.pono.toString()
            binding.vendor.text = poDetailsItem.supplier
            binding.poDate.text = poDetailsItem.receiptdate.toString().substring(0, 10)
            binding.receiptNumber.text = poDetailsItem.receiptno.toString()
            binding.itemDescription.text = poDetailsItem.itemdesc.toString()
            binding.receivedQty.text = poDetailsItem.itemqtyreceived.toString()

            itemView.setOnClickListener {
                putAwayItemClicked.putAwayItemClicked(poDetailsItem)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseOrderPutAwayViewHolder {
        val binding =
            PoItemPutAwayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PurchaseOrderPutAwayViewHolder(binding.root)
    }


    override fun getItemCount(): Int {
        return poList.size
    }

    interface PutAwayItemClick {
        fun putAwayItemClicked(poDetailsItem2: PODetailsItem2)
    }
}