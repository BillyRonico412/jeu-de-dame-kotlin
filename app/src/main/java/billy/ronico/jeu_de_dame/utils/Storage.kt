package billy.ronico.jeu_de_dame.utils

import android.content.Context
import billy.ronico.jeu_de_dame.models.Setting
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun persisteSetting(context: Context, setting: Setting) {
    val fileOutput = context.openFileOutput("setting.txt", Context.MODE_PRIVATE)
    val outputStream = ObjectOutputStream(fileOutput)
    outputStream.writeObject(setting)
    outputStream.close()
}

fun loadSetting(context: Context): Setting {
    val fileInput = context.openFileInput("setting.txt")
    val inputStream = ObjectInputStream(fileInput)
    val setting = inputStream.readObject() as Setting
    inputStream.close()
    return setting
}