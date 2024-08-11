package com.example.slipsync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slipsync.Adapter.CreatedSlipsAdpater
import com.example.slipsync.Adapter.UserViewAdapter
import com.example.slipsync.databinding.ActivityUserViewSlipsBinding
import com.example.slipsync.databinding.FragmentSlipsBinding
import com.example.slipsync.model.SlipDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserViewSlipsActivity : AppCompatActivity() {
    private var listOfSlipItem: ArrayList<SlipDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private  var userName1:String?=null
    private lateinit var createdSlipAdapter:UserViewAdapter
    private val binding: ActivityUserViewSlipsBinding by lazy{
        ActivityUserViewSlipsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val database = FirebaseDatabase.getInstance().getReference("admin")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userId?.let {
            database.child(it).child("name").get().addOnSuccessListener { dataSnapshot ->
                userName1 = dataSnapshot.getValue(String::class.java)
                Log.d("UserName", "Name: $userName1")
            }.addOnFailureListener { exception ->
                Log.e("UserName", "Error: ${exception.message}")
            }
        }


        retrieveSlips()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun retrieveSlips() {
        val slipRef = database.reference.child("admin").child("CreatedSlips")
        val sortingQuery = slipRef.orderByChild("date")

        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val slipItems = buySnapshot.getValue(SlipDetails::class.java)
                    slipItems?.let {
                        // Normalize the strings by converting to lowercase and removing spaces
                        val normalizedSlipName = it.slipName?.lowercase()?.replace("\\s".toRegex(), "")
                        val normalizedUserName = userName1?.lowercase()?.replace("\\s".toRegex(), "")

                        // Check if the normalized names match
                        if (normalizedSlipName == normalizedUserName) {
                            listOfSlipItem.add(it)
                        }
                    }
                }
                listOfSlipItem.reverse()
                if (listOfSlipItem.isNotEmpty()) {
                    // Setup the RecyclerView
                    setRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
                Log.e("RetrieveSlips", "Error: ${error.message}")
            }
        })
    }


    private fun setRecyclerView() {
        val rv = binding.yourSlipsRecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        createdSlipAdapter = UserViewAdapter(listOfSlipItem,this)
        rv.adapter= createdSlipAdapter
    }
}