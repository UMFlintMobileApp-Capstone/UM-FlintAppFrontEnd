package com.example.um_flintapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter() :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private val messages: MutableList<Message> = mutableListOf<Message>()

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sender: TextView = itemView.findViewById(R.id.tvSender)
        val content: TextView = itemView.findViewById(R.id.tvMessageContent)
        val timestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.sender.text = message.sender
        holder.content.text = message.content
        holder.timestamp.text = message.timestamp
    }

    override fun getItemCount(): Int = messages.size

    // Function to update the messages in the adapter
    fun updateMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
    }

    fun addMessage(newMessage: Message){
        messages.add(newMessage)
    }

    fun clearMessages(){
        messages.clear()
    }
}