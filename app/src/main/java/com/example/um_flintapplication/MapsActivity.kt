package com.example.um_flintapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.um_flintapplication.databinding.ActivityAcademicCalendarBinding

class MapsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityAcademicCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAcademicCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Academic Calendar"

        setupNavigationDrawer()
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
