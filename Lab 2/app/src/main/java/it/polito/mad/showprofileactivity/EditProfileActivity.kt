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
import java.io.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var nickname: EditText
    private lateinit var age: EditText
    private lateinit var bio: EditText
    private lateinit var phone: EditText
    private lateinit var location: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var imageUserProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        name = findViewById(R.id.editTextFullName)
        nickname = findViewById(R.id.editTextNickname)
        age = findViewById(R.id.editTextAge)
        bio = findViewById(R.id.editTextBio)
        phone = findViewById(R.id.editTextPhone)
        location = findViewById(R.id.editTextLocation)
        ratingBar = findViewById(R.id.ratingBar)
        imageUserProfile = findViewById(R.id.imageUserProfile)

        registerForContextMenu(imageUserProfile)
        imageUserProfile.setOnClickListener {
            imageUserProfile.showContextMenu()
        }

        val sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        name.setText(sharedPref.getString("name", "Full Name"))
        nickname.setText(sharedPref.getString("nickname", "Nickname"))
        // age da fare
        bio.setText(sharedPref.getString("bio", "Bio"))
        phone.setText(sharedPref.getString("phone", "Phone"))
        location.setText(sharedPref.getString("location", "Location"))
        ratingBar.rating = sharedPref.getFloat("rating", 3.5F)

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

        // set onClick for Add new sports button
        val addChip = findViewById<Chip>(R.id.chipAdd)
        addChip.setOnClickListener {
            val intent = Intent(this, SelectSportsActivity::class.java)
            startActivity(intent)
        }
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

        imageUserProfile.setImageBitmap(bitmap)

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

        outState.putString("name", name.text.toString())
        outState.putString("nickname", nickname.text.toString())
        outState.putString("bio", bio.text.toString())
        //outState.putInt("age",age.text.toString().toInt())
        outState.putString("phone", phone.text.toString())
        outState.putString("location", location.text.toString())
        outState.putFloat("rating", ratingBar.rating)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name.setText(savedInstanceState.getString("name"))
        nickname.setText(savedInstanceState.getString("nickname"))
        bio.setText(savedInstanceState.getString("bio"))
        age.setText(savedInstanceState.getString("age"))
        phone.setText(savedInstanceState.getString("phone"))
        location.setText(savedInstanceState.getString("location"))
        ratingBar.rating = savedInstanceState.getFloat("rating")

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
                //    val intent = Intent(this, MainActivity::class.java)

                // val outState = Bundle();
                //  outState.putString("name",name.text.toString())
                /*     outState?.putString("nickname",nickname.text.toString())
                outState?.putString("bio",bio.text.toString())
                //outState?.putInt("age",age.text.toString().toInt())
                outState?.putString("phone",phone.text.toString())
                outState?.putString("location",location.text.toString())*/
                //intent.putExtras(outState);
                // start your next activity

                // startActivity(intent)

                finish()
                val sharedPref = this.getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                ) ?: return true
                with(sharedPref.edit()) {
                    putString("name", name.text.toString())
                    putString("nickname", nickname.text.toString())
                    putString("nickname", nickname.text.toString())
                    putString("bio", bio.text.toString())
                    putString("phone", phone.text.toString())
                    putString("location", location.text.toString())
                    putFloat("rating", ratingBar.rating)
                    apply()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}