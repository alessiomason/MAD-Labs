package it.polito.mad.playgroundsreservations.profile

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RatingBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.User
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import java.io.*
import it.polito.mad.playgroundsreservations.Global

class EditProfileActivity: AppCompatActivity() {
    private lateinit var profile: Profile
    private lateinit var selectedSports: SelectedSports
    private lateinit var nameView: EditText
    private lateinit var nicknameView: EditText
    private lateinit var ageView: EditText
    private lateinit var bioView: EditText
    private lateinit var genderMaleRadioButton: RadioButton
    private lateinit var genderFemaleRadioButton: RadioButton
    private lateinit var genderOtherRadioButton: RadioButton
    private lateinit var phoneView: EditText
    private lateinit var locationView: EditText
    private lateinit var ratingBarView: RatingBar
    private lateinit var userProfileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        this.title = resources?.getString(R.string.edit_profile)

        nameView = findViewById(R.id.editTextFullName)
        nicknameView = findViewById(R.id.editTextNickname)
        ageView = findViewById(R.id.editTextAge)
        bioView = findViewById(R.id.editTextBio)
        genderMaleRadioButton = findViewById(R.id.radioGenderMale)
        genderFemaleRadioButton = findViewById(R.id.radioGenderFemale)
        genderOtherRadioButton = findViewById(R.id.radioGenderOther)
        phoneView = findViewById(R.id.editTextPhone)
        locationView = findViewById(R.id.editTextLocation)
        ratingBarView = findViewById(R.id.ratingBar)
        userProfileImageView = findViewById(R.id.imageUserProfile)

        registerForContextMenu(userProfileImageView)
        userProfileImageView.setOnClickListener {
            userProfileImageView.showContextMenu()
        }

        val viewModel by viewModels<ViewModel>()
        val sharedPref = this.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val gson = Gson()
        profile = gson.fromJson(sharedPref.getString("profile", "{}"), Profile::class.java)

        viewModel.getUserInfo(Global.userId!!).observe(this) { user ->
            if (user != null) {
                nameView.setText(user.fullName)
                nicknameView.setText(user.username)
                bioView.setText(user.bio)
                when (user.gender) {
                    Gender.MALE -> {
                        genderMaleRadioButton.isChecked = true
                        genderFemaleRadioButton.isChecked = false
                        genderOtherRadioButton.isChecked = false
                    }
                    Gender.FEMALE -> {
                        genderMaleRadioButton.isChecked = false
                        genderFemaleRadioButton.isChecked = true
                        genderOtherRadioButton.isChecked = false
                    }
                    Gender.OTHER -> {
                        genderMaleRadioButton.isChecked = false
                        genderFemaleRadioButton.isChecked = false
                        genderOtherRadioButton.isChecked = true
                    }
                    null -> {
                        genderMaleRadioButton.isChecked = false
                        genderFemaleRadioButton.isChecked = false
                        genderOtherRadioButton.isChecked = false
                    }
                }
                // ageView.setText(resources.getString(R.string.years, calculateAge(user.dateOfBirth).toString()))
                ageView.setText(resources.getString(R.string.years, user.dateOfBirth))
                phoneView.setText(user.phone)
                locationView.setText(user.location)
                ratingBarView.rating = user.rating
            }
        }


        // set onClick for Add new sports button
        val addChip = findViewById<Chip>(R.id.chipAdd)
        addChip.setOnClickListener {
            val intent = Intent(this, SelectSportsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.no_anim)
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = this.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val gson = Gson()
        selectedSports = gson.fromJson(sharedPref.getString("selectedSports", "{}"), SelectedSports::class.java)

        val tennisChip = findViewById<Chip>(R.id.chipTennis)
        if (selectedSports.tennis) tennisChip.visibility = View.VISIBLE else tennisChip.visibility = View.GONE
        val basketballChip = findViewById<Chip>(R.id.chipBasketball)
        if (selectedSports.basketball) basketballChip.visibility = View.VISIBLE else basketballChip.visibility = View.GONE
        val footballChip = findViewById<Chip>(R.id.chipFootball)
        if (selectedSports.football) footballChip.visibility = View.VISIBLE else footballChip.visibility = View.GONE
        val volleyballChip = findViewById<Chip>(R.id.chipVolleyball)
        if (selectedSports.volleyball) volleyballChip.visibility = View.VISIBLE else volleyballChip.visibility = View.GONE
        val golfChip = findViewById<Chip>(R.id.chipGolf)
        if (selectedSports.golf) golfChip.visibility = View.VISIBLE else golfChip.visibility = View.GONE

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

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gallery -> {
                selectFromCamera()
                true
            }
            R.id.camera -> {
                while (ContextCompat.checkSelfPermission(applicationContext, "android.permission.CAMERA") ==
                    PERMISSION_DENIED)
                    requestPermissions(arrayOf("android.permission.CAMERA"), 102)
                openCamera()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654

    // opens camera so that user can capture image
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New User Profile Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        profile.userProfileImageUriString = imageUri.toString()
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    private fun selectFromCamera() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap? = null

        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            bitmap = uriToBitmap(profile.userProfileImageUriString!!.toUri())
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            profile.userProfileImageUriString = data.data.toString()
            bitmap = uriToBitmap(profile.userProfileImageUriString!!.toUri())
        }

        userProfileImageView.setImageBitmap(bitmap)
    }

    // takes URI of the image and returns bitmap
    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("userProfileImageUriString", profile.userProfileImageUriString)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        profile.userProfileImageUriString = savedInstanceState.getString("userProfileImageUriString")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val reservationViewModel by viewModels<ViewModel>()
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.save_profile -> {
                finish()
                overridePendingTransition(R.anim.no_anim, R.anim.fade_out)
                val sharedPref = this.getSharedPreferences(
                    "profile",
                    Context.MODE_PRIVATE
                ) ?: return true
                with(sharedPref.edit()) {
                    val gson = Gson()
                    val profileJson = gson.toJson(profile)
                    putString("profile", profileJson)
                    apply()
                }
                var gender: Gender = Gender.MALE
                if (genderMaleRadioButton.isChecked) {
                    gender = Gender.MALE
                } else if (genderFemaleRadioButton.isChecked) {
                    gender = Gender.FEMALE
                } else if (genderOtherRadioButton.isChecked) {
                    gender = Gender.OTHER
                }
                val user = User(
                    id = Global.userId!!,
                    username = nicknameView.text.toString(),
                    fullName = nameView.text.toString(),
                    bio = bioView.text.toString(),
                    gender = gender,
                    phone = phoneView.text.toString(),
                    location = locationView.text.toString(),
                    rating = ratingBarView.rating,
                    // dateBirth da modificare con la data
                    dateOfBirth = ageView.text.split(" ")[0]
                )
                reservationViewModel.updateUserInfo(user)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}