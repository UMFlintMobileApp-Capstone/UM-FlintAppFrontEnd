package com.example.um_flintapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView
import coil.load
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.um_flintapplication.databinding.ActivityMainBinding
import com.example.um_flintapplication.apiRequests.Retrofit
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.ContextCompat



class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var googleSignIn : Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbar.title = "University of Michigan - Flint" // Set the toolbar title here

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Initialize AppBarConfiguration with top-level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_resources_departments, R.id.nav_resources_maps, R.id.nav_announcements, R.id.nav_scheduling_reserve_room, R.id.nav_scheduling_schedule_advisor, R.id.nav_scheduling_group_meetings, R.id.nav_messaging_discord
            ), drawerLayout
        )

        // Set up ActionBar with NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Initialize ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.appBarMain.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        // Add DrawerListener to DrawerLayout and sync state
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to the Home page
                    val intent = Intent(this, MainActivity::class.java) // Replace with your Home activity
                    startActivity(intent)
                    true
                }
//                R.id.nav_resources_academic_calendar -> {
//                    // Navigate to Academic Calendar page
//                    val intent = Intent(this, AcademicCalendarActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
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
//                R.id.nav_scheduling_reserve_room -> {
//                    // Navigate to Reserve Room page
//                    val intent = Intent(this, ReserveRoomActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
                R.id.nav_announcements -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, AlertsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

//        //Begin News
//        CoroutineScope(Dispatchers.IO).launch {
//                val items = Retrofit.api.getNews(3)
//
//                withContext(Dispatchers.Main) {
//                    val layout = findViewById<LinearLayout>(R.id.NewsSection)
//
//                    items.forEach { item ->
//                        val textView = TextView(this@MainActivity)
//
//                        textView.text = item.title
//                        textView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
//                        textView.setPadding(0, 8, 0, 0)
//
//                        val layoutParams = LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT, // Width
//                            LinearLayout.LayoutParams.WRAP_CONTENT  // Height
//                        )
//                        textView.layoutParams = layoutParams
//
//                        layout.addView(textView)
//                    }
//                }
//            }

//        //Begin events (NO IMAGE) !! TEMPORARY !!
//        CoroutineScope(Dispatchers.IO).launch{
//            val events = Retrofit.api.getEvents(3)
//
//            withContext(Dispatchers.Main){
//                val layout = findViewById<LinearLayout>(R.id.EventsSection)
//
//                events.forEach{ item ->
//                    val textview = TextView(this@MainActivity)
//                    textview.text = item.title
//                    textview.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
//                    textview.setPadding(0, 8, 0, 0)
//
//                    val layoutParams = LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT, // Width
//                            LinearLayout.LayoutParams.WRAP_CONTENT  // Height
//                    )
//                    textview.layoutParams = layoutParams
//
//                    layout.addView(textview)
//
//
//                }
//            }
//        }

//        Begin events (WITH IMAGE) !! TO DO !!
//        CoroutineScope(Dispatchers.IO).launch{
//            val events = Retrofit.api.getEvents(3)
//
//            withContext(Dispatchers.Main){
//                val layout = findViewById<LinearLayout>(R.id.EventsSection)
//
//                events.forEach{item ->
//                    val bgImage: String = item.photo
//
//                    val imgview = ImageView(this@MainActivity)
//                    imgview.setPadding(0, 8, 0, 0)
//                    imgview.layoutParams = LinearLayout.LayoutParams(0, 80)
//                    imgview.load(bgImage){}
//                    layout.addView(imgview)
//                }
//            }
//        }

//        <View-->
//        <!--                        android:layout_width="0dp"-->
//        <!--                        android:layout_height="80dp"-->
//        <!--                        android:layout_weight="1"-->
//        <!--                        android:background="@color/maize"-->
//        <!--                        android:padding="8dp"-->
//        <!--                        android:onClick="openEvent1"/>-->

        // Basically to sign in you have to create an instance of the Auth class, making sure to
        // pass the activity to it (via 'this').
        // You can then call the login function, which has a callback that will give you the JWT
        // token.
        //
        // If the user has already logged in, then the JWT token will be passed automatically.
        // Else it will try to login the user automatically (given they have a umich.edu account
        // signed in.)
        //
        // You can see here I'm just displaying in the logs what the token is for now. Need to make
        // it so it fetches the user's name for instance here.
        googleSignIn = Auth(this)

        var signInButton = findViewById<LinearLayout>(R.id.SignIn)
        signInButton.setOnClickListener{
            googleSignIn.login { cred -> Log.d(TAG, "Token is $cred")}
        }

        googleSignIn.silentLogin { cred ->
            if(cred!=null){
                Toast.makeText(this, "Logged in silently!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Override onOptionsItemSelected to toggle the drawer when the hamburger icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun openAlertsPage(view: View) {
        val intent = Intent(this, AlertsActivity::class.java)
        startActivity(intent)
    }

    // Function to open the Sign In page
    fun openSignInPage(view: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    // Function to open the Events page
    fun openEventsPage(view: View) {
        val intent = Intent(this, EventsActivity::class.java)
        startActivity(intent)
    }

    // Function to open the News page
    fun openNewsPage(view: View) {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }

    // Function to open the Maps page
    fun openMapsPage(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
