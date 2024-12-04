package com.example.um_flintapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.um_flintapplication.apiRequests.GenericResponse
import com.example.um_flintapplication.apiRequests.Retrofit
import com.example.um_flintapplication.databinding.ActivitySendAnnouncementBinding
import com.google.android.material.navigation.NavigationView
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SendAnnouncementActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendAnnouncementBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using the correct binding class
        binding = ActivitySendAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Send Announcements"

        val googleSignIn = Auth(this)

        googleSignIn.goHomeIfUnauthorized()

        // Initialize DrawerLayout and NavigationView
        setupNavigationDrawer()

        // Setup announcement functionality
        setupFormFunctionality()
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

        // Set up NavigationView to work with navigation destinations
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to the Home page
                    val intent =
                        Intent(this, MainActivity::class.java) // Replace with your Home activity
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

                R.id.nav_messaging_discord -> {
                    val url = "https://discord.gg/AEefzfqSB9"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    true
                }

                R.id.nav_messaging_student_messaging -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, MessagingActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupFormFunctionality() {
        // Date Picker
        val calendar = Calendar.getInstance()
        binding.btnPickDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                binding.tvDate.text = "${month + 1}/$dayOfMonth/$year"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.eBtnPickDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                binding.evDate.text = "${month + 1}/$dayOfMonth/$year"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Populate Spinner
        val groups = listOf(Role("Student",1),Role("Staff",2),Role("Faculty",3))

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, groups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGroup.adapter = adapter

        // Send Button
        binding.btnSend.setOnClickListener {
            val subject = binding.etSubject.text.toString()
            val message = binding.etMessage.text.toString()
            val startDate = binding.tvDate.text.toString()
            val endDate = binding.evDate.text.toString()
            val group = binding.spinnerGroup.selectedItem as Role

            if (subject.isNotBlank() && message.isNotBlank() &&
                startDate.isNotBlank() && endDate.isNotBlank()) {

                CoroutineScope(Dispatchers.IO).launch {
                    var response: GenericResponse? = null

                    Retrofit(this@SendAnnouncementActivity).api.addAnnouncement(
                        subject, message, startDate, endDate, group.id
                    ).onSuccess {
                        response = data
                    }

                    val finalResponse = response
                    if(finalResponse!=null){
                        if(finalResponse.status=="success"){
                            withContext(Dispatchers.Main){
                                // Reset fields after successful submission
                                binding.etSubject.text.clear()
                                binding.etMessage.text.clear()
                                binding.tvDate.text = "" // Clear the date TextView
                                binding.evDate.text = ""
                                binding.spinnerGroup.setSelection(0) // Reset the Spinner to the first item

                                Toast.makeText(this@SendAnnouncementActivity,
                                    finalResponse.message, Toast.LENGTH_SHORT).show()
                            }
                        }else if(finalResponse.status=="failure") {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@SendAnnouncementActivity,
                                    finalResponse.message, Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@SendAnnouncementActivity,
                               "Unable to send announcement, try again.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    data class Role(val name: String, val id: Int){
        override fun toString(): String = name
    }
}