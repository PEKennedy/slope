package ca.unb.mobiledev.slope

import android.app.Activity
import android.graphics.Point
import android.os.*
import android.view.WindowInsets
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.slope.objects.Obstacle
import ca.unb.mobiledev.slope.objects.Player
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
    var gameObjects = mapOf<String,ObjectView>()

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
        mFrame = findViewById(R.id.frame) //relativeLayout

        distText = findViewById(R.id.distance)
        distText.text = "testing"
        //actionBar?.hide()

        startGame()

    }



    fun startGame(){
        // Determine the screen size
        val (width, height) = getScreenDimensions(this)

        //foreach object
        val x = ObjectView(applicationContext,width,height,id++)
        x.setBitmap()
        gameObjects += Pair("test",x)

        val player = Player(applicationContext,width,height,id++,this)
        gameObjects += Pair("Player",player)

        val obstacle = Obstacle(applicationContext,width,height,id++)
        gameObjects += Pair("Obstacle1",obstacle)

        gameObjects.values.forEach {
            mFrame?.addView(it)
            it.start()
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

                /*val mainThreadLooper = Looper.getMainLooper() // --> Looper of the main/UI thread
                val mainThreadHandler = Handler(mainThreadLooper) // --> Get handler to main thread
                val messageToSendToMainThread =
                    Message.obtain() // --> Create a message to send to UI thread
                messageToSendToMainThread.obj = (player.position.x/100f).toInt()//123 // 123 -> actual msg value
                mainThreadHandler.sendMessage(messageToSendToMainThread)*/

                runOnUiThread {
                   setDistanceText((player.position.y/100f).toInt())
                    if(player.hitObstacle){
                        gameOver()
                    }
                }

                //setDistanceText((player.position.x/100f).toInt())
            }


        }, 0, REFRESH_RATE.toLong(), TimeUnit.MILLISECONDS)
    }

    fun gameOver(){
        val gameOverMenu = GameOverMenuDialog(this,distance)
        //gameOverMenu.setScoreText(distance)//.distance = distance
        gameOverMenu.show(supportFragmentManager,"game_over_menu")
        gameOverMenu.isCancelable = false
        isPaused = true
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun pause(){
        val fragmentManager = supportFragmentManager
        pauseMenu.show(fragmentManager,"pause_menu")
        isPaused = true
    }

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