package com.example.slipsync.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slipsync.Adapter.CreatedSlipsAdpater
import com.example.slipsync.databinding.FragmentSlipsBinding
import com.example.slipsync.model.SlipDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SlipsFragment : Fragment() {

    private var listOfSlipItem: ArrayList<SlipDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSlipsBinding
    private lateinit var createdSlipAdapter: CreatedSlipsAdpater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        retrieveSlipHistory()
        binding = FragmentSlipsBinding.inflate(inflater,container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    companion object {
    }


    private fun retrieveSlipHistory() {
        val slipRef = database.reference.child("admin").child("CreatedSlips")
        val shortingQuery = slipRef.orderByChild("slipNumber")
        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val slipItems = buySnapshot.getValue(SlipDetails::class.java)
                    slipItems?.let {
                        listOfSlipItem.add(it)
                    }
                }
                listOfSlipItem.reverse()
                if (listOfSlipItem.isNotEmpty()) {
                    //setup the recycler view
                    setPreviousBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun setPreviousBuyItemRecyclerView(){
            val rv = binding.slipsRecyclerView
            rv.layoutManager = LinearLayoutManager(requireContext())
            createdSlipAdapter = CreatedSlipsAdpater(listOfSlipItem,requireContext())
            rv.adapter= createdSlipAdapter

    }
}