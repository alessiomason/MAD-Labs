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
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import java.io.FileDescriptor
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {
    lateinit var name:EditText
    lateinit var nickname:EditText
    lateinit var age:EditText
    lateinit var bio:EditText
    lateinit var phone:EditText
    lateinit var location:EditText
    var imageUserProfile: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        name=findViewById(R.id.editTextFullName)
        nickname=findViewById(R.id.editTextNickname)
        age=findViewById(R.id.editTextAge)
        bio=findViewById(R.id.editTextBio)
        phone=findViewById(R.id.editTextPhone)
        location=findViewById(R.id.editTextLocation)
        imageUserProfile = findViewById(R.id.imageUserProfile)

        //TODO ask for permission of camera upon first launch of application
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
        }

        imageUserProfile?.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 121)
            } else {
                openCamera()
            }
        }
        
        val sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        name.setText(sharedPref.getString("name", "Full Name"))
        nickname.setText(sharedPref.getString("nickname", "Nickname"))
        // age da fare
        bio.setText(sharedPref.getString("bio", "Bio"))
        phone.setText(sharedPref.getString("phone", "Phone"))
        location.setText(sharedPref.getString("location", "Location"))

    }

    private var imageUri: Uri? = null
    private val IMAGE_CAPTURE_CODE = 654

    //TODO opens camera so that user can capture image
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New User Profile Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            //imageUserProfile?.setImageURI(image_uri)
            val bitmap = uriToBitmap(imageUri!!)
            imageUserProfile?.setImageBitmap(bitmap)
        }
    }

    //TODO takes URI of the image and returns bitmap
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

        outState.putString("name",name.text.toString())
        outState.putString("nickname",nickname.text.toString())
        outState.putString("bio",bio.text.toString())
        //outState.putInt("age",age.text.toString().toInt())
        outState.putString("phone",phone.text.toString())
        outState.putString("location",location.text.toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name.setText(savedInstanceState.getString("name"));
        nickname.setText(savedInstanceState.getString("nickname"));
        bio.setText(savedInstanceState.getString("bio"));
        age.setText(savedInstanceState.getString("age"));
        phone.setText(savedInstanceState.getString("phone"));
        location.setText(savedInstanceState.getString("location"));

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
                val sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return true
                with (sharedPref.edit()) {
                    putString("name", name.text.toString())
                    putString("nickname", nickname.text.toString())
                    putString("nickname", nickname.text.toString())
                    putString("bio", bio.text.toString())
                    putString("phone", phone.text.toString())
                    putString("location", location.text.toString())
                    apply()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}