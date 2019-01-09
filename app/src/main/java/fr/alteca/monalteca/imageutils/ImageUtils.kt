package fr.alteca.monalteca.imageutils

import android.content.Context
import android.graphics.*
import android.media.Image
import android.renderscript.*


class ImageUtils{

    companion object {
        private val TRANSFORMATION_MATRIX=Matrix4f(
            floatArrayOf(-0.33f, -0.33f, -0.33f, 1.0f,
                -0.59f, -0.59f, -0.59f, 1.0f,
                -0.11f, -0.11f, -0.11f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f)
        )

        /***
         * Transforms a image into a Bitmap via RenderScript
         * @param image source image
         * @param context Activity Context
         * @return RGB Bitmap
         */
        fun  imageToBitmap(image:Image, context: Context) :Bitmap{
            val nv21 = yuv_420_888toNV21ByteArray(image)
            return yuvToRgb(nv21,image.width,image.height,context)
        }

        /***
         * This method transforms a image YUB in a ByteArray in NV21 format
         * @param image source image in YUV_4200_888 to transform
         * @return ByteArray from the image in NV21
          */
        private fun yuv_420_888toNV21ByteArray(image: Image):ByteArray{

            val yByteBuffer=image.planes[0].buffer
            val uByteBuffer=image.planes[1].buffer
            val vByteBuffer=image.planes[2].buffer

            val ySize = yByteBuffer.remaining()
            val uSize = uByteBuffer.remaining()
            val vSize = vByteBuffer.remaining()

            var nv21 = ByteArray(ySize + uSize + vSize)

            //U and V are swapped
            yByteBuffer.get(nv21, 0, ySize)
            uByteBuffer.get(nv21, ySize, uSize)
            vByteBuffer.get(nv21, ySize + vSize, vSize)

            return nv21
        }

        /***
         * this method allows the conversion from an NV21 image format to RGB bitmap via renderscript
         * @param byteArrayNV21 ByteArray for the image in NV21 format
         * @param width image width
         * @param height image height
         * @param context Activity Context
         * @return Bitmap RGB Bitmap
         */
        private fun yuvToRgb(byteArrayNV21: ByteArray,width: Int,height: Int, context: Context):Bitmap{

            val rs=RenderScript.create(context)
            val scriptIntrinsicYuvToRGB = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))

            val yubTypeBuilder = Type.Builder(rs, Element.YUV(rs))
            yubTypeBuilder.setX(width)
            yubTypeBuilder.setY(height)
            yubTypeBuilder.setYuvFormat(ImageFormat.NV21)

            val inAllocation=Allocation.createTyped(rs,yubTypeBuilder.create(),Allocation.USAGE_SCRIPT)
            inAllocation.copyFrom(byteArrayNV21)

            val rgbType=Type.createXY(rs, Element.RGBA_8888(rs),width,height)
            val outAllocation=Allocation.createTyped(rs,rgbType,Allocation.USAGE_SCRIPT)

            scriptIntrinsicYuvToRGB.setInput(inAllocation)
            scriptIntrinsicYuvToRGB.forEach(outAllocation)
            val bmpout = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            outAllocation.copyTo(bmpout)
            rs.destroy()
            return bmpout
        }

        /***
         *This method allow to invert the colors of the bitmap via
         * ScriptIntrinsicColorMatrix
         * @param bitmap Source to invert
         * @param context Activity Context
         * @return Bitmap with inverted colors
         */
        fun invertColorsFromBitmap(bitmap: Bitmap, context: Context): Bitmap {
            val result = bitmap.copy(bitmap.config, true)
            val rs=RenderScript.create(context)
            val inverter= ScriptIntrinsicColorMatrix.create(rs)

            val inputAllocation=Allocation.createFromBitmap(rs,bitmap)
            val outputAllocation=Allocation.createTyped(rs,inputAllocation.type)

            inverter.setColorMatrix(TRANSFORMATION_MATRIX)
            inverter.forEach(inputAllocation,outputAllocation)
            outputAllocation.copyTo(result)
            rs.destroy()
            return result

        }
    }
}