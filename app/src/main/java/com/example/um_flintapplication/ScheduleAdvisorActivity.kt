package com.example.um_flintapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.um_flintapplication.apiRequests.Advisors
import com.example.um_flintapplication.apiRequests.Colleges
import com.example.um_flintapplication.apiRequests.Retrofit
import com.example.um_flintapplication.databinding.ActivityScheduleAdvisorBinding
import com.google.android.material.navigation.NavigationView
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.forEach

class ScheduleAdvisorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleAdvisorBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleAdvisorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colleges = ArrayList<Colleges>()

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Schedule an Advisor"

        // Initialize DrawerLayout and NavigationView
        setupNavigationDrawer()

        val adapter: ArrayAdapter<Colleges?> = ArrayAdapter<Colleges?>(this, android.R.layout.simple_dropdown_item_1line, colleges as List<Colleges?>)
        binding.collegeDropdown.setAdapter(adapter)
        adapter.setNotifyOnChange(true)

        // Dropdown Options
        CoroutineScope(Dispatchers.IO).launch {
            Retrofit(this@ScheduleAdvisorActivity).api.getColleges().onSuccess {
                data.forEach { item ->
                    colleges.add(item)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        // Handle Selection
        binding.collegeDropdown.setOnItemClickListener { _, _, position, _ ->
            binding.advisorSection.isVisible = true
            loadAdvisors(colleges[position].id)
        }
    }

    private fun loadAdvisors(college: Int) {
        // Clear existing cards
        binding.advisorSection.removeAllViews()

        CoroutineScope(Dispatchers.IO).launch {
            var advisors: List<Advisors>? = null
            Retrofit(this@ScheduleAdvisorActivity).api.getAdvisors(college).onSuccess {
                advisors = data
            }

            // Add cards dynamically
            advisors?.forEach { advisor ->
                val degrees = advisor.degrees.joinToString { it -> it.name }

                // Inflate the card view
                val advisorView = layoutInflater.inflate(R.layout.advisor_card, null)
                advisorView.findViewById<TextView>(R.id.advisor_name).text = advisor.name
                advisorView.findViewById<TextView>(R.id.programs).text = degrees
                advisorView.findViewById<TextView>(R.id.email).text = "Email: ${advisor.email}"

                // Set button click for scheduling
                if (advisor.curl != null) {
                    advisorView.findViewById<Button>(R.id.schedule_button).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(advisor.curl))
                        startActivity(intent)
                    }
                }

                // Ensure proper spacing (use margin if not applied in advisor_card.xml)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0, 0, 0, 16) // Add 16dp margin bottom
                advisorView.layoutParams = layoutParams

                // Add card to the advisor section
                withContext(Dispatchers.Main) {
                    binding.advisorSection.addView(advisorView)
                }
            }
        }
    }

    data class Advisor(val name: String, val programs: String, val email: String, val link: String)

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
}
