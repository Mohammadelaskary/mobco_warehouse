package com.example.mobco.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobco.Model.PODetailsItem2
import com.example.mobco.databinding.PoItemInspectionBinding


class PurchaseOrdersInspectionAdapter (private val poInspectionActionsButtonsClicked:POInspectionItemClick): RecyclerView.Adapter<PurchaseOrdersInspectionAdapter.PurchaseOrderInspectionViewHolder>() {

    var poList: Array<PODetailsItem2> = arrayOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class PurchaseOrderInspectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PoItemInspectionBinding.bind(view)
    }



    override fun onBindViewHolder(holder: PurchaseOrderInspectionViewHolder, position: Int) {
        val currentPosition = holder.adapterPosition
        val poDetailsItem = poList[currentPosition]

        with(holder) {
            if (!poDetailsItem.isinspected.toBoolean())
                itemView.visibility = VISIBLE
            else
                itemView.visibility = GONE
            binding.poNumber.text = poDetailsItem.pono.toString()
            binding.vendor.text = poDetailsItem.supplier
            binding.poDate.text = poDetailsItem.receiptdate.toString().substring(0,10)
            binding.receiptNumber.text = poDetailsItem.receiptno.toString()
            binding.itemDescription.text = poDetailsItem.itemdesc.toString()
            binding.receivedQty.text = poDetailsItem.itemqtyreceived.toString()

            itemView.setOnClickListener {
                poInspectionActionsButtonsClicked.inspectionClicked(poDetailsItem)
            }


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseOrderInspectionViewHolder {
        val binding =
            PoItemInspectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PurchaseOrderInspectionViewHolder(binding.root)
    }


    override fun getItemCount(): Int {
        return poList.size
    }

    interface POInspectionItemClick {
        fun inspectionClicked(poDetailsItem2: PODetailsItem2)
    }
}