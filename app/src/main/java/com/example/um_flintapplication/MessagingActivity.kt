package com.example.um_flintapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.um_flintapplication.apiRequests.CreateThread
import com.example.um_flintapplication.apiRequests.GenericResponse
import com.example.um_flintapplication.apiRequests.MessageThread
import com.example.um_flintapplication.apiRequests.Retrofit
import com.example.um_flintapplication.apiRequests.Thread
import com.example.um_flintapplication.apiRequests.WebSocketClient
import com.example.um_flintapplication.databinding.ActivityMessagingBinding
import com.google.android.material.navigation.NavigationView
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime

class MessagingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessagingBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val threads = ArrayList<Thread>()
    private lateinit var adapter: MessageAdapter
    private lateinit var toUser: Thread

    private lateinit var webSocketClient: WebSocketClient

    private val socketListener = object: WebSocketClient.SocketListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onMessage(message: String) {
            Log.e("socketCheck onMessage", message)

            val m = JSONObject(message)

            runOnUiThread(Runnable{
                adapter.addMessage(
                    Message(
                        m.getString("from"),
                        m.getString("text"),
                        m.getString("date")
                    )
                )

                adapter.notifyDataSetChanged()
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityMessagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Messaging"

        // Initialize DrawerLayout and NavigationView
        setupNavigationDrawer()

        webSocketClient = WebSocketClient.getInstance(this@MessagingActivity)
        webSocketClient.setSocketUrl("wss://umflintapp.troxal.com/messaging/ws/")
        webSocketClient.setListener(socketListener)

        // Initialize the Spinner for User Selection
        setupUserSpinner()

        // Initialize RecyclerView for messages
        setupRecyclerView()

        setupThreadManager()
    }

    private fun setupNavigationDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Initialize ActionBarDrawerToggle for the navigation drawer
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up NavigationView to handle menu item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to the Home page
                    val intent = Intent(this, MainActivity::class.java) // Replace with your Home activity
                    startActivity(intent)
                    true
                }
                R.id.nav_resources_academic_calendar -> {
                    // Navigate to Academic Calendar page
                    val intent = Intent(this, AcademicCalendar::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_resources_departments -> {
                    // Navigate to Departments page
                    val intent = Intent(this, DepartmentInformationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_resources_maps -> {
                    // Navigate to Maps page
                    val intent = Intent(this, MapsPage::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_scheduling_reserve_room -> {
                    // Navigate to Reserve Room page
                    val intent = Intent(this, ScheduleGroupMeetingActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_scheduling_schedule_advisor -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, ScheduleAdvisorActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_announcements -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, AlertsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_send_announcements -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, SendAnnouncementActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_messaging_discord-> {
                    val url = "https://discord.gg/AEefzfqSB9"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    true
                }
                R.id.nav_messaging_student_messaging-> {
                    // Navigate to Announcements page
                    val intent = Intent(this, MessagingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupUserSpinner() {
        val sAdapter: ArrayAdapter<Thread?> = ArrayAdapter<Thread?>(this, android.R.layout.simple_dropdown_item_1line, threads as List<Thread?>)
        binding.threadDropdown.setAdapter(sAdapter)

        threads.clear()

        // Dropdown Options
        CoroutineScope(Dispatchers.IO).launch {
            Retrofit(this@MessagingActivity).api.getMessageThreads().onSuccess {
                data.forEach{thread ->
                    threads.add(thread)
                }
            }

            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
            }
        }

        // Handle Selection
        binding.threadDropdown.setOnItemClickListener { _, _, position, _ ->
            adapter.clearMessages()

            toUser = threads[position]
            // Logic to load messages for the selected user
            var messages: List<MessageThread>? = null

            CoroutineScope(Dispatchers.IO).launch {
                Retrofit(this@MessagingActivity).api.getMessages(
                    toUser.uuid
                ).onSuccess {
                    messages = data

                    Log.e("MESSAGE:", data.toString())
                }

                messages?.forEach { message ->
                    adapter.addMessage(
                        Message(
                            message.sender.email,
                            message.message,
                            message.sendDate
                        )
                    )

                    withContext(Dispatchers.Main){
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            // Set up message sending functionality
            setupMessageSending()
        }
    }

    private fun setupRecyclerView() {
        val messageRecyclerView = binding.recyclerMessages
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter()

        messageRecyclerView.adapter=adapter
    }

    private fun setupMessageSending() {
        val messageInput = binding.etMessageInput
        val sendButton = binding.btnSendMessage

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotBlank()) {
                // Logic to send the message
                val msg = JSONObject()
                val users = JSONArray()

                toUser.users.forEach { u ->
                    users.put(u.email)
                }

                msg.put("type","message")
                msg.put("to",users)
                msg.put("text",message)
                msg.put("date",LocalDateTime.now())

                webSocketClient.sendMessage(msg.toString())

                messageInput.text.clear()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupThreadManager(){
        val deleteBtn = binding.deletebutton
        val addBtn = binding.addbutton

        deleteBtn.setOnClickListener {
            if(::toUser.isInitialized){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm thread deletion?")
                builder.setMessage("Are you sure you want to delete this thread with ${toUser}?")

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    CoroutineScope(Dispatchers.IO).launch{
                        var response: GenericResponse? = null

                        Retrofit(this@MessagingActivity).api.deleteMessageThread(
                            toUser.uuid
                        ).onSuccess {
                            response = data
                        }

                        if(response!=null){
                            withContext(Dispatchers.Main){
                                Toast.makeText(applicationContext,
                                    "Successfully deleted thread with $toUser!", Toast.LENGTH_SHORT).show()
                            }
                            threads.remove(toUser)
                            withContext(Dispatchers.Main){
                                setupUserSpinner()
                                adapter.clearMessages()
                                adapter.notifyDataSetChanged()
                            }
                        }else{
                            withContext(Dispatchers.Main){
                                Toast.makeText(applicationContext,
                                    "Failed to delete thread with ${toUser}!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                }
                builder.show()
            }
        }

        addBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Create Thread")
            val dialogLayout = inflater.inflate(R.layout.alert_edit_text, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                CoroutineScope(Dispatchers.IO).launch {
                    var thread: CreateThread? = null

                    Retrofit(this@MessagingActivity).api.createThread(
                        editText.text.toString()
                    ).onSuccess {
                        thread = data
                    }

                    if(thread?.threadCreated==true){
                        withContext(Dispatchers.Main){
                            setupUserSpinner()
                            Toast.makeText(applicationContext, "Created thread with " + editText.text.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            builder.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()

        webSocketClient.disconnect()
    }

    override fun onResume() {
        super.onResume()

        webSocketClient.connect()
    }
}