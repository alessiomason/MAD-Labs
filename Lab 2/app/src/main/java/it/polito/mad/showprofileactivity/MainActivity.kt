package it.polito.mad.showprofileactivity


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        /*  val bundle = intent.extras
          if(bundle!=null)
          {
              name.setText(bundle.getString("name"))
             nickname.setText(savedInstanceState.getString("nickname"))
             age.setText(savedInstanceState.getInt("age"))
              bio.setText(savedInstanceState.getString("bio"))
              phone.setText(savedInstanceState.getString("phone"))
              location.setText(savedInstanceState.getString("location"))

          }*/
    }

    override fun onResume() {
        super.onResume()
        val bundle = intent.extras
        if(bundle!=null)
        {
            name.setText(bundle.getString("name"))
            /*   nickname.setText(savedInstanceState.getString("nickname"))
             //  age.setText(savedInstanceState.getInt("age"))
               bio.setText(savedInstanceState.getString("bio"))
               phone.setText(savedInstanceState.getString("phone"))
               location.setText(savedInstanceState.getString("location"))*/

        }
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name.setText(savedInstanceState.getString("name"));
       /* nickname.setText(savedInstanceState.getString("nickname"));
        bio.setText(savedInstanceState.getString("bio"));
        age.setText(savedInstanceState.getString("age"));
        phone.setText(savedInstanceState.getString("phone"));
        location.setText(savedInstanceState.getString("location"));*/

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