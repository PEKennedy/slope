package ca.unb.mobiledev.slope

import android.app.Activity
import android.graphics.Point
import android.hardware.SensorManager
import android.os.*
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.WindowInsets
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Layer
import androidx.constraintlayout.widget.ConstraintLayout
import ca.unb.mobiledev.slope.objects.*
import ca.unb.mobiledev.slope.sensor.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

interface CloseHandle {
    fun close()
    fun unPause()
}

// Activity for gameplay. Main activity experienced while using the app
class GameActivity : AppCompatActivity(), CloseHandle {

    private var mFrame: RelativeLayout? = null
    private lateinit var distText:TextView

    private val pauseMenu = PauseMenuDialog(this)

    private var gestureDetector: GestureDetector? = null

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
    var background = mutableListOf<Background>()

    // Reference to the thread job
    private var mMoverFuture: ScheduledFuture<*>? = null

    var distance = 0
    var wasTouched = false

    // Sensor
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer : AccelerometerSensor

    // Setup values when activity is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val btnPause = findViewById<Button>(R.id.btnPause)
        btnPause.setOnClickListener {
            pause()
        }
        mFrame = findViewById(R.id.gameFrame)//findViewById(R.id.frame) //relativeLayout gameFrame

        mFrame!!.setLayerType(Layer.LAYER_TYPE_SOFTWARE,null)

        distText = findViewById(R.id.distance)
        distText.text = "testing"

        val frame = findViewById<ConstraintLayout>(R.id.frame)

        // Sensor
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = AccelerometerSensor(mSensorManager)
        mAccelerometer.register()

        startGame()

    }

    // Touch event handler, for jumping ingame
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    //borrowed from lab8
    private fun setupGestureDetector(){
        gestureDetector = GestureDetector(this,object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(motionEvent: MotionEvent): Boolean {
                wasTouched = true
                return true
            }

            override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return true
            }

            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                return true
            }
        })
    }

    // Starts game. Initializes gameplay related variables and objects
    fun startGame(){
        // Determine the screen size
        val (width, height) = getScreenDimensions(this)

        mFrame?.removeAllViews()
        //Reset the game state
        gameObjects.clear()


        lastTime = System.currentTimeMillis()
        id = 0

        val terrain = Terrain(applicationContext,width,height,id++,obstacles,background)
        gameObjects += Pair("Terrain",terrain)

        val obstacle1 = Obstacle(applicationContext,width,height,id++)
        gameObjects += Pair("Obstacle1",obstacle1)
        obstacles += obstacle1

        val obstacle2 = Obstacle(applicationContext,width,height,id++)
        obstacle2.obType = 2 //tree
        gameObjects += Pair("Obstacle2",obstacle2)
        obstacles += obstacle2

        val obstacle3 = Obstacle(applicationContext,width,height,id++)
        obstacle3.obType = 2 //tree
        gameObjects += Pair("Obstacle3",obstacle3)
        obstacles += obstacle3

        val obstacle4 = Obstacle(applicationContext,width,height,id++)
        obstacle4.obType = 1 //snowman
        gameObjects += Pair("Obstacle4",obstacle4)
        obstacles += obstacle4

        val obstacle5 = Obstacle(applicationContext,width,height,id++)
        obstacle5.obType = 1 //snowman
        gameObjects += Pair("Obstacle5",obstacle5)
        obstacles += obstacle5

        val obstacle6 = Obstacle(applicationContext,width,height,id++)
        gameObjects += Pair("Obstacle6",obstacle6)
        obstacles += obstacle6

        val player = Player(applicationContext,width,height,id++,this,obstacles, mAccelerometer)
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

    // Unused function to reset game object positions
    fun resetPositions(offset:Vec2){
        gameObjects.values.forEach{
            it.position += offset
        }
        cameraPos += offset
    }

    // Gets called when a game over is registered. Stops the game.
    fun gameOver(){
        if(!isPaused){
            val gameOverMenu = GameOverMenuDialog(this,distance)
            gameOverMenu.show(supportFragmentManager,"game_over_menu")
            gameOverMenu.isCancelable = false
            isPaused = true
        }
    }

    // Default activity method when activity is destroyed. Unregisters sensor
    override fun onDestroy() {
        super.onDestroy()
        mAccelerometer.unregister()
    }

    // Gets called when game is paused by player
    private fun pause(){
        val fragmentManager = supportFragmentManager
        pauseMenu.show(fragmentManager,"pause_menu")
        isPaused = true
        mAccelerometer.unregister()
    }

    // Default activity method when activity is resumed.
    override fun onResume() {
        super.onResume()
        setupGestureDetector()
        mAccelerometer.register()
    }

    // Default activity method when activity is paused.
    override fun onPause() {
        super.onPause()
        mAccelerometer.unregister()
    }

    // Default activity method on close.
    override fun close() {
        finish()
    }

    // Default activity method when activity is unPaused.
    override fun unPause() {
        isPaused = false
        mAccelerometer.register()
    }

    // Sets the text that shows player distance travevled
    private fun setDistanceText(dist:Int){
        distance = dist
        distText.text = dist.toString() + "m"
    }

    // Default activity method when window focus changes
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) isPaused = true
    }

    // Returns current screen dimensions
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