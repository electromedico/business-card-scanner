package fr.alteca.monalteca.model

import android.os.Parcel
import android.os.Parcelable

class VCard() : Parcelable {

    var fullName:String=""
    var org: String = ""
    var title: String = ""
    var tels: Array<Tel> = emptyArray()
    var adrs: Array<Adr> = emptyArray()
    var emails: Array<Email> = emptyArray()

    constructor(parcel: Parcel) : this() {
        fullName = parcel.readString()?:""
        org = parcel.readString()?:""
        title = parcel.readString()?:""
        tels = parcel.createTypedArray(Tel)?: emptyArray()
        adrs = parcel.createTypedArray(Adr)?:emptyArray()
        emails = parcel.createTypedArray(Email)?:emptyArray()
    }


    class Tel(val telType:String,val Telnumber:String):Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString()?:"",
            parcel.readString()?:""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(telType)
            parcel.writeString(Telnumber)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Tel> {
            override fun createFromParcel(parcel: Parcel): Tel {
                return Tel(parcel)
            }

            override fun newArray(size: Int): Array<Tel?> {
                return arrayOfNulls(size)
            }
        }
    }


    class Adr(val Adrtype:String,val adr:String):Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString()?:"",
            parcel.readString()?:""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(Adrtype)
            parcel.writeString(adr)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Adr> {
            override fun createFromParcel(parcel: Parcel): Adr {
                return Adr(parcel)
            }

            override fun newArray(size: Int): Array<Adr?> {
                return arrayOfNulls(size)
            }
        }
    }

    class Email(val emailType:String,val email: String):Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString()?:"",
            parcel.readString()?:""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(emailType)
            parcel.writeString(email)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Email> {
            override fun createFromParcel(parcel: Parcel): Email {
                return Email(parcel)
            }

            override fun newArray(size: Int): Array<Email?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeString(org)
        parcel.writeString(title)
        parcel.writeTypedArray(tels, flags)
        parcel.writeTypedArray(adrs, flags)
        parcel.writeTypedArray(emails, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VCard> {
        override fun createFromParcel(parcel: Parcel): VCard {
            return VCard(parcel)
        }

        override fun newArray(size: Int): Array<VCard?> {
            return arrayOfNulls(size)
        }
    }

}


