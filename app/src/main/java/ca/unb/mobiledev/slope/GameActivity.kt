package ca.unb.mobiledev.slope

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


interface CloseHandle {
    fun close()
}

class GameActivity : AppCompatActivity(), CloseHandle {
    private val pauseMenu = PauseMenuDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val btnPause = findViewById<Button>(R.id.btnPause)
        btnPause.setOnClickListener {
            pause()
        }
    }

    private fun pause(){
        val fragmentManager = supportFragmentManager//supportFragmentManager
        pauseMenu.show(fragmentManager,"pause_menu")

    }

    override fun close() {
        finish()
    }

}