package it.polito.mad.showprofileactivity


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var nickname: TextView
    lateinit var age: TextView
    lateinit var bio: TextView
    lateinit var phone: TextView
    lateinit var location: TextView
    lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        name = findViewById(R.id.textFullName)
        nickname = findViewById(R.id.textNickname)
        age = findViewById(R.id.textAge)
        bio = findViewById(R.id.textBio)
        phone = findViewById(R.id.textPhone)
        location = findViewById(R.id.textLocation)
        ratingBar = findViewById(R.id.ratingBar)
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        name.setText(sharedPref.getString("name", "Full Name"))
        nickname.setText(sharedPref.getString("nickname", "Nickname"))
        // age da fare
        bio.setText(sharedPref.getString("bio", "Bio"))
        phone.setText(sharedPref.getString("phone", "Phone"))
        location.setText(sharedPref.getString("location", "Location"))


    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name.setText(savedInstanceState.getString("name"))
        nickname.setText(savedInstanceState.getString("nickname"))
        bio.setText(savedInstanceState.getString("bio"));
        // age.setText(savedInstanceState.getString("age"));
        phone.setText(savedInstanceState.getString("phone"));
        location.setText(savedInstanceState.getString("location"));

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.modify_profile -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                // start your next activity
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}