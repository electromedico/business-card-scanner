package fr.alteca.monalteca

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_modify.*

class ModifyActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)
        val mySharedPreferences =getSharedPreferences(getString(R.string.vcard), Context.MODE_PRIVATE)
        name_tv.text = mySharedPreferences.getString(getString(R.string.vcard_name),"")
        function_tv.text = mySharedPreferences.getString(getString(R.string.vcard_function),"")
        tel_fix_tv.text =  mySharedPreferences.getString(getString(R.string.vcard_tel_fix),"")
        tel_mobile_tv.text = mySharedPreferences.getString(getString(R.string.vcard_tel_mobile),"")
        adr_tv.text = mySharedPreferences.getString(getString(R.string.vcard_adr),"")
        email_tv.text = mySharedPreferences.getString(getString(R.string.vcard_email),"")

        setListeners()
    }

    private fun setListeners() {
        edit_name_iv.setOnClickListener {
            createDialogue(getString(R.string.full_name),name_tv.text.toString(),InputName.NAME)
        }

        edit_adr_iv.setOnClickListener {
            createDialogue(getString(R.string.adr),adr_tv.text.toString(),InputName.ADR)
        }

        edit_email_iv.setOnClickListener{
            createDialogue(getString(R.string.email),email_tv.text.toString(),InputName.EMAIL)
        }

        edit_tel_fix_iv.setOnClickListener {
            createDialogue(getString(R.string.tel_fix),tel_fix_tv.text.toString(),InputName.FIX)
        }

        edit_tel_mobile_iv.setOnClickListener {
            createDialogue(getString(R.string.mobile),tel_mobile_tv.text.toString(),InputName.MOBILE)
        }

        edit_fucntion_iv.setOnClickListener {
            createDialogue(getString(R.string.function),function_tv.text.toString(),InputName.FUNCTION)
        }
    }

    override fun onBackPressed() {
        updateSharedPreferences()
        super.onBackPressed()
    }

    fun createDialogue(title:String, value:String, inputType: InputName) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        val view = layoutInflater.inflate(R.layout.input_dialog, null)
        val editText = view.findViewById<EditText>(R.id.input)
        editText.setText(value)
        builder.setView(view)

        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
            updateView(editText.text.toString(),inputType)
        })

        builder.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which ->

        })

        val dialog =builder.create()
        dialog.show()

    }

    fun updateView(text: String,inputType: InputName) {

        when(inputType){
            InputName.EMAIL-> email_tv.text = text
            InputName.ADR-> adr_tv.text=text
            InputName.FIX->tel_fix_tv.text=text
            InputName.FUNCTION->function_tv.text=text
            InputName.MOBILE->tel_mobile_tv.text=text
            InputName.NAME->name_tv.text=text
        }
    }

    fun updateSharedPreferences(){

        val sharedPreferencesEditor = getSharedPreferences(getString(R.string.vcard), Context.MODE_PRIVATE).edit()
        sharedPreferencesEditor.putString(getString(R.string.vcard_name),name_tv.text.toString())
        sharedPreferencesEditor.putString(getString(R.string.vcard_email),email_tv.text.toString())
        sharedPreferencesEditor.putString(getString(R.string.vcard_adr),adr_tv.text.toString())
        sharedPreferencesEditor.putString(getString(R.string.vcard_function),function_tv.text.toString())
        sharedPreferencesEditor.putString(getString(R.string.vcard_tel_mobile),tel_mobile_tv.text.toString())
        sharedPreferencesEditor.putString(getString(R.string.vcard_tel_fix),tel_fix_tv.text.toString())
        sharedPreferencesEditor.apply()
    }

    enum class InputName {
        NAME,
        FUNCTION,
        FIX,
        MOBILE,
        ADR,
        EMAIL
    }
}
