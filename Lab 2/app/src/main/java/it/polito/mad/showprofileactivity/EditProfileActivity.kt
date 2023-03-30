package it.polito.mad.showprofileactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText

class EditProfileActivity : AppCompatActivity() {
    lateinit var name:EditText
    lateinit var nickname:EditText
    lateinit var age:EditText
    lateinit var bio:EditText
    lateinit var phone:EditText
    lateinit var location:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        name=findViewById(R.id.editTextFullName)
        nickname=findViewById(R.id.editTextNickname)
        age=findViewById(R.id.editTextAge)
        bio=findViewById(R.id.editTextBio)
        phone=findViewById(R.id.editTextPhone)
        location=findViewById(R.id.editTextLocation)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState?.putString("name",name.text.toString())
        outState?.putString("nickname",nickname.text.toString())
        outState?.putString("bio",bio.text.toString())
        outState?.putInt("age",age.text.toString().toInt())
        outState?.putString("phone",phone.text.toString())
        outState?.putString("location",location.text.toString())

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
                val intent = Intent(this, MainActivity::class.java)

                val outState = Bundle();
                outState?.putString("name",name.text.toString())
           /*     outState?.putString("nickname",nickname.text.toString())
                outState?.putString("bio",bio.text.toString())
                //outState?.putInt("age",age.text.toString().toInt())
                outState?.putString("phone",phone.text.toString())
                outState?.putString("location",location.text.toString())*/
                intent.putExtras(outState);
                // start your next activity
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}