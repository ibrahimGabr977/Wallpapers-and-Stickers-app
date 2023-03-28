package com.hope.igb.footballtime.screens.display.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


@Suppress("deprecation")
class ImageSaver(private val context: Context, currentFolderName: String?) {






    private val appFolder : File
    private val currentFolder: File


    init {
        appFolder = baseFolder()
        currentFolder = File(appFolder.absolutePath, ""+currentFolderName)
    }



     private fun baseFolder() : File{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Football Time")
        else
            File(Environment.getExternalStorageDirectory(), "Football Time")
    }







        fun saveImage(fileName : String, imageRes: Int): File{

            createAppFolders()

            val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageRes)


            val imageFile = File(currentFolder, "$fileName.jpg")
            val fot =FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fot)
            fot.flush()
            fot.close()

            return imageFile

        }


    private fun createAppFolders(){

        if(!appFolder.exists())
            appFolder.mkdir()

        if (!currentFolder.exists())
            currentFolder.mkdir()

    }










}