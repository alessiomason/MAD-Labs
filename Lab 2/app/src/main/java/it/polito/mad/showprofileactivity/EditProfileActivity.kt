package it.polito.mad.showprofileactivity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import java.io.*

class EditProfileActivity: AppCompatActivity() {
    private lateinit var profile: Profile
    private lateinit var selectedSports: SelectedSports
    private lateinit var nameView: EditText
    private lateinit var nicknameView: EditText
    private lateinit var ageView: EditText
    private lateinit var bioView: EditText
    private lateinit var phoneView: EditText
    private lateinit var locationView: EditText
    private lateinit var ratingBarView: RatingBar
    private lateinit var userProfileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        nameView = findViewById(R.id.editTextFullName)
        nicknameView = findViewById(R.id.editTextNickname)
        ageView = findViewById(R.id.editTextAge)
        bioView = findViewById(R.id.editTextBio)
        phoneView = findViewById(R.id.editTextPhone)
        locationView = findViewById(R.id.editTextLocation)
        ratingBarView = findViewById(R.id.ratingBar)
        userProfileImageView = findViewById(R.id.imageUserProfile)

        registerForContextMenu(userProfileImageView)
        userProfileImageView.setOnClickListener {
            userProfileImageView.showContextMenu()
        }

        val sharedPref = this.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val gson = Gson()
        profile = gson.fromJson(sharedPref.getString("profile", "{}"), Profile::class.java)
        if (profile.name != null) nameView.setText(profile.name)
        if (profile.nickname != null) nicknameView.setText(profile.nickname)
        if (profile.age != null) ageView.setText(profile.age.toString())
        if (profile.bio != null) bioView.setText(profile.bio)
        if (profile.phone != null) phoneView.setText(profile.phone)
        if (profile.location != null) locationView.setText(profile.location)
        if (profile.rating != null) ratingBarView.rating = profile.rating!!

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

        // set onClick for Add new sports button
        val addChip = findViewById<Chip>(R.id.chipAdd)
        addChip.setOnClickListener {
            val intent = Intent(this, SelectSportsActivity::class.java)
            startActivity(intent)
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
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, 112)
                } else
                    openCamera()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654

    // opens camera so that user can capture image
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New User Profile Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
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
            bitmap = uriToBitmap(imageUri!!)
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            bitmap = uriToBitmap(imageUri!!)
        }

        userProfileImageView.setImageBitmap(bitmap)

        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val getPermission = Intent()
                getPermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getPermission)
            }
        }
        bitmapToFile(bitmap!!, "user_profile_picture.png")
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

    private fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()

            // Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapData = bos.toByteArray()

            // write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("name", nameView.text.toString())
        outState.putString("nickname", nicknameView.text.toString())
        outState.putString("bio", bioView.text.toString())
        outState.putInt("age", ageView.text.toString().toInt())
        outState.putString("phone", phoneView.text.toString())
        outState.putString("location", locationView.text.toString())
        outState.putFloat("rating", ratingBarView.rating)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nameView.setText(savedInstanceState.getString("name"))
        nicknameView.setText(savedInstanceState.getString("nickname"))
        bioView.setText(savedInstanceState.getString("bio"))
        ageView.setText(savedInstanceState.getInt("age").toString())
        phoneView.setText(savedInstanceState.getString("phone"))
        locationView.setText(savedInstanceState.getString("location"))
        ratingBarView.rating = savedInstanceState.getFloat("rating")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.save_profile -> {
                finish()
                val sharedPref = this.getSharedPreferences(
                    "profile",
                    Context.MODE_PRIVATE
                ) ?: return true
                with(sharedPref.edit()) {
                    profile.name = nameView.text.toString()
                    profile.nickname = nicknameView.text.toString()
                    profile.age = ageView.text.toString().toIntOrNull()
                    profile.bio = bioView.text.toString()
                    profile.phone = phoneView.text.toString()
                    profile.location = locationView.text.toString()
                    profile.rating = ratingBarView.rating

                    val gson = Gson()
                    val profileJson = gson.toJson(profile)
                    putString("profile", profileJson)
                    apply()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}