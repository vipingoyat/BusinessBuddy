package com.example.slipsync

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.slipsync.databinding.ActivitySlipDetailsBinding
import com.example.slipsync.model.SlipDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream

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

        binding.createSlipButton.setOnClickListener {
            val bitmap = captureViewAsBitmap(binding.pdfDownload)
            saveBitmapAsPDF(bitmap, "$slipName")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun captureViewAsBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun saveBitmapAsPDF(bitmap: Bitmap, fileName: String) {
        // Create a directory to save the PDF
        val pdfDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyApp")
        if (!pdfDir.exists()) {
            pdfDir.mkdir()
        }

        // Create the PDF file
        val pdfFile = File(pdfDir, "$fileName.pdf")

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        // Draw the bitmap onto the page
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        // Write the document content
        document.writeTo(FileOutputStream(pdfFile))
        document.close()

        Toast.makeText(this, "PDF saved to ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()
    }

}