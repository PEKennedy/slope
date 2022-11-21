package ca.unb.mobiledev.slope

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnClear = findViewById<Button>(R.id.btnClear)
        btnPlay.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, GameActivity::class.java)
            startActivity(intent)
        }
        btnClear.setOnClickListener {
            val sharedPref = getSharedPreferences(getString(R.string.PREF_FILE_NAME), Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.commit()
        }
        
    }
}