package com.example.smartlagoon.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.IOException

class TFLiteModel(context: Context) {

    private var interpreter: Interpreter

    init {
        val model = loadModelFile(context, "model1.tflite") // sostituisci con il nome del tuo modello
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

    fun runInference(input: FloatArray): FloatArray {
        val output = FloatArray(1) // Adatta in base all'output del modello
        interpreter.run(input, output)
        return output
    }
}
