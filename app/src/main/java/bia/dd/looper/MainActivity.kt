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


class MainActivity : AppCompatActivity() {


    private lateinit var recyclerview : RecyclerView
    private lateinit var bucketList : ArrayList<ItemsViewModel>
    private lateinit var bucketListAdapter : CustomAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        bucketList = ArrayList()
        bucketList.add(ItemsViewModel("Go to a basketball game","2022/08/03", "Uncompleted", false ))
        bucketList.add(ItemsViewModel("Drink every Starbucks drink","2022/01/24", "2022/03/08", true ))
        bucketList.add(ItemsViewModel("Play minecraft","2022/03/24", "2022/03/20", true ))
        bucketList.add(ItemsViewModel("Graduate","2023/05/06", "Uncompleted", false ))

        bucketList.sortWith(compareBy<ItemsViewModel> {it.completed}.thenBy { it.completedDate }.thenBy { it.dueDate })
        //bucketList.sortBy { it.dueDate }

        // This will pass the ArrayList to our Adapter
        bucketListAdapter = CustomAdapter(bucketList)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = bucketListAdapter

        bucketListAdapter.onItemClick = {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            intent.putExtra("bucketItem", it)
            intent.putExtra("itemIndex", bucketList.indexOf(it))
            editItemActivity.launch(intent)
        }

        // fab
        val mAddFab = findViewById<FloatingActionButton>(R.id.add_fab)

        mAddFab.setOnClickListener {
            val intent = Intent(this@MainActivity, InsertListItem::class.java)
            addItemActivity.launch(intent)
            //startActivity(intent)

        }
    }


    fun addTrack() {

    }


}