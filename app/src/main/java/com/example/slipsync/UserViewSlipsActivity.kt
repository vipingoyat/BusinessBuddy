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
    private lateinit var userSlipAdapter: CreatedSlipsAdpater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
//        currentUserName()
        retrieveSlips()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


//    private fun currentUserName() {
//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//        val database = FirebaseDatabase.getInstance().reference
//
//        database.child("admin").child(userId).addListenerForSingleValueEvent(object :
//            ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Assuming the user data is stored as a map
//                    val userData = dataSnapshot.getValue(Map::class.java) as Map<String, Any>?
//                    val userName = userData?.get("name") as String?
//                    userName?.let {
//                        // Do something with the user name
//                        userName1 = userName
//                        Log.d("User Name", it)
//                    } ?: run {
//                        Log.d("User Name", "User name not found.")
//                    }
//                } else {
//                    Log.d("Data", "User not found.")
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("Database Error", databaseError.message)
//            }
//        })
//        retrieveSlips()
//    }

    private fun retrieveSlips() {
        val slipRef = database.reference.child("admin").child("CreatedSlips")
        val sortingQuery = slipRef.orderByChild("date")
        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
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
                    setRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
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