package ca.unb.mobiledev.slope

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        btnPlay.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, GameActivity::class.java)
            startActivity(intent)
        }

//        val sharedPref = this?.getSharedPreferences(getString(R.string.PREF_FILE_NAME), Context.MODE_PRIVATE) ?: return
//        sharedPref.getFloat(getString(R.string.saved_volume_value_key), )
    }
}