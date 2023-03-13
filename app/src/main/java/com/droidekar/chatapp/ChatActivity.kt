package com.droidekar.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var msgs: RecyclerView
    private lateinit var msgText: EditText
    private lateinit var sendButton: ImageView
    private lateinit var msgList: ArrayList<Message>
    private lateinit var adapter: MessageAdapter

    var receiverRoom: String? = null
    var senderRoom: String? = null
    private lateinit var mDb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mDb = FirebaseDatabase.getInstance().getReference()

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        msgs = findViewById(R.id.chat)
        msgText = findViewById(R.id.message_box)
        sendButton = findViewById(R.id.send_button)

        // setup recycler
        msgs.layoutManager = LinearLayoutManager(this)

        msgList = ArrayList<Message>()
        adapter = MessageAdapter(this, msgList)
        msgs.adapter = adapter

        // load msgs from DB
        mDb.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // prevent dups
                    msgList.clear()
                    for(data in snapshot.children) {
                        val message = data.getValue(Message::class.java)
                        msgList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        sendButton.setOnClickListener {
            val msg = msgText.text.toString()
            val messageObj = Message(msg, senderUid)

            // add msgs to both rooms
            mDb.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObj).addOnSuccessListener {
                    mDb.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObj)
                }

            msgText.setText("")

        }
    }
}