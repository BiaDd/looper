/** references
 * https://www.youtube.com/watch?v=NiJzLUysicg&list=PLpZQVidZ65jPz-XIHdWi1iCra8TU9h_kU&index=4
 *
 * **/
package bia.dd.looper

import android.Manifest
import androidx.appcompat.app.AppCompatActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.*
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// add a record button
// add a play button
// add tracks
// let to play or record track seperately

// later allow to click on track and edit tracks by clipping

const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity(), Timer.OnTimerTickListener {


//    private lateinit var recyclerview : RecyclerView
//    private lateinit var trackList : ArrayList<TrackModel>
//    private lateinit var trackListAdapter : CustomAdapter

    private lateinit var amplitudes : ArrayList<Float>
    private var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false

    private lateinit var recorder: MediaRecorder
    private var dirPath = ""
    private var filename = ""
    private var isRecording = false
    private var isPaused = false

    private lateinit var timer : Timer

    private lateinit var duration : String

    private lateinit var db : AppDatabase

    private lateinit var vibrator : Vibrator

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
        }

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "audioRecords"
        ).build()

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

        timer = Timer(this)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        btnRecord.setOnClickListener {
            when {
                isPaused->resumeRecorder()
                isRecording->pauseRecorder()
                else->startRecording()
            }

            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }

        btnList.setOnClickListener() {

        }

        btnDone.setOnClickListener() {
            stopRecorder()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBG.visibility = View.VISIBLE
            filenameinput.setText(filename)
        }

        btnCancel.setOnClickListener() {
            File("$dirPath$filename.mp3").delete()
            dismiss()
        }

        btnOk.setOnClickListener() {
            save()
            dismiss()
        }

        bottomSheetBG.setOnClickListener() {
            File("$dirPath$filename.mp3").delete()
            dismiss()
        }

        btnDelete.setOnClickListener() {
            stopRecorder()
            File("$dirPath$filename.mp3").delete()
        }

        btnDelete.isClickable = false

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

    private fun save() {
        val newFilename = filenameinput.text.toString()
        if (newFilename != filename) {
            var newFile = File("$dirPath$newFilename.mp3")
            File("$dirPath$filename.mp3").renameTo(newFile)
        }

        val filePath = "$dirPath$newFilename.mp3"
        val timeStamp = Date().time
        val ampsPath = "$dirPath$newFilename"

        try {
            val fos = FileOutputStream(ampsPath)
            val out = ObjectOutputStream(fos)
            out.writeObject(amplitudes)
            fos.close()
            out.close()
        } catch (e : IOException) {

        }
        val record = AudioRecord(newFilename, filePath, timeStamp, duration, ampsPath)

        GlobalScope.launch {
            db.audioRecordDao().insert(record)
        }

    }

    private fun hideKeyboard(view : View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }

    private fun dismiss() {
        bottomSheetBG.visibility = View.GONE
        hideKeyboard(filenameinput)

        Handler(Looper.getMainLooper()).postDelayed({
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }, 100)
    }

    private fun pauseRecorder() {
        recorder.pause()
        isPaused = true
        btnRecord.setImageResource(R.drawable.ic_record)
        timer.pause()

    }

    private fun resumeRecorder() {
        recorder.resume()
        isPaused = false
        btnRecord.setImageResource(R.drawable.ic_pause)
        timer.start()
    }

    private fun startRecording() {
        if(!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
            return
        }
        // else start recording
        recorder = MediaRecorder()
        dirPath = "${externalCacheDir?.absolutePath}/"

        var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        var date : String = simpleDateFormat.format(Date())
        filename = "audio_record_$date"

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$dirPath$filename.mp3")

            try {
                prepare()
            }
            catch (e:IOException){

            }
            start()
        }

        btnRecord.setImageResource(R.drawable.ic_pause)
        isRecording = true
        isPaused = false
        timer.start()

        btnDelete.isClickable = true
        btnDelete.setImageResource(R.drawable.ic_delete)

        btnList.visibility = View.GONE
        btnDone.visibility = View.VISIBLE
    }

    private fun stopRecorder() {
        timer.stop()
        // stop recorder
        recorder.apply() {
            stop()
            release()
        }
        isPaused=false
        isRecording=false

        btnList.visibility = View.VISIBLE
        btnDone.visibility = View.GONE

        btnDelete.isClickable = false
        btnDelete.setImageResource(R.drawable.ic_delete_disabled)

        btnRecord.setImageResource(R.drawable.ic_record)

        tvTimer.text ="00:00:00"
        amplitudes = waveFormView.clear()

    }

    override fun onTimerTick(duration: String) {
        tvTimer.text = duration
        this.duration = duration.dropLast(3)
        waveFormView.addAmplitude(recorder.maxAmplitude.toFloat())
    }


}