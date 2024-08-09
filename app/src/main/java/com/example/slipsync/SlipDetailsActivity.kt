package com.example.slipsync

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.slipsync.databinding.ActivitySlipDetailsBinding
import com.example.slipsync.model.SlipDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SlipDetailsActivity : AppCompatActivity() {

    ///Slip item Details
    private  var slipNumber: String?= null
    private  var slipName: String?=null
    private  var slipDate: String?=null
    private  var vehicleNo: String?=null
    private  var slipItem: String?=null
    private  var slipQuantity: String?=null
    private  var slipAmount: String?=null

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivitySlipDetailsBinding by lazy {
        ActivitySlipDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        //Initialization of Firebase
        auth = FirebaseAuth.getInstance()

        //Initialization of Firebase Database Instance
        database = FirebaseDatabase.getInstance()


        slipNumber = intent.getStringExtra("SlipNumber")
        slipDate = intent.getStringExtra("SlipDate")
        slipName = intent.getStringExtra("SlipName")
        vehicleNo = intent.getStringExtra("SlipVehicleNo")
        slipItem = intent.getStringExtra("SlipItem")
        slipQuantity = intent.getStringExtra("SlipQuantity")
        slipAmount = intent.getStringExtra("SlipAmount")

        with(binding){
            numberText.text = slipNumber
            dateEditText.text = slipDate
            NameEditText.text = slipName
            VehicleEditText.text = vehicleNo
            ItemEditText.text = slipItem
            quantityEditText.text = slipQuantity
            amountEditText.text = slipAmount
        }

        binding.recenOrderBackButton.setOnClickListener {
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}