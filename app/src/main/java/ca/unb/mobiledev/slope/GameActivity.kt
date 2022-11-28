package ca.unb.mobiledev.slope

import android.app.Activity
import android.graphics.Point
import android.os.*
import android.util.Log
import android.view.WindowInsets
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.slope.objects.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


interface CloseHandle {
    fun close()
    fun unPause()
}

class GameActivity : AppCompatActivity(), CloseHandle {

    private var mFrame: RelativeLayout? = null
    private lateinit var distText:TextView

    private val pauseMenu = PauseMenuDialog(this)




    private var id = 0

    private var lastTime = System.currentTimeMillis()
    private var curTime = lastTime
    private var deltaT = 0f

    private var isPaused = false
    private val REFRESH_RATE = 16

    //used for transforming object coordinates to on-screen coordinates
    var cameraPos = Vec2(0f,0f)

    //use a map so its easy to find objects
    var gameObjects = mutableMapOf<String,ObjectView>() //:MutableMap<String,ObjectView>// = MutableMap<String,ObjectView>()

    var obstacles = mutableListOf<Obstacle>()

    // Reference to the thread job
    private var mMoverFuture: ScheduledFuture<*>? = null

    var distance = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val btnPause = findViewById<Button>(R.id.btnPause)
        btnPause.setOnClickListener {
            pause()
        }
        mFrame = findViewById(R.id.gameFrame)//findViewById(R.id.frame) //relativeLayout gameFrame

        distText = findViewById(R.id.distance)
        distText.text = "testing"
        Log.i("ACTIVITY",btnPause.text.toString())
        //actionBar?.hide()


        startGame()

    }



    fun startGame(){
        // Determine the screen size
        val (width, height) = getScreenDimensions(this)

        mFrame?.removeAllViews()
        //Reset the game state
        gameObjects.clear() //= mapOf<String,ObjectView>()


        lastTime = System.currentTimeMillis()
        id = 0


        //foreach object

        val terrain = Terrain(applicationContext,width,height,id++,obstacles)
        gameObjects += Pair("Terrain",terrain)

        val obstacle1 = Obstacle(applicationContext,width,height,id++)
        gameObjects += Pair("Obstacle1",obstacle1)
        obstacles += obstacle1

        val obstacle2 = Obstacle(applicationContext,width,height,id++)
        gameObjects += Pair("Obstacle2",obstacle2)
        obstacles += obstacle2

        val obstacle3 = Obstacle(applicationContext,width,height,id++)
        gameObjects += Pair("Obstacle3",obstacle3)
        obstacles += obstacle3

        val obstacle4 = Obstacle(applicationContext,width,height,id++)
        gameObjects += Pair("Obstacle4",obstacle4)
        obstacles += obstacle4

        val obstacle5 = Obstacle(applicationContext,width,height,id++)
        gameObjects += Pair("Obstacle5",obstacle5)
        obstacles += obstacle5

        val player = Player(applicationContext,width,height,id++,this,obstacles)
        gameObjects += Pair("Player",player)

        gameObjects.values.forEach {
            mFrame?.addView(it)
            it.start(gameObjects)
        }

        // Creates a WorkerThread
        val executor = Executors.newScheduledThreadPool(1)

        mMoverFuture = executor.scheduleWithFixedDelay({
            curTime = lastTime
            lastTime = System.currentTimeMillis()
            deltaT = (lastTime-curTime).toFloat()/1000f

            if(!isPaused){
                gameObjects.values.forEach {
                    it.update(deltaT,gameObjects)
                    it.render(cameraPos)
                }

                runOnUiThread {
                   setDistanceText((player.position.x/100f).toInt())
                    if(player.hitObstacle){
                        executor.shutdown()
                        executor.awaitTermination(3000L,TimeUnit.MILLISECONDS)
                        gameOver()
                    }
                }

                cameraPos = player.position + Vec2(-300f,-400f)
            }


        }, 0, REFRESH_RATE.toLong(), TimeUnit.MILLISECONDS)
    }

    fun gameOver(){
        if(!isPaused){
            val gameOverMenu = GameOverMenuDialog(this,distance)
            gameOverMenu.show(supportFragmentManager,"game_over_menu")
            gameOverMenu.isCancelable = false
            isPaused = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun pause(){
        val fragmentManager = supportFragmentManager
        pauseMenu.show(fragmentManager,"pause_menu")
        isPaused = true
    }

    //TODO onPause, onResume >> try to suspend things to conserve battery

    override fun close() {
        finish()
    }

    override fun unPause() {
        isPaused = false
    }

    private fun setDistanceText(dist:Int){
        distance = dist
        distText.text = dist.toString() + "m"
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) isPaused = true

    }

    private fun getScreenDimensions(activity: Activity): Pair<Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val windowInsets: WindowInsets = windowMetrics.windowInsets
            val insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout())

            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom

            val b = windowMetrics.bounds
            Pair(b.width() - insetsWidth, b.height() - insetsHeight)
        } else {
            val size = Point()
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay // deprecated in API 30
            @Suppress("DEPRECATION")
            display?.getSize(size) // deprecated in API 30
            Pair(size.x, size.y)
        }
    }

}