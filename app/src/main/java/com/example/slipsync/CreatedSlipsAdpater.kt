package com.example.slipsync

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slipsync.databinding.SlipsItemBinding

class CreatedSlipsAdpater(
    private val slipItemName:MutableList<String>,
    private val slipName:MutableList<String>,
    private val slipItemAmount:MutableList<String>,
    private var requirecontext: Context
):RecyclerView.Adapter<CreatedSlipsAdpater.CreatedSlipViewHolder>() {


    override fun onBindViewHolder(holder: CreatedSlipViewHolder, position: Int) {
        holder.bind(
            slipItemName[position],
            slipName[position],
            slipItemAmount[position]
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatedSlipViewHolder {
        return CreatedSlipViewHolder(SlipsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = slipItemName.size

    inner class CreatedSlipViewHolder(private val binding: SlipsItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(s: String, s1: String, s2: String) {
            binding.itemName.text = s
            binding.slipName.text = s1
            binding.amountText.text = s2
        }

    }
}