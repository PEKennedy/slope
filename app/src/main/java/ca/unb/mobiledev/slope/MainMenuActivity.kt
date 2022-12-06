package ca.unb.mobiledev.slope

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

// Main Menu. Default startup activity
class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Get buttons from layout file
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnCredits = findViewById<Button>(R.id.btnCredits)

        // Listener for play button
        btnPlay.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, GameActivity::class.java)
            startActivity(intent)
        }

        // Listener for score clearing button
        btnClear.setOnClickListener {
            val sharedPref = getSharedPreferences(getString(R.string.PREF_FILE_NAME), Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.commit()
        }

        // Listener for credits button
        btnCredits.setOnClickListener{
            val creditsDialog = CreditsDialog()
            creditsDialog.show(supportFragmentManager,"credits_menu")
        }
    }
}