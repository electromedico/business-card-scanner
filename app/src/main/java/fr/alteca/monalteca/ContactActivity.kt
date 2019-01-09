package fr.alteca.monalteca

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import fr.alteca.monalteca.model.utils.VCardUtils
import fr.alteca.monalteca.textrazor.conectivity.TextRazorOnTaskCompleted
import fr.alteca.monalteca.textrazor.model.TextRazorResponse

class ContactActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
    }

    private fun getContactInfoFromCodebar(contactsInfo: FirebaseVisionBarcode.ContactInfo){
        val vCard= VCardUtils.vCardFromContactInfo(contactsInfo)
        Log.i("getinfo",vCard.toString())

    }

}
