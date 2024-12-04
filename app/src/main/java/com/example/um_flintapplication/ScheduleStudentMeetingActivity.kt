package com.example.um_flintapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.um_flintapplication.apiRequests.BuildingRooms
import com.example.um_flintapplication.apiRequests.GenericResponse
import com.example.um_flintapplication.apiRequests.Retrofit
import com.example.um_flintapplication.databinding.ActivityScheduleStudentMeetingBinding
import com.google.android.material.navigation.NavigationView
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.forEach

class ScheduleStudentMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleStudentMeetingBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using the correct binding class
        binding = ActivityScheduleStudentMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Schedule Student Meeting"

        val justSignedIn = intent.extras?.getBoolean("justLoggedIn")
        if(justSignedIn==null){
            val googleSignIn = Auth(this)

            googleSignIn.goHomeIfUnauthorized()
        }

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

                R.id.nav_scheduling_schedule_student -> {
                    // Navigate to Schedule Student Meeting page
                    val intent = Intent(this, ScheduleStudentMeetingActivity::class.java)
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
        val locations = ArrayList<BuildingRooms>()

        // Date Picker
        val calendar = Calendar.getInstance()
        binding.btnPickDate.setOnClickListener {
            pickDateTime { c ->
                binding.tvDate.text = c.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))
            }
        }

        binding.eBtnPickDate.setOnClickListener {
            pickDateTime { c ->
                binding.evDate.text = c.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGroup.adapter = adapter
        adapter.setNotifyOnChange(true)

        CoroutineScope(Dispatchers.IO).launch {
            Retrofit(this@ScheduleStudentMeetingActivity).api.getUsableLocations(
            ).onSuccess {
                data.forEach { item ->
                    locations.add(item)
                }
            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
            }
        }

        // Send Button
        binding.btnSend.setOnClickListener {
            val subject = binding.etSubject.text.toString()
            val message = binding.etMessage.text.toString()
            val startDate = binding.tvDate.text.toString()
            val endDate = binding.evDate.text.toString()
            val users = binding.eUsers.text.toString()
            val location = binding.spinnerGroup.selectedItem as BuildingRooms

            if (subject.isNotBlank() && message.isNotBlank() &&
                startDate.isNotBlank() && endDate.isNotBlank() &&
                users.isNotBlank()) {

                CoroutineScope(Dispatchers.IO).launch {
                    var response: GenericResponse? = null

                    Retrofit(this@ScheduleStudentMeetingActivity).api.scheduleStudentMeeting(
                        subject, message, startDate, endDate, location.id, users
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
                                binding.eUsers.text.clear()
                                binding.spinnerGroup.setSelection(0) // Reset the Spinner to the first item

                                Toast.makeText(this@ScheduleStudentMeetingActivity,
                                    finalResponse.message, Toast.LENGTH_SHORT).show()
                            }
                        }else if(finalResponse.status=="failure") {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@ScheduleStudentMeetingActivity,
                                    finalResponse.message, Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@ScheduleStudentMeetingActivity,
                                "Unable to schedule meeting, try again.", Toast.LENGTH_LONG).show()
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

    private fun pickDateTime(callback: (LocalDateTime) -> Unit) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)

                val localDate = LocalDateTime.ofInstant(
                    pickedDateTime.toInstant(), pickedDateTime.timeZone.toZoneId()
                )

                callback(localDate)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

}