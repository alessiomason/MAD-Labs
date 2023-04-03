package it.polito.mad.showprofileactivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import com.google.gson.Gson

data class SelectedSports(
    var tennis: Boolean,
    var basketball: Boolean,
    var football: Boolean,
    var volleyball: Boolean,
    var golf: Boolean
)

class SelectSportsActivity : AppCompatActivity() {
    private lateinit var selectedSports: SelectedSports
    private lateinit var tennisCb: CheckBox
    private lateinit var basketballCb: CheckBox
    private lateinit var footballCb: CheckBox
    private lateinit var volleyballCb: CheckBox
    private lateinit var golfCb: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_sports)

        tennisCb = findViewById(R.id.checkBoxTennis)
        basketballCb = findViewById(R.id.checkBoxBasketball)
        footballCb = findViewById(R.id.checkBoxFootball)
        volleyballCb = findViewById(R.id.checkBoxVolleyball)
        golfCb = findViewById(R.id.checkBoxGolf)

        val sharedPref = this.getSharedPreferences("selectedSports", Context.MODE_PRIVATE)
        val gson = Gson()
        selectedSports = gson.fromJson(sharedPref.getString("selectedSports", "{}"), SelectedSports::class.java)

        tennisCb.isChecked = selectedSports.tennis
        basketballCb.isChecked = selectedSports.basketball
        footballCb.isChecked = selectedSports.football
        volleyballCb.isChecked = selectedSports.volleyball
        golfCb.isChecked = selectedSports.golf

        // set onClick for save button
        val saveButton = findViewById<Button>(R.id.saveSportsButton)
        saveButton.setOnClickListener {
            // save selected sports
            if (tennisCb.isChecked) selectedSports.tennis = true
            if (basketballCb.isChecked) selectedSports.basketball = true
            if (footballCb.isChecked) selectedSports.football = true
            if (volleyballCb.isChecked) selectedSports.volleyball = true
            if (golfCb.isChecked) selectedSports.golf = true

            finish()

            with(sharedPref.edit()) {
                val selectedSportsJson = gson.toJson(selectedSports)
                putString("selectedSports", selectedSportsJson)
                apply()
            }
        }
    }
}