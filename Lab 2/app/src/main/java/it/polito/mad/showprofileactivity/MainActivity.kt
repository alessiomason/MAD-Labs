package it.polito.mad.showprofileactivity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var nickname: TextView
    private lateinit var age: TextView
    private lateinit var bio: TextView
    private lateinit var phone: TextView
    private lateinit var location: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var imageUserProfile: ImageView

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
        imageUserProfile = findViewById(R.id.imageUserProfile)

        ratingBar.setIsIndicator(true)
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        name.text = sharedPref.getString("name", "Full Name")
        nickname.text = sharedPref.getString("nickname", "Nickname")
        age.text = sharedPref.getInt("age", 0).toString()
        bio.text = sharedPref.getString("bio", "Bio")
        phone.text = sharedPref.getString("phone", "Phone")
        location.text = sharedPref.getString("location", "Location")
        ratingBar.rating = sharedPref.getFloat("rating", 3.5F)

        ratingBar.setIsIndicator(true)

        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val getPermission = Intent()
                getPermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getPermission)
            }
        }
        val file = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + "user_profile_picture.png"
        )
        imageUserProfile.setImageBitmap(BitmapFactory.decodeFile(file.path))
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name.text = savedInstanceState.getString("name")
        nickname.text = savedInstanceState.getString("nickname")
        bio.text = savedInstanceState.getString("bio")
        age.text = savedInstanceState.getInt("age").toString()
        phone.text = savedInstanceState.getString("phone")
        location.text = savedInstanceState.getString("location")
        ratingBar.rating = savedInstanceState.getFloat("rating")

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