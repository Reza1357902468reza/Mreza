package com.example.geophoto

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream

class EditPhotoActivity : AppCompatActivity() {

    private lateinit var imgView: ImageView
    private lateinit var editText: EditText
    private lateinit var btnApply: Button
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_photo)

        imgView = findViewById(R.id.img_edit)
        editText = findViewById(R.id.edit_text)
        btnApply = findViewById(R.id.btn_apply)

        photoUri = intent.getStringExtra("photo_uri")?.let { Uri.parse(it) }

        val bmp = contentResolver.openInputStream(photoUri!!)?.use { BitmapFactory.decodeStream(it) }
        imgView.setImageBitmap(bmp)

        btnApply.setOnClickListener {
            bmp?.let {
                val mutableBmp = it.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(mutableBmp)
                val paint = Paint().apply {
                    color = Color.WHITE
                    textSize = 64f
                    setShadowLayer(5f, 3f, 3f, Color.BLACK)
                }
                canvas.drawText(editText.text.toString(), 50f, mutableBmp.height - 100f, paint)

                val file = File(cacheDir, "edited_${'$'}{System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { out ->
                    mutableBmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
                }

                // read exif
                val exifInput = contentResolver.openInputStream(photoUri!!)
                val latLong = exifInput?.use { ExifInterface(it).latLong }

                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra("photo_uri", Uri.fromFile(file).toString())
                if (latLong != null) {
                    intent.putExtra("lat", latLong[0])
                    intent.putExtra("lng", latLong[1])
                }
                startActivity(intent)
            }
        }
    }
}
