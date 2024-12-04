package com.example.um_flintapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout.LayoutParams
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.um_flintapplication.apiRequests.AcademicEvents
import com.example.um_flintapplication.apiRequests.Retrofit
import com.example.um_flintapplication.databinding.ActivityAcademicCalendarBinding
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonElement
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AcademicCalendar : AppCompatActivity() {

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
        val sems: MutableList<String> = mutableListOf("Select a Term")
        val spinner: Spinner = findViewById(R.id.SemesterDropDown)
        CoroutineScope(Dispatchers.IO).launch {
            var acadEvents: AcademicEvents? = null

            Retrofit(this@AcademicCalendar).api.getAcadEvents().onSuccess {
                acadEvents = data
            }
            withContext(Dispatchers.Main){
                for (jsonElement: JsonElement in acadEvents!!.data){
                    val jsonObj = jsonElement.asJsonObject
                    sems.add(jsonObj["name"].asString)
                }

                val adapter = ArrayAdapter(
                    this@AcademicCalendar,
                    android.R.layout.simple_spinner_item,
                    sems
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                spinner.onItemSelectedListener

            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val sem = sems[p2]
                    val tableData: MutableList<List<String>> = mutableListOf()
                    val table = findViewById<TableLayout>(R.id.acadTableLayout)

                    for (semester: JsonElement in acadEvents!!.data) {
                        val jsonObj = semester.asJsonObject
                        if (sem == jsonObj["name"].asString) {
                            table.removeAllViews() // only clear if term is found
                            // loop through terms for chosen term
                            for (terms: JsonElement in jsonObj["terms"].asJsonArray) {
                                tableData.clear()
                                tableData.add(listOf(terms.asJsonObject["name"].asString)) // start list off with term header
                                val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                val headerDateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                                val termStartDate = LocalDateTime.parse(terms.asJsonObject["start_at"].asString, dateTimeFormatter).format(headerDateTimeFormatter)
                                val termEndDate = LocalDateTime.parse(terms.asJsonObject["end_at"].asString, dateTimeFormatter).format(headerDateTimeFormatter)
                                tableData.add(listOf("$termStartDate - $termEndDate"))
                                // loop through each event
                                for (events: JsonElement in terms.asJsonObject["events"].asJsonArray) {
                                    val eventsObj = events.asJsonObject

                                    // you actually have to do it this way, i dont know why
                                    var date = LocalDateTime.parse(eventsObj["start_at"].asString, dateTimeFormatter) // yuck
                                    val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a")
                                    val startDate = date.format(formatter)

                                    if (eventsObj["end_at"].asString.isNotEmpty()) {
                                        date = LocalDateTime.parse(eventsObj["end_at"].asString, dateTimeFormatter) // yuck
                                        val endDate = date.format(formatter)
                                        tableData.add(
                                            listOf(
                                                eventsObj["title"].asString,
                                                "$startDate - $endDate"
                                            )
                                        )
                                    } else {
                                        tableData.add(
                                            listOf(
                                                eventsObj["title"].asString,
                                                startDate
                                            )
                                        )
                                    }
                                }
                                tableData.forEachIndexed() {index, rowData ->
                                    val row = TableRow(this@AcademicCalendar)
                                    if (index % 2 == 0){
                                        row.setBackgroundColor(ContextCompat.getColor(this@AcademicCalendar, R.color.lightgray))
                                    }
                                    else {
                                        row.setBackgroundColor(ContextCompat.getColor(this@AcademicCalendar, R.color.gray))
                                    }
                                    row.layoutParams = TableRow.LayoutParams(0,LayoutParams.WRAP_CONTENT)

                                    // columns
                                    for (columnData in rowData) {
                                        val textView = TextView(this@AcademicCalendar)

                                        textView.text = columnData
                                        textView.layoutParams = TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, .5f)
                                        textView.setPadding(8,8,8,8)
                                        textView.setTextSize(16f)

                                        if (index == 0) {
                                            textView.gravity = Gravity.CENTER
                                            textView.setTypeface(null, android.graphics.Typeface.BOLD)
                                            textView.setTextSize(20f)
                                            textView.setBackgroundColor(ContextCompat.getColor(this@AcademicCalendar, R.color.college_blue))
                                            textView.setTextColor(ContextCompat.getColor(this@AcademicCalendar, R.color.white))
                                        }
                                        else if (index == 1) {
                                            textView.gravity = Gravity.CENTER
                                            textView.setTypeface(null, android.graphics.Typeface.BOLD)
                                            textView.setTextSize(16f)
                                            textView.setBackgroundColor(ContextCompat.getColor(this@AcademicCalendar, R.color.college_blue))
                                            textView.setTextColor(ContextCompat.getColor(this@AcademicCalendar, R.color.white))
                                        }
                                        else textView.setTextColor(ContextCompat.getColor(this@AcademicCalendar, R.color.black))
                                        row.addView(textView)
                                    }
                                    table.addView(row)
                                }
                                table.visibility = View.VISIBLE
                            }
                        }
                    }
                    }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

    //ArrayAdapter.createFromResource()



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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

