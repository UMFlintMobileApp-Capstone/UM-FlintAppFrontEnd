package com.example.um_flintapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.um_flintapplication.databinding.ActivityScheduleAdvisorBinding
import com.google.android.material.navigation.NavigationView

class ScheduleAdvisorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleAdvisorBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleAdvisorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Schedule an Advisor"

        // Initialize DrawerLayout and NavigationView
        setupNavigationDrawer()

        // Dropdown Options
        val colleges = listOf(
            "College of Arts, Sciences and Education",
            "College of Innovation and Technology",
            "School of Nursing",
            "School of Management",
            "College of Health Sciences"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, colleges)
        binding.collegeDropdown.setAdapter(adapter)

        // Handle Selection
        binding.collegeDropdown.setOnItemClickListener { _, _, position, _ ->
            when (colleges[position]) {
                "College of Innovation and Technology" -> showCITAdvisors()
                else -> binding.advisorSection.isVisible = false
            }
        }
    }

    private fun showCITAdvisors() {
        binding.advisorSection.isVisible = true
        binding.advisorSection.removeAllViews()

        val advisors = listOf(
            Advisor("Cydnee Robertson", "Computer Information Systems Program (BS), Data Science Program (BS), Digital Machining and Automation Technology Program (BS), General Biology (BS) students with last names beginning with letters A-H, Human-Centered Design Program (BS), Information Technology and Informatics Program (BS), Sustainability and Renewable Energy Technology (BS), Wildlife Biology Program (BS)", "cweirauc@umich.edu", "https://calendly.com/cydnee"),
            Advisor("Jeff Dobbs", "Computer Information Systems Program (BS), Data Science Program (BS), Digital Machining and Automation Technology Program (BS), General Biology (BS) students with last names beginning with letters A-H, Human-Centered Design Program (BS), Information Technology and Informatics Program (BS), Sustainability and Renewable Energy Technology (BS), Wildlife Biology Program (BS)", "jdobbs@umich.edu", "https://calendly.com/jeffdobbs"),
            Advisor("Ashley Bennett", "Artificial Intelligence (BS), Computer Science Program (BS), Cybersecurity Program (BS), Software Engineering Program (BS)", "amarieb@umich.edu", "https://go.umflint.edu/AshleyBennett"),
            Advisor("Dan McCabe", "Applied & Engineering Physics Program (BS), Electrical Engineering Program (BSE), General Biology (BS) students with last names beginning with letters Q-Z, Mechanical Engineering Program (BSE), Preferential Admissions Transfer Program with the University of Michigan-Ann Arbor College of Engineering", "dmmccabe@umich.edu", "https://go.umflint.edu/DanMcCabe"),
            Advisor("Aubree Kraut", "All CIT Graduate Programs", "arottier@umich.com", "https://go.umflint.edu/AubreeKraut"),
            )

        for (advisor in advisors) {
            val advisorView = layoutInflater.inflate(R.layout.advisor_card, null)
            advisorView.findViewById<TextView>(R.id.advisor_name).text = advisor.name
            advisorView.findViewById<TextView>(R.id.programs).text = advisor.programs
            advisorView.findViewById<TextView>(R.id.email).text = "Email: ${advisor.email}"
            advisorView.findViewById<Button>(R.id.schedule_button).setOnClickListener {
                // Open advisor's Calendly link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(advisor.link))
                startActivity(intent)
            }
            binding.advisorSection.addView(advisorView)
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
}
