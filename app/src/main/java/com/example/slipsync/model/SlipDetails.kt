package com.example.slipsync.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class SlipDetails() : Serializable {
     var date: String? = null
    var slipAmount: String? = null
    var slipItem:String?=null
    var slipName: String? = null
    var slipNumber: String? = null
    var slipQuantity: String? = null
    var vehicleNo: String? = null

    constructor(parcel: Parcel) : this() {
        date = parcel.readString()
        slipAmount = parcel.readString()
        slipItem = parcel.readString()
        slipName = parcel.readString()
        slipNumber = parcel.readString()
        slipQuantity = parcel.readString()
        vehicleNo = parcel.readString()
    }
     fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(slipAmount)
        parcel.writeString(slipItem)
        parcel.writeString(slipName)
        parcel.writeString(slipNumber)
        parcel.writeString(slipQuantity)
        parcel.writeString(vehicleNo)
    }

     fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SlipDetails> {
        override fun createFromParcel(parcel: Parcel): SlipDetails {
            return SlipDetails(parcel)
        }

        override fun newArray(size: Int): Array<SlipDetails?> {
            return arrayOfNulls(size)
        }
    }
}