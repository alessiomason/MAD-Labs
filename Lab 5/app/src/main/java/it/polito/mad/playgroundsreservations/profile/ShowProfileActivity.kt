package it.polito.mad.playgroundsreservations.profile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.fadeOut
import androidx.core.net.toUri
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Sports
import it.polito.mad.playgroundsreservations.reservations.ReservationsViewModel
import java.io.FileInputStream

class ShowProfileActivity: AppCompatActivity() {
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
        setContentView(R.layout.activity_show_profile)
        this.title = resources?.getString(R.string.user_profile)

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



// Inserisci qui il tuo codice di caricamento...

       // progressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = this.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val gson = Gson()
        profile = gson.fromJson(sharedPref.getString("profile", "{}"), Profile::class.java)
        if (profile.name != null) nameView.text = profile.name
        if (profile.nickname != null) nicknameView.text = profile.nickname
        if (profile.age != null) ageView.text = resources.getString(R.string.years, profile.age.toString())
        if (profile.bio != null) bioView.text = profile.bio
        if (profile.gender == Gender.MALE) genderView.text = resources.getString(R.string.genderMale)
        if (profile.gender == Gender.FEMALE) genderView.text = resources.getString(R.string.genderFemale)
        if (profile.gender == Gender.OTHER) genderView.text = resources.getString(R.string.genderOther)
        if (profile.phone != null) phoneView.text = profile.phone
        if (profile.location != null) locationView.text = profile.location
        if (profile.rating != null) ratingBarView.rating = profile.rating!!
        selectedSports = gson.fromJson(sharedPref.getString("selectedSports", "{}"), SelectedSports::class.java)

        val reservationViewModel by viewModels<ReservationsViewModel>()
        val playgrounds = reservationViewModel.playgrounds

        playgrounds.observe(this) { itemList ->
            itemList?.let {items ->
                for (item in items) {
                    if (item.sport == Sports.BASKETBALL) {
                        val textView = findViewById<TextView>(R.id.my_court_basketball_title)
                        textView.text = item.name
                        val imageView = findViewById<ImageView>(R.id.my_court_basketball_image)
                        imageView.setImageResource(R.drawable.basketball_court)
                        val addressView = findViewById<TextView>(R.id.my_court_basketball_address)
                        addressView.text = item.address
                    } else if (item.sport == Sports.VOLLEYBALL) {
                        val textView = findViewById<TextView>(R.id.my_court_volleyball_title)
                        textView.text = item.name
                        val imageView = findViewById<ImageView>(R.id.my_court_volleyball_image)
                        imageView.setImageResource(R.drawable.volleyball_court)
                        val addressView = findViewById<TextView>(R.id.my_court_volleyball_address)
                        addressView.text = item.address
                    } else if (item.sport == Sports.GOLF) {
                        val textView = findViewById<TextView>(R.id.my_court_golf_title)
                        textView.text = item.name
                        val imageView = findViewById<ImageView>(R.id.my_court_golf_image)
                        imageView.setImageResource(R.drawable.golf_field)
                        val addressView = findViewById<TextView>(R.id.my_court_golf_address)
                        addressView.text = item.address
                    } else if (item.sport == Sports.TENNIS) {
                        val textView = findViewById<TextView>(R.id.my_court_tennis_title)
                        textView.text = item.name
                        val imageView = findViewById<ImageView>(R.id.my_court_tennis_image)
                        imageView.setImageResource(R.drawable.tennis_court)
                        val addressView = findViewById<TextView>(R.id.my_court_tennis_address)
                        addressView.text = item.address
                    } else if (item.sport == Sports.FOOTBALL) {
                        val textView = findViewById<TextView>(R.id.my_court_football_title)
                        textView.text = item.name
                        val imageView = findViewById<ImageView>(R.id.my_court_football_image)
                        imageView.setImageResource(R.drawable.football_pitch)
                        val addressView = findViewById<TextView>(R.id.my_court_football_address)
                        addressView.text = item.address
                    }
                }
            }
        }


        val emptyChip = findViewById<Chip>(R.id.chipEmpty)
        emptyChip.visibility = VISIBLE
        val tennisChip = findViewById<Chip>(R.id.chipTennis)
        if (selectedSports.tennis) {
            tennisChip.visibility = VISIBLE
            emptyChip.visibility = GONE
        } else tennisChip.visibility = GONE
        val basketballChip = findViewById<Chip>(R.id.chipBasketball)
        if (selectedSports.basketball) {
            basketballChip.visibility = VISIBLE
            emptyChip.visibility = GONE
        } else basketballChip.visibility = GONE
        val footballChip = findViewById<Chip>(R.id.chipFootball)
        if (selectedSports.football) {
            footballChip.visibility = VISIBLE
            emptyChip.visibility = GONE
        } else footballChip.visibility = GONE
        val volleyballChip = findViewById<Chip>(R.id.chipVolleyball)
        if (selectedSports.volleyball) {
            volleyballChip.visibility = VISIBLE
            emptyChip.visibility = GONE
        } else volleyballChip.visibility = GONE
        val golfChip = findViewById<Chip>(R.id.chipGolf)
        if (selectedSports.golf) {
            golfChip.visibility = VISIBLE
            emptyChip.visibility = GONE
        } else golfChip.visibility = GONE


        if (profile.userProfileImageUriString != null) {
            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(profile.userProfileImageUriString!!.toUri(), "r", null) ?: return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            userProfileImageView.setImageBitmap(BitmapFactory.decodeStream(inputStream))
            parcelFileDescriptor.close()
        }
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_anim, R.anim.fade_out)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.modify_profile -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.no_anim)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}