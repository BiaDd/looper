package bia.dd.looper

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

// add a record button
// add a play button
// add tracks
// let to play or record track seperately

// later allow to click on track and edit tracks by clipping

const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerview : RecyclerView
    private lateinit var trackList : ArrayList<TrackModel>
    private lateinit var trackListAdapter : CustomAdapter

    private var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
        }


        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        trackList = ArrayList()

        // This will pass the ArrayList to our Adapter
        trackListAdapter = CustomAdapter(trackList)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = trackListAdapter

        val recordFab = findViewById<FloatingActionButton>(R.id.record_button)

        recordFab.setOnClickListener {
            startRecording()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun startRecording() {
        if(!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
            return
        }
        // else start recording
    }

    fun addTrack() {

    }


}