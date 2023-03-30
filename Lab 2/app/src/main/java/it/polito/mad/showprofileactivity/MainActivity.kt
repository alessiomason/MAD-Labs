package it.polito.mad.showprofileactivity


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var nickname: TextView
    lateinit var age: TextView
    lateinit var bio: TextView
    lateinit var phone: TextView
    lateinit var location: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        name=findViewById(R.id.textFullName)
        nickname=findViewById(R.id.textNickname)
        age=findViewById(R.id.textAge)
        bio=findViewById(R.id.textBio)
        phone=findViewById(R.id.textPhone)
        location=findViewById(R.id.textLocation)

        if(savedInstanceState!=null)
        {
            name.setText(savedInstanceState.getString("name"))
            nickname.setText(savedInstanceState.getString("nickname"))
            age.setText(savedInstanceState.getString("age"))
            bio.setText(savedInstanceState.getString("bio"))
            phone.setText(savedInstanceState.getString("phone"))
            location.setText(savedInstanceState.getString("location"))

        }
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
                val outState = Bundle();
                outState?.putString("name",name.text.toString())
                outState?.putString("nickname",nickname.text.toString())
                outState?.putString("bio",bio.text.toString())
                outState?.putString("age",age.text.toString())
                outState?.putString("phone",phone.text.toString())
                outState?.putString("location",location.text.toString())
                intent.putExtras(outState);
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}