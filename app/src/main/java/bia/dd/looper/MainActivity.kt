package bia.dd.looper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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


class MainActivity : AppCompatActivity() {


    private lateinit var recyclerview : RecyclerView
    private lateinit var trackList : ArrayList<TrackModel>
    private lateinit var trackListAdapter : CustomAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        trackList = ArrayList()


        // This will pass the ArrayList to our Adapter
        trackListAdapter = CustomAdapter(trackList)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = trackListAdapter


    }


    fun addTrack() {

    }


}