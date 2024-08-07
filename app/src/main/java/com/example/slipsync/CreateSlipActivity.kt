package com.example.slipsync
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.slipsync.databinding.ActivityCreateSlipBinding
import com.example.slipsync.model.CreatedSlipData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Locale

class CreateSlipActivity : AppCompatActivity() {
    private val binding:ActivityCreateSlipBinding by lazy {
        ActivityCreateSlipBinding.inflate(layoutInflater)
    }

    ///Slip item Details
    private lateinit var slipName:String
    private lateinit var slipDate:String
    private lateinit var vehicleNo:String
    private lateinit var slipItem:String
    private  lateinit var slipQuantity:String
    private lateinit var slipAmount:String

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }


        val myCalendar = Calendar.getInstance()
        updateLable(myCalendar)

        val datePicker = DatePickerDialog.OnDateSetListener{
            view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        binding.dateEditText.setOnClickListener {
            DatePickerDialog(this,datePicker,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }



        //Initialization of Firebase
        auth = FirebaseAuth.getInstance()

        //Initialization of Firebase Database Instance
        database = FirebaseDatabase.getInstance()

        binding.createSlipButton.setOnClickListener {
            //Get Data from Field
            slipName = binding.NameEditText.text.toString().trim()
            slipDate = binding.dateEditText.text.toString().trim()
            vehicleNo = binding.VehicleEditText.text.toString().trim()
            slipItem = binding.ItemEditText.text.toString().trim()
            slipQuantity = binding.quantityEditText.text.toString().trim()
            slipAmount = binding.amountEditText.text.toString().trim()

            if(!(slipName.isBlank()||vehicleNo.isBlank()||slipItem.isBlank()||slipQuantity.isBlank()||slipAmount.isBlank()||slipDate.isBlank())){
                uploadData()
                Toast.makeText(this,"Slip is Created Successfully",Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this,"Fill All The Details",Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun uploadData() {
        //Get Reference to the database
        val slipRef = database.getReference("createdSlips")

        //Generate the Unique key for the slip generated
        val newSlipKey = slipRef.push().key

        val newItem = CreatedSlipData(
            newSlipKey,
            slipName = slipName,
            date = slipDate,
            vehicleNo = vehicleNo,
            slipItem = slipItem,
            slipQuantity = slipQuantity,
            slipAmount = slipAmount
            )

        newSlipKey?.let {
                key->
            slipRef.child(key).setValue(newItem).addOnSuccessListener {
                Toast.makeText(this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this,"Data Failed to Upload",Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun updateLable(myCalendar: Calendar?) {
       val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        if (myCalendar != null) {
            binding.dateEditText.text = sdf.format(myCalendar.time)
        }
    }
}