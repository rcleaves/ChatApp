package com.droidekar.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var nameListView: RecyclerView
    private lateinit var nameList: ArrayList<User>
    private lateinit var adapter: UserAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseDatabase.getInstance().getReference()

        // setup recycler
        nameListView = findViewById(R.id.name_list)
        nameListView.layoutManager = LinearLayoutManager(this)

        nameList = ArrayList<User>()
        adapter = UserAdapter(this, nameList)
        nameListView.adapter = adapter

        // load users from DB
        mDb.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear list
                nameList.clear()

                // populate list exclude self
                for(data in snapshot.children) {
                    val user = data.getValue(User::class.java)
                    if (mAuth.currentUser?.uid != user?.uuid) {
                        nameList.add(user!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            // logout user
            mAuth.signOut()
            finish()
            return true
        }
        return true;
        return super.onOptionsItemSelected(item)
    }
}