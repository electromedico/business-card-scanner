package fr.alteca.monalteca.qrutils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix

import com.google.zxing.qrcode.QRCodeWriter


class QrUtils {
    companion object {

        fun qrCodeGenerator(text : String): Bitmap{

            val qrCodeWriter = QRCodeWriter ()

            val bitMatrix : BitMatrix= qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,200,200)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
            for (x in 0 until width){

                for (y in 0 until height){

                    val color = when(bitMatrix.get(x,y)){
                        true ->  Color.BLACK

                        false-> Color.WHITE
                    }
                    bmp.setPixel(x,y,color)
                }

            }
            return bmp
        }

    }
}