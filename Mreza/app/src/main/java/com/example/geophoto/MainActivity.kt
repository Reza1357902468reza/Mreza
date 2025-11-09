package com.example.geophoto

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var btnPick: Button
    private lateinit var btnCamera: Button
    private lateinit var imgPreview: ImageView
    private lateinit var tvCoords: TextView

    private var photoUri: Uri? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { openEditPhoto(it) }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && photoUri != null) {
            openEditPhoto(photoUri!!)
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        if (perms.values.all { it }) {
            pickMedia.launch("image/*")
        } else {
            Toast.makeText(this, "نیاز به مجوز است", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPick = findViewById(R.id.btn_pick)
        btnCamera = findViewById(R.id.btn_camera)
        imgPreview = findViewById(R.id.img_preview)
        tvCoords = findViewById(R.id.tv_coords)

        btnPick.setOnClickListener {
            requestPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }

        btnCamera.setOnClickListener {
            val file = File(cacheDir, "captured_${System.currentTimeMillis()}.jpg")
            photoUri = FileProvider.getUriForFile(this, "${'$'}packageName.provider", file)
            takePicture.launch(photoUri)
        }

        if (intent?.action == Intent.ACTION_SEND) {
            (intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri)?.let { openEditPhoto(it) }
        }
    }

    private fun openEditPhoto(uri: Uri) {
        val intent = Intent(this, EditPhotoActivity::class.java)
        intent.putExtra("photo_uri", uri.toString())
        startActivity(intent)
    }
}
