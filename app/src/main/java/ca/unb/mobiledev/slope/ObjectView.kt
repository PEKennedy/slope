package ca.unb.mobiledev.slope

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import java.lang.Exception


open class ObjectView (context: Context?, displayWidth: Int, displayHeight: Int, protected val objId: Int) :
    View(context) {

    protected open val defaultBitmap = R.drawable.b64

    // Display dimensions
    //private var mStepX: Int
   // private var mStepY: Int
    private val mPainter = Paint()

    private val mDisplayWidth: Int = displayWidth
    private val mDisplayHeight: Int = displayHeight

    private var screenPos = Vec2(0f,0f)
    var position = Vec2(displayWidth / 2.0f,displayHeight / 2.0f)

    // Reference to the scaled bitmap object
    private lateinit var scaledBitmap: Bitmap

    private var hasBitmap = false


    fun setBitmap(bitmapResource:Int=defaultBitmap){
        try{
            val bitmap = BitmapFactory.decodeResource(resources, bitmapResource)
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height,false)
            hasBitmap = true
        }catch(e:Exception){

        }
    }


    @Synchronized
    override fun onDraw(canvas: Canvas) {
        if(hasBitmap) {
            canvas.drawBitmap(scaledBitmap, screenPos.x, screenPos.y, mPainter)
        }
    }

    companion object {
        private const val BITMAP_SIZE = 64
        private const val MAX_STEP = 10
    }

    private fun isOnScreen():Boolean {
        // Return true if the BubbleView is on the screen
        return screenPos.x <= mDisplayWidth
                && screenPos.x + BITMAP_SIZE >= 0
                && screenPos.y <= mDisplayHeight
                && screenPos.y + BITMAP_SIZE >= 0
    }

    val isActive = true

    open fun start(){//bitmap:Int=-1){
        // Smooth out the edges
        //mPainter.isAntiAlias = true

        //setBitmap
        /*if(bitmap != -1){
            setBitmap()//bitmap)
        }*/
    }

    open fun update(deltaT : Float){
        //Log.i("gameThread", "Called from object id:$id")
       // Log.i("Obj2",deltaT.toString())
        //position += Vec2(0f, 100f)*deltaT
        //Log.i("Obj2",position.toString())
    }

    open fun render(){//canvas: Canvas, screenPos: Vec2){
        //Log.i("renderThread", "Called render obj id:$id")
        if(hasBitmap){
           // Log.i("Obj",position.toString())
            setScreenPos(Vec2(0f,0f))
            //screenPos += Vec2(0f,1f)
            if (isOnScreen()) {
                this@ObjectView.postInvalidate()
            }
        }

    }

    protected fun setScreenPos(cameraPos: Vec2) {
        screenPos = position - cameraPos
    }
}