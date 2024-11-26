package com.example.um_flintapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.um_flintapplication.apiRequests.Retrofit
import com.example.um_flintapplication.databinding.ActivityScheduleGroupMeetingBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleGroupMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleGroupMeetingBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleGroupMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buildings = ArrayList<String>()

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Reserve a Room"

        // Initialize DrawerLayout and NavigationView
        setupNavigationDrawer()

        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(this, android.R.layout.simple_dropdown_item_1line, buildings as List<String?>)
        binding.buildingDropdown.setAdapter(adapter)
        adapter.setNotifyOnChange(true)

        // Handle Dropdown Selection
        binding.buildingDropdown.setOnItemClickListener { _, _, position, _ ->
            binding.roomSection.isVisible = true
            loadRoomData(buildings[position])
        }

        // Dropdown Options
        CoroutineScope(Dispatchers.IO).launch {
            val building = Retrofit(this@ScheduleGroupMeetingActivity).api.getBuildings()

            building.forEach { item ->
                buildings.add(item.name)
                adapter.notifyDataSetChanged()
            }
        }

        // Reset Button
        binding.resetButton.setOnClickListener {
            binding.buildingDropdown.setText("")
            binding.roomSection.isVisible = false
        }
    }

    private fun loadRoomData(building: String) {
        binding.roomSection.removeAllViews()

        CoroutineScope(Dispatchers.IO).launch {
            Retrofit(this@ScheduleGroupMeetingActivity).api.getRooms(building).forEach{item ->
                Retrofit(this@ScheduleGroupMeetingActivity).api.getRoomTimes(item.id).forEach{time ->
                    val roomView = layoutInflater.inflate(R.layout.room_card, null)
                    roomView.findViewById<TextView>(R.id.room_name).text = item.name
                    roomView.findViewById<TextView>(R.id.timings).text =
                        buildString {
                            append("Time: ")
                            append(time.startTime)
                            append(" - ")
                            append(time.endTime)
                        }
                    roomView.findViewById<Button>(R.id.select_room_button).setOnClickListener {
                        // Handle room selection
                    }

                    withContext(Dispatchers.Main){
                        binding.roomSection.addView(roomView)
                    }
                }
            }
        }
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
                    val intent = Intent(this, MapsActivity::class.java)
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
}