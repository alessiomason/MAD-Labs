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
import android.view.View.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var profile: Profile
    private lateinit var selectedSports: SelectedSports
    private lateinit var nameView: TextView
    private lateinit var nicknameView: TextView
    private lateinit var ageView: TextView
    private lateinit var bioView: TextView
    private lateinit var genderView: TextView
    private lateinit var phoneView: TextView
    private lateinit var locationView: TextView
    private lateinit var ratingBarView: RatingBar
    private lateinit var userProfileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameView = findViewById(R.id.textFullName)
        nicknameView = findViewById(R.id.textNickname)
        ageView = findViewById(R.id.textAge)
        bioView = findViewById(R.id.textBio)
        genderView = findViewById(R.id.textGender)
        phoneView = findViewById(R.id.textPhone)
        locationView = findViewById(R.id.textLocation)
        ratingBarView = findViewById(R.id.ratingBar)
        userProfileImageView = findViewById(R.id.imageUserProfile)
        ratingBarView.setIsIndicator(true)
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = this.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val gson = Gson()
        profile = gson.fromJson(sharedPref.getString("profile", "{}"), Profile::class.java)
        if (profile.name != null) nameView.text = profile.name
        if (profile.nickname != null) nicknameView.text = profile.nickname
        if (profile.age != null) ageView.text = profile.age.toString()
        if (profile.bio != null) bioView.text = profile.bio
        if (profile.gender == Gender.MALE) genderView.text = resources.getString(R.string.genderMale)
        if (profile.gender == Gender.FEMALE) genderView.text = resources.getString(R.string.genderFemale)
        if (profile.gender == Gender.OTHER) genderView.text = resources.getString(R.string.genderOther)
        if (profile.phone != null) phoneView.text = profile.phone
        if (profile.location != null) locationView.text = profile.location
        if (profile.rating != null) ratingBarView.rating = profile.rating!!
        selectedSports = gson.fromJson(sharedPref.getString("selectedSports", "{}"), SelectedSports::class.java)

        val tennisChip = findViewById<Chip>(R.id.chipTennis)
        if (selectedSports.tennis) tennisChip.visibility = VISIBLE else tennisChip.visibility = GONE
        val basketballChip = findViewById<Chip>(R.id.chipBasketball)
        if (selectedSports.basketball) basketballChip.visibility = VISIBLE else basketballChip.visibility = GONE
        val footballChip = findViewById<Chip>(R.id.chipFootball)
        if (selectedSports.football) footballChip.visibility = VISIBLE else footballChip.visibility = GONE
        val volleyballChip = findViewById<Chip>(R.id.chipVolleyball)
        if (selectedSports.volleyball) volleyballChip.visibility = VISIBLE else volleyballChip.visibility = GONE
        val golfChip = findViewById<Chip>(R.id.chipGolf)
        if (selectedSports.golf) golfChip.visibility = VISIBLE else golfChip.visibility = GONE

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
        userProfileImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
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