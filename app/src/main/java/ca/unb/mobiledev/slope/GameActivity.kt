package ca.unb.mobiledev.slope

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val btnPause = findViewById<Button>(R.id.btnPause)
        btnPause.setOnClickListener {
            finish()
        }
    }
}