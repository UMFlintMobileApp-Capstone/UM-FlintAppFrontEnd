package com.example.um_flintapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.um_flintapplication.databinding.ActivityDepartmentInformationBinding
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import android.net.Uri
import android.widget.LinearLayout.LayoutParams
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import com.google.android.material.elevation.ElevationOverlayProvider
import org.w3c.dom.Text

class DepartmentInformationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityDepartmentInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDepartmentInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Department Information"

        setupNavigationDrawer()

        // Find the containers
        val containerLayout1: LinearLayout = findViewById(R.id.containerLayout)
        val containerLayout2: LinearLayout = findViewById(R.id.containerLayout2)

        // Populate with static data
        val schoolsCollegesLinks = listOf(
            Pair("College of Arts, Sciences & Education", "https://www.umflint.edu/case/"),
            Pair("College of Health Sciences", "https://www.umflint.edu/chs/"),
            Pair("College of Innovation & Technology","https://www.umflint.edu/cit/"),
            Pair("School of Management","https://www.umflint.edu/som/"),
            Pair("School of Nursing","https://www.umflint.edu/nursing/")
        )
        val officesCentersLinks = listOf(
            Pair("Academic Advising", "https://www.umflint.edu/studentsuccess/academic-advising/"),
            Pair("Academic Affairs (Provost & Vice Chancellor)", "https://www.umflint.edu/provost/academic-affairs/"),
            Pair("Admissions","https://www.umflint.edu/admissions/"),
            Pair("Alumni Relations","https://www.umflint.edu/advancement/alumni-relations/"),
            Pair("Assessment of Student Learning","https://www.umflint.edu/assessment/"),
            Pair("Bookstore","https://umflint.bncollege.com/"),
            Pair("Campus Counselor","https://www.umflint.edu/caps/"),
            Pair("Cashier/Student Accounts","https://www.umflint.edu/studentaccounts/"),
            Pair("Center for Gender & Sexuality","https://www.umflint.edu/cgs/"),
            Pair("Center for Global Engagement","https://www.umflint.edu/cge/"),
            Pair("Chancellor","https://www.umflint.edu/chancellor/"),
            Pair("Conference & Events","https://www.umflint.edu/cae/"),
            Pair("Counseling & Psychological Services","https://www.umflint.edu/caps/"),
            Pair("Development","https://www.umflint.edu/advancement/development/"),
            Pair("Dining Services","https://www.umflint.edu/dining-services/"),
            Pair("Disability & Accessibility Support Services","https://www.umflint.edu/disabilitysupportservices/"),
            Pair("Early Childhood Development Center","https://www.umflint.edu/ecdc/"),
            Pair("Education Abroad","https://www.umflint.edu/cge/education-abroad/"),
            Pair("Education Student Hub","https://www.umflint.edu/case/academic-departments/education/student-hub/"),
            Pair("Educational Opportunity Initiatives","https://www.umflint.edu/eoi/"),
            Pair("Emergency Information Center","https://www.umflint.edu/campus-safety-information-and-resources/"),
            Pair("Engaged Learning","https://www.umflint.edu/engaged-learning-office/"),
            Pair("Enrollment Management","https://www.umflint.edu/enrollment-management/"),
            Pair("Environment, Health, & Safety","https://www.umflint.edu/ehs/"),
            Pair("Equity, Civil Rights & Title IX","https://www.umflint.edu/ecrt/"),
            Pair("Facilities & Operations","https://www.umflint.edu/facilities/"),
            Pair("Financial Aid","https://www.umflint.edu/finaid/"),
            Pair("Financial Services & Contract","https://www.umflint.edu/financialservices/"),
            Pair("Geographic Information Systems Center","https://www.umflint.edu/gis/"),
            Pair("Government & Community Relations","https://www.umflint.edu/govrelations/"),
            Pair("Graduate Programs","https://www.umflint.edu/graduateprograms/"),
            Pair("Housing & Residential Life","https://www.umflint.edu/housing/"),
            Pair("Human Subjects Protection – Institutional Review Board","https://www.umflint.edu/research/compliance/institutional-review-board/"),
            Pair("Information Technology Services","https://www.umflint.edu/its/"),
            Pair("Institutional Analysis","https://www.umflint.edu/ia/"),
            Pair("Intercultural Center","https://www.umflint.edu/icc/"),
            Pair("K-12 Partnerships","https://www.umflint.edu/k12/"),
            Pair("Library (Frances Willson Thompson Library)","https://libguides.umflint.edu/library?_gl=1*cqofe6*_gcl_aw*R0NMLjE3MzAyMTc0ODAuQ2owS0NRandqNEs1QmhEWUFSSXNBRDFMeTJvZU1NVWhtbU5NU1hWb01HS3kzN0ZFVXdaeF9WZC01VXJSTzgzUlhVZERyYWgtekgwYXQwa2FBcXIxRUFMd193Y0I.*_gcl_au*MjEwMzM5MzU1NC4xNzMxMzU2ODk5*_ga*MjA0NTM3OTI3MC4xNjU5Mzg0MzAw*_ga_64H0Z0BJSB*MTczMzE2OTAwOS4xNTEuMS4xNzMzMTcwNzc4LjQxLjAuMA.."),
            Pair("Marketing & Communications","https://www.umflint.edu/mac/"),
            Pair("Office of the Dean of Students","https://www.umflint.edu/deanofstudents/"),
            Pair("Online & Digital Education","https://www.umflint.edu/ode/"),
            Pair("Orientation","https://www.umflint.edu/studentsuccess/orientation/"),
            Pair("Procurement","https://procurement.umich.edu/"),
            Pair("Provost","https://www.umflint.edu/provost/"),
            Pair("Public Safety","https://www.umflint.edu/safety/"),
            Pair("Recreation Services","https://www.umflint.edu/rec/"),
            Pair("Registrar","https://www.umflint.edu/registrar/"),
            Pair("Research & Economic Development","https://www.umflint.edu/ored/"),
            Pair("Staff Council","https://www.umflint.edu/staffcouncil/"),
            Pair("Student Affairs","https://www.umflint.edu/dsa/"),
            Pair("Student Government","https://www.umflint.edu/sg/"),
            Pair("Student Success Center","https://www.umflint.edu/studentsuccess/"),
            Pair("Student Veterans Resource Center","https://www.umflint.edu/studentveterans/"),
            Pair("Sustainability","https://www.umflint.edu/sustainability/"),
            Pair("Thompson Center for Learning & Teaching","https://www.umflint.edu/tclt/"),
            Pair("Tutoring","https://www.umflint.edu/studentsuccess/tutoring-supplemental-instruction/"),
            Pair("University Advancement","https://www.umflint.edu/advancement/"),
            Pair("University Human Resources","https://www.umflint.edu/hr/"),
            Pair("Vice Chancellor of Business & Finance","https://www.umflint.edu/business-financial-services/"),
            Pair("Vice Provost for Academic Affairs","https://www.umflint.edu/vice-provost-academic-affairs/"),
            Pair("Writing Center (Marian E. Wright Writing Center)","https://www.umflint.edu/writingcenter/")

        )

        val schoolTable = TableLayout(this@DepartmentInformationActivity)



        addLinksToContainer(schoolTable, schoolsCollegesLinks)
        addLinksToContainer(containerLayout2, officesCentersLinks)
        containerLayout1.addView(schoolTable)
    }

    private fun addLinksToContainer(container: LinearLayout, links: List<Pair<String, String>>) {
        links.forEachIndexed {index, link ->
            val dataRow = TableRow(this)
            val textView = TextView(this)
            textView.text = link.first // Department name
            textView.setTextColor(resources.getColor(android.R.color.white))
            textView.setPadding(32, 32, 32, 32)
            textView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.second))
                startActivity(intent)
            }
            textView.setTextSize(16f)

            val fillerView = TextView(this)
            fillerView.width = LayoutParams.MATCH_PARENT
            fillerView.height = 2
            fillerView.setBackgroundColor(ContextCompat.getColor(this, R.color.college_gold))

            dataRow.addView(textView)
            container.addView(dataRow)
            container.addView(fillerView)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}