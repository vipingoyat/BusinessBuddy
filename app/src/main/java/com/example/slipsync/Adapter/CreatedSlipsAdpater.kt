package com.example.slipsync.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slipsync.SlipDetailsActivity
import com.example.slipsync.databinding.SlipsItemBinding
import com.example.slipsync.model.SlipDetails

class CreatedSlipsAdpater(
    private val slipItems: List<SlipDetails>,
    private val requireContext: Context
) : RecyclerView.Adapter<CreatedSlipsAdpater.CreatedSlipViewHolder>() {


    override fun onBindViewHolder(holder: CreatedSlipViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatedSlipViewHolder {
        return CreatedSlipViewHolder(
            SlipsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = slipItems.size

    inner class CreatedSlipViewHolder(private val binding: SlipsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if(position!=RecyclerView.NO_POSITION){
                        openSlipDetailsActivity(position)
                    }
                }
            }

        private fun openSlipDetailsActivity(position: Int) {
            val slipItem = slipItems[position]

            //Intent to open detail activity and pass the data
            val intent = Intent(requireContext,SlipDetailsActivity::class.java).apply {
                putExtra("SlipNumber",slipItem.slipNumber)
                putExtra("SlipDate",slipItem.date)
                putExtra("SlipName",slipItem.slipName)
                putExtra("SlipVehicleNo",slipItem.vehicleNo)
                putExtra("SlipItem",slipItem.slipItem)
                putExtra("SlipQuantity",slipItem.slipQuantity)
                putExtra("SlipAmount",slipItem.slipAmount)
            }

            ///Start the Details Activity
            requireContext.startActivity(intent)
        }

        fun bind(position: Int) {
            val slipItem = slipItems[position]
            binding.apply {
                slipName.text = slipItem.slipName
                dateText.text = slipItem.date
                amountText.text = slipItem.slipAmount
            }
        }

    }
}