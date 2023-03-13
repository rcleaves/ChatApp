package com.droidekar.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.SurfaceControlViewHost
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val msgList: ArrayList<Message>)
    : RecyclerView.Adapter<ViewHolder>(){

    val ITEM_RECEIVE = 1
    val ITEM_SEND = 2

    class SendViewHolder(itemView: View) : ViewHolder(itemView) {
        val sendMessage = itemView.findViewById<TextView>(R.id.send_msg)
    }

    class ReceiveViewHolder(itemView: View) : ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.receive_msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == ITEM_RECEIVE) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            return SendViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val msg = msgList.get(position)
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(msg.senderId)) {
            return ITEM_SEND
        } else {
            return ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = msgList.get(position)
        if(holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            holder.sendMessage.text = msg.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = msg.message
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

}