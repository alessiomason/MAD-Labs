package it.polito.mad.showprofileactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SelectSportsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_sports)

        // set onClick for save button
        val saveButton = findViewById<Button>(R.id.saveSportsButton)
        saveButton.setOnClickListener {
            // save selected sports
            finish()
        }
    }
}