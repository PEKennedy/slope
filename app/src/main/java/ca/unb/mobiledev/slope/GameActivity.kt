package ca.unb.mobiledev.slope

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*


interface CloseHandle {
    fun close()
    fun unPause()
}

class GameActivity : AppCompatActivity(), CloseHandle {

    private val pauseMenu = PauseMenuDialog(this)

    private var id = 0
    private val testObj = GameObject(id++)
    private val testObj2 = GameObject(id++)
    private val testObj3 = TestCircle(id++)

    private val gameObjects = listOf<GameObject>(testObj, testObj2, testObj3)
    private var lastTime = System.currentTimeMillis()
    private var curTime = lastTime
    private var deltaT = 0L

    private var isPaused = false

    private val gameJob = startGameJob() //keep this after gameObjects list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val btnPause = findViewById<Button>(R.id.btnPause)
        btnPause.setOnClickListener {
            pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameJob.cancel()
    }

    private fun pause(){
        val fragmentManager = supportFragmentManager//supportFragmentManager
        pauseMenu.show(fragmentManager,"pause_menu")
        isPaused = true
    }

    override fun close() {
        finish()
    }

    override fun unPause() {
        isPaused = false
    }

    private fun startGameJob(): Job { //timeInterval: Long
        gameObjects.forEach {
            it.start()
        }

        return CoroutineScope(Dispatchers.Default).launch{
            while(NonCancellable.isActive){
                if(!isPaused){ //don't know of a way to "pause" the thread, so its going to just keep running
                    curTime = System.currentTimeMillis()
                    deltaT = curTime - lastTime //find time since last frame
                    lastTime = curTime

                    gameObjects.forEach {
                        if(it.isActive){
                            it.update(deltaT)
                            //it.render()
                        }
                    }

                    //delay(timeInterval)
                    delay(1000)


                }
            }
        }
    }


}