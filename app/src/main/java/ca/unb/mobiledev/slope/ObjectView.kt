package ca.unb.mobiledev.slope

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import java.lang.Exception


open class ObjectView (context: Context?, displayWidth: Int, displayHeight: Int, protected val objId: Int) :
    View(context) {

    protected open val defaultBitmap = R.drawable.b64

    private val mPainter = Paint()

    // Display dimensions
    private val mDisplayWidth: Int = displayWidth
    private val mDisplayHeight: Int = displayHeight

    protected var objScreenPos = Vec2(0f,0f)
    var position = Vec2(0f,0f)//Vec2(displayWidth / 2.0f,displayHeight / 2.0f)

    // Reference to the scaled bitmap object
    private lateinit var scaledBitmap: Bitmap

    private var hasBitmap = false

    var bitmapOffset = Vec2(0f,0f)

    var objRotation = 0f

    fun setBitmap(bitmapResource:Int=defaultBitmap){
        try{
            val bitmap = BitmapFactory.decodeResource(resources, bitmapResource)
            //by default, offset so positions are at center-bottom
            bitmapOffset = Vec2(bitmap.width.toFloat()/2, bitmap.height.toFloat())
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height,false)
            hasBitmap = true
        }catch(e:Exception){

        }
    }


    @Synchronized
    override fun onDraw(canvas: Canvas) {
        if(hasBitmap) {
            canvas.save()
            canvas.rotate(objRotation,
                objScreenPos.y+bitmapOffset.x,objScreenPos.y+bitmapOffset.y)
            canvas.drawBitmap(scaledBitmap, objScreenPos.x, objScreenPos.y, mPainter)
            canvas.restore()
        }
    }

    companion object {
        private const val BITMAP_SIZE = 64
        private const val MAX_STEP = 10
    }

    fun isOnScreen():Boolean {
        // Return true if the BubbleView is on the screen
        //TODO: probably need to use bitmapOffset in other parts of the condition
        return objScreenPos.x <= mDisplayWidth
                && objScreenPos.x + BITMAP_SIZE + bitmapOffset.x >= 0
                && objScreenPos.y <= mDisplayHeight
                && objScreenPos.y + BITMAP_SIZE >= 0
    }

    //val isActive = true

    open fun start(objMap:Map<String,ObjectView>){//bitmap:Int=-1){
        // Smooth out the edges
        //mPainter.isAntiAlias = true

    }

    open fun update(deltaT : Float, objMap:Map<String,ObjectView>){

    }

    open fun render(camera:Vec2=Vec2(0f,0f)){//canvas: Canvas, objScreenPos: Vec2){
        if(hasBitmap){
            setScreenPos(camera)
            if (isOnScreen()) {
                this@ObjectView.postInvalidate()
            }
        }

    }

    protected fun setScreenPos(cameraPos: Vec2) {
        objScreenPos = position - bitmapOffset - cameraPos
    }
}