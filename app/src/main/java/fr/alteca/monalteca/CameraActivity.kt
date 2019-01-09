package fr.alteca.monalteca


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.graphics.*
import android.media.Image
import android.media.ImageReader
import android.os.Parcelable
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import fr.alteca.monalteca.camera.Camera2Object
import fr.alteca.monalteca.camera.utils.Camera2Utils
import fr.alteca.monalteca.imageutils.ImageUtils
import fr.alteca.monalteca.model.VCard
import fr.alteca.monalteca.model.utils.VCardUtils
import fr.alteca.monalteca.textrazor.conectivity.PostController
import fr.alteca.monalteca.textrazor.conectivity.TextRazorOnTaskCompleted
import fr.alteca.monalteca.textrazor.model.TextRazorResponse
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity(),ImageReader.OnImageAvailableListener, TextRazorOnTaskCompleted {

    private var isScaningDone=true

    /**Flag pour l'inversion de colors*/
    private var isColorInverted=false

    /**Camera permission key**/
    private val REQUEST_CAMERA_PERMISSION : Int = 100

    /**Firebase Barcode Detector**/
    private lateinit var barcodeDetector:FirebaseVisionBarcodeDetector

    /**Firebase Image Detector**/
    private lateinit var textDetector: FirebaseVisionTextRecognizer

    /** Flag for takePhotoFAB*/
    private var isTakePhotoFABClicked: Boolean=false

    /**Camera2 api implementation**/
    private lateinit var camera2Object: Camera2Object

    /**ArrayList with all the textLines from the scanned card*/
    private lateinit var mTextLineList:ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        FirebaseApp.initializeApp(this)
        checkPermission()

        camera2Object=Camera2Object(this,this,cameraSurfaceView)
        barcodeDetector= FirebaseVision.getInstance().visionBarcodeDetector
        textDetector= FirebaseVision.getInstance().onDeviceTextRecognizer

        takePhotoFAB.setOnClickListener {
            isTakePhotoFABClicked=true
        }
    }

    override fun onResume() {
        super.onResume()
        camera2Object.startCamera()
    }

    override fun onPause() {
        camera2Object.stopCamera()
        super.onPause()

    }

    private fun checkHardware() : Boolean{
        val packageManager= this.packageManager
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun checkPermission(){
        val hasHardware=checkHardware()
        if (hasHardware){
            if(hasCameraPermission() && hasWriteExternalPermission()){
                //takePicture()
            }else{
                askForPermission()
            }
        }
    }

    private fun askForPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.CAMERA)
            || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CAMERA_PERMISSION )

        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_CAMERA_PERMISSION  )

        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
    }
    private fun hasWriteExternalPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CAMERA_PERMISSION ->{
                for (result in grantResults){
                    if(result==PackageManager.PERMISSION_GRANTED){
                    }else{
                        askForPermission()
                        Log.d(this.componentName.toShortString(),"permision denied")
                    }
                }

            }
        }
    }

    private fun scanCode(image: Image){

        if(isScaningDone){

            isScaningDone=false

            val fireBaseImage = if (isColorInverted){
                FirebaseVisionImage.fromBitmap(invertcolors(image))
            }else{
                FirebaseVisionImage.fromMediaImage(image,Camera2Utils.getRotationCompensation(camera2Object.cameraId,this,this))
            }
            barcodeDetector.detectInImage(fireBaseImage)
                .addOnSuccessListener { barcodes ->
                    Log.i("detector onsucces", "size: "+barcodes.size.toString())

                    if (barcodes.size>0){

                        var vCardArray=ArrayList<VCard>()

                        for (barcode in barcodes){
                            if(barcode.valueType == FirebaseVisionBarcode.TYPE_CONTACT_INFO){
                                val contactInfo=barcode.contactInfo
                                if (contactInfo != null) {
                                    vCardArray.add(VCardUtils.vCardFromContactInfo(contactInfo))
                                }
                            }
                        }
                        if (vCardArray.size>0){
                            goToContactActivity(vCardArray)
                        }
                    }

                    isColorInverted=!isColorInverted
                    isScaningDone=true

                }
                .addOnFailureListener {
                    Log.i("dectector onfailed","failed")
                    isScaningDone=true
                }
        }
    }

    private fun scanForText(image: Image) {
        if (isScaningDone){

            isScaningDone=false
            val fireBaseImage=FirebaseVisionImage.fromMediaImage(image,Camera2Utils.getRotationCompensation(camera2Object.cameraId,this,this))

            textDetector.processImage(fireBaseImage)
                .addOnSuccessListener { firebaseVisionText ->
                    if(firebaseVisionText!=null && firebaseVisionText.text!=null){
                        mTextLineList=createTextLineList(firebaseVisionText)
                        getEntities(firebaseVisionText)
                    }
                }
                .addOnFailureListener{
                    isScaningDone=true
                }
        }

    }

    private fun getEntities(firebaseVisionText: FirebaseVisionText) {
        val postController= PostController(this,this)
        postController.startNerService(firebaseVisionText.text)

    }


    private fun invertcolors(image: Image): Bitmap {
        //On recupere le plane B&W( noir et blanc)
        val bitmap=ImageUtils.imageToBitmap(image,this)
        return ImageUtils.invertColorsFromBitmap(bitmap, this)

    }



    private fun goToContactActivity(vCardArray: ArrayList<VCard>) {
        val intent=Intent(this,ContactActivity::class.java)
        intent.putExtra(getString(R.string.gson_info_tag),vCardArray)

        startActivity(intent)
    }

    override fun onImageAvailable(reader: ImageReader?) {
        if (reader != null) {
            val image = reader.acquireLatestImage()
            if (image != null) {

                if (isTakePhotoFABClicked){
                    scanForText(image)
                }else{
                    scanCode(image)
                }
                image.close()
            }
        }
    }

    override fun onTaskCompletedGet(textRazorResponse: TextRazorResponse) {
        createVCardFromScannedImage(textRazorResponse)
    }
    /**Methode pour generer un VCard a partir de l'image et le NER WebService
     * @param textRazorResponse
     */
    private fun createVCardFromScannedImage(textRazorResponse: TextRazorResponse){
        val vCard=VCard()
        for(entity in textRazorResponse.response.entities){
            VCardUtils.setVCardProperty(vCard,entity)
        }
    }
    private fun createTextLineList(firebaseVisionText:FirebaseVisionText):ArrayList<String>{
        val array= ArrayList<String>()
        for (block in firebaseVisionText.textBlocks){
            for (textLine in block.lines){
                array.add(textLine.text)
            }
        }
        return array
    }
    fun addContactIntent(){
        ContactsContract.Intents.Insert.NAME
        ContactsContract.Intents.Insert.COMPANY
        ContactsContract.Intents.Insert.EMAIL
        ContactsContract.Intents.Insert.EMAIL_TYPE
        ContactsContract.Intents.Insert.PHONE
        ContactsContract.Intents.Insert.PHONE_TYPE

    }

}
