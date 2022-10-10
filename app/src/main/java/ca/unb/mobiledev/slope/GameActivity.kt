package ca.unb.mobiledev.slope

import android.graphics.Color
import android.graphics.PorterDuff
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
    //private val gameView = findViewById<GameView>(R.id.canvas)

    private var id = 0



    private var lastTime = System.currentTimeMillis()
    private var curTime = lastTime
    private var deltaT = 0L

    private var isPaused = false

    val testObj = GameObject(id++)
    val testObj2 = GameObject(id++)
    val testObj3 = TestCircle(id++)
    val gameObjects = listOf<GameObject>(testObj, testObj2, testObj3)

    val gameJob = startGameJob(gameObjects) //keep this after gameObjects list
    //private val renderJob = startRenderJob()
    lateinit var renderJob: Job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val btnPause = findViewById<Button>(R.id.btnPause)
        btnPause.setOnClickListener {
            pause()
        }
        //these used to be private vars outside onCreate
        val gameView = findViewById<GameView>(R.id.canvas)
        renderJob = startRenderJob(gameView, gameObjects)
    }

    override fun onDestroy() {
        super.onDestroy()
        gameJob.cancel()
        renderJob.cancel()
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

    private fun startGameJob(gameObjects: List<GameObject>): Job { //timeInterval: Long
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
                            //it.render(gameView)
                            //it.render()
                        }
                    }

                    //delay(timeInterval)
                    //delay(50)
                    delay(16)

                }
            }
        }
    }

   private fun startRenderJob(gameView: GameView, gameObjects: List<GameObject>): Job {

       return CoroutineScope(Dispatchers.Default).launch {
           while(!gameView.isCanvasInit()){
               delay(1)
           }
           val canvas = gameView.getCanvas()
           while (NonCancellable.isActive) {
                if(!isPaused){
                    gameObjects.forEach {
                        if(it.isActive) {
                            it.render(canvas)
                        }
                    }
                    //force redraw
                    gameView.invalidate()
                    //wait?
                    delay(16)
                    //delay(50)
                    //clear screen
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                }

           }
        }
    }


}