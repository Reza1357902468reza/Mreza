package com.example.geophoto

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // osmdroid setup (uses device cache, no API key)
        Configuration.getInstance().load(this, this.getPreferences(MODE_PRIVATE))

        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapview)

        val lat = intent.getDoubleExtra("lat", Double.NaN)
        val lng = intent.getDoubleExtra("lng", Double.NaN)
        val photoUriStr = intent.getStringExtra("photo_uri")

        val defaultPoint = GeoPoint(0.0, 0.0)
        val point = if (!lat.isNaN() && !lng.isNaN()) GeoPoint(lat, lng) else defaultPoint

        mapView.controller.setZoom(16.0)
        mapView.controller.setCenter(point)

        // place marker if we have coordinates
        if (!lat.isNaN() && !lng.isNaN()) {
            val marker = Marker(mapView)
            marker.position = point
            marker.title = "Photo location"
            photoUriStr?.let { s ->
                try {
                    val uri = Uri.parse(s)
                    val stream = contentResolver.openInputStream(uri)
                    stream?.use {
                        val bmp = BitmapFactory.decodeStream(it)
                        // marker icon or info window could use bmp if desired
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
            mapView.overlays.add(marker)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
