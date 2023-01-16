/** references
 * https://www.youtube.com/watch?v=NiJzLUysicg&list=PLpZQVidZ65jPz-XIHdWi1iCra8TU9h_kU&index=4
 *
 * **/
package bia.dd.looper

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// add a record button
// add a play button
// add tracks
// let to play or record track seperately

// later allow to click on track and edit tracks by clipping

const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {


//    private lateinit var recyclerview : RecyclerView
//    private lateinit var trackList : ArrayList<TrackModel>
//    private lateinit var trackListAdapter : CustomAdapter

    private var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false

    private lateinit var recorder: MediaRecorder
    private var dirPath = ""
    private var filename = ""
    private var isRecording = false
    private var isPaused = false

    //private var btnRecord = findViewById<FloatingActionButton>(R.id.record_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
        }


//        // getting the recyclerview by its id
//        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
//
//        // this creates a vertical layout Manager
//        recyclerview.layoutManager = LinearLayoutManager(this)
//
//        trackList = ArrayList()
//
//        // This will pass the ArrayList to our Adapter
//        trackListAdapter = CustomAdapter(trackList)
//
//        // Setting the Adapter with the recyclerview
//        recyclerview.adapter = trackListAdapter



//        btnRecord.setOnClickListener {
//            when {
//                isPaused->resumeRecorder()
//                isRecording->pauseRecorder()
//                else->startRecording()
//            }
//        }


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

//    private fun pauseRecorder() {
//        recorder.pause()
//        isPaused = true
//        btnRecord.setImageResource(R.drawable.ic_record)
//
//    }
//
//    private fun resumeRecorder() {
//        recorder.resume()
//        isPaused = false
//        btnRecord.setImageResource(R.drawable.ic_pause)
//    }
//
//    private fun startRecording() {
//        if(!permissionGranted) {
//            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
//            return
//        }
//        // else start recording
//        recorder = MediaRecorder()
//        dirPath = "${externalCacheDir?.absolutePath}/"
//
//        var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
//        var date : String = simpleDateFormat.format(Date())
//        filename = "audio_record_$date"
//
//        recorder.apply {
//            setAudioSource(MediaRecorder.AudioSource.MIC)
//            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//            setOutputFile("$dirPath$filename.mp3")
//
//            try {
//                prepare()
//            }
//            catch (e:IOException){
//
//            }
//            start()
//        }
//
//        btnRecord.setImageResource(R.drawable.ic_pause)
//        isRecording = true
//        isPaused = false
//
//
//    }

    fun addTrack() {

    }


}