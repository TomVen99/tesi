package com.example.smartlagoon.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.IOException
import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MarineClassifier(context: Context) {

    private var interpreter: Interpreter

    init {
        val model = loadModelFile(context, "model.tflite")
        interpreter = Interpreter(model)
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context, modelFile: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelFile)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classifyImage(bitmap: Bitmap): Int {
        val input = preprocessImage(bitmap)
        val output = Array(1) { FloatArray(NUM_CLASSES) }
        interpreter.run(input, output)
        //return output[0].indexOf(output[0].maxOrNull() ?: 0f)
        val outputList = output[0].toList()
        return outputList.indexOf(outputList.maxOrNull() ?: 0f)
    }

    /*private fun preprocessImage(bitmap: Bitmap): Array<FloatArray> {
        // Converti l'immagine in un array FloatArray con la dimensione corretta
        return arrayOf(FloatArray(bitmap.width * bitmap.height))
    }*/
    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        // Dimensioni dell'immagine di input
        val imgHeight = 224
        val imgWidth = 224
        val inputSize = imgHeight * imgWidth * 3

        // Crea un ByteBuffer per l'input del modello
        val inputBuffer = ByteBuffer.allocateDirect(4 * inputSize)
        inputBuffer.order(ByteOrder.nativeOrder())

        // Scala l'immagine al formato desiderato
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, imgWidth, imgHeight, true)

        // Converti l'immagine in un tensore di input di 4 dimensioni [1, height, width, channels]
        val intValues = IntArray(imgHeight * imgWidth)
        scaledBitmap.getPixels(intValues, 0, imgWidth, 0, 0, imgWidth, imgHeight)

        // Riempie il ByteBuffer con i dati del bitmap
        for (pixel in intValues) {
            // Estrai i valori di colore RGB dal pixel e normalizza tra 0 e 1
            inputBuffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f) // R
            inputBuffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f)  // G
            inputBuffer.putFloat((pixel and 0xFF) / 255.0f)         // B
        }

        // Restituisce il buffer di input preparato
        return inputBuffer
    }


    companion object {
        private const val NUM_CLASSES = 10 // Modifica in base al numero di classi nel tuo modello
    }

    enum class Categories {
        Fish, Goldfish, HarborSeal, Jellyfish, Lobster, Other, Oyster, SeaTurtle, Squid, Starfish
    }
}
