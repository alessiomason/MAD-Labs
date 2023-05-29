package it.polito.mad.playgroundsreservations.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import it.polito.mad.playgroundsreservations.Global
import it.polito.mad.playgroundsreservations.MainActivity
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import java.io.InputStream


class ShowProfileActivity: AppCompatActivity() {
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

        findViewById<Button>(R.id.logout_button).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Global.userId = null

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.no_anim)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = this.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val gson = Gson()
        selectedSports = gson.fromJson(sharedPref.getString("selectedSports", "{}"), SelectedSports::class.java)

        val viewModel by viewModels<ViewModel>()
        val playgrounds = viewModel.playgrounds

        viewModel.getUserInfo(Global.userId!!).observe(this) { user ->
            if (user != null) {
                nameView.text = user.fullName
                nicknameView.text = user.username
                bioView.text = user.bio
                genderView.text = when (user.gender) {
                    Gender.MALE -> resources.getString(R.string.genderMale)
                    Gender.FEMALE -> resources.getString(R.string.genderFemale)
                    Gender.OTHER -> resources.getString(R.string.genderOther)
                    null -> ""
                }

                // ageView.text = resources.getString(R.string.years, calculateAge(user.dateOfBirth).toString())
                ageView.text = resources.getString(R.string.years, user.dateOfBirth)
                phoneView.text = user.phone
                locationView.text = user.location
                ratingBarView.rating = user.rating


                val storageReference = Firebase.storage.reference.child("profileImages/${user.id}")

                Glide.with(this)
                    .load(storageReference)
                    .placeholder(R.drawable.user_profile)
                    .into(userProfileImageView)
            }
        }

        playgrounds.observe(this) { itemList ->
            itemList?.let {items ->
                for (item in items) {
                    when (item.sport) {
                        Sport.BASKETBALL -> {
                            val textView = findViewById<TextView>(R.id.my_court_basketball_title)
                            textView.text = item.name
                            val imageView = findViewById<ImageView>(R.id.my_court_basketball_image)
                            imageView.setImageResource(R.drawable.basketball_court)
                            val addressView =
                                findViewById<TextView>(R.id.my_court_basketball_address)
                            addressView.text = item.address
                        }
                        Sport.VOLLEYBALL -> {
                            val textView = findViewById<TextView>(R.id.my_court_volleyball_title)
                            textView.text = item.name
                            val imageView = findViewById<ImageView>(R.id.my_court_volleyball_image)
                            imageView.setImageResource(R.drawable.volleyball_court)
                            val addressView =
                                findViewById<TextView>(R.id.my_court_volleyball_address)
                            addressView.text = item.address
                        }
                        Sport.GOLF -> {
                            val textView = findViewById<TextView>(R.id.my_court_golf_title)
                            textView.text = item.name
                            val imageView = findViewById<ImageView>(R.id.my_court_golf_image)
                            imageView.setImageResource(R.drawable.golf_field)
                            val addressView = findViewById<TextView>(R.id.my_court_golf_address)
                            addressView.text = item.address
                        }
                        Sport.TENNIS -> {
                            val textView = findViewById<TextView>(R.id.my_court_tennis_title)
                            textView.text = item.name
                            val imageView = findViewById<ImageView>(R.id.my_court_tennis_image)
                            imageView.setImageResource(R.drawable.tennis_court)
                            val addressView = findViewById<TextView>(R.id.my_court_tennis_address)
                            addressView.text = item.address
                        }
                        Sport.FOOTBALL -> {
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

        /*
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(profile.userProfileImageUriString!!.toUri(), "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        userProfileImageView.setImageBitmap(BitmapFactory.decodeStream(inputStream))
        parcelFileDescriptor.close()
        */
        // Reference to an image file in Cloud Storage
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

// class to download images from the Firebase storage
@GlideModule
class MyAppGlideModule: AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}

/*
fun calculateAge(dateOfBirth: String): Int {

    val dateParts = dateOfBirth.split("/")
    val yearBirth = dateParts[0].toInt()
    val monthBirth = dateParts[1].toInt()
    val dayBirth = dateParts[2].toInt()
    val currentDate = Calendar.getInstance()
    val currentYear = currentDate.get(Calendar.YEAR)
    val currentMonth = currentDate.get(Calendar.MONTH) + 1  // I mesi in Calendar vanno da 0 a 11, quindi aggiungi 1
    val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
    var age = currentYear - yearBirth
    if (currentMonth < monthBirth || (currentMonth == monthBirth && currentDay < dayBirth))
        age--

    return age
} */
