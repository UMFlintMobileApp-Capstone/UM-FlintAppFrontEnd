package com.example.um_flintapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.um_flintapplication.databinding.ActivityMessagingBinding
import com.google.android.material.navigation.NavigationView

class MessagingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessagingBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val users = listOf("User A", "User B", "User C") // Sample users

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

        // Initialize the Spinner for User Selection
        setupUserSpinner()

        // Initialize RecyclerView for messages
        setupRecyclerView()

        // Set up message sending functionality
        setupMessageSending()
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
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_resources_academic_calendar -> {
                    startActivity(Intent(this, AcademicCalendar::class.java))
                    true
                }
                R.id.nav_resources_departments -> {
                    startActivity(Intent(this, DepartmentInformationActivity::class.java))
                    true
                }
                R.id.nav_resources_maps -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                R.id.nav_announcements -> {
                    startActivity(Intent(this, AlertsActivity::class.java))
                    true
                }
                R.id.nav_messaging_discord -> {
                    val url = "https://discord.gg/AEefzfqSB9"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    true
                }
                R.id.nav_messaging_student_messaging -> {
                    // Stay on the current page
                    true
                }
                else -> false
            }
        }
    }

    private fun setupUserSpinner() {
        val userSpinner = binding.spinnerUsers
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, users)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userSpinner.adapter = adapter

        // Handle user selection from the spinner
        userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                // Logic to load messages for the selected user
                val selectedUser = users[position]
                // Load messages for this user
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRecyclerView() {
        val messageRecyclerView = binding.recyclerMessages
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        // Set up your adapter for messages (if implemented)
    }

    private fun setupMessageSending() {
        val messageInput = binding.etMessageInput
        val sendButton = binding.btnSendMessage

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotBlank()) {
                // Logic to send the message
                messageInput.text.clear()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}