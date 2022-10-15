package ca.unb.mobiledev.slope

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import kotlin.math.sin

private const val STROKE_WIDTH = 12f

class TestCircle(id: Int) : GameObject(id) {
    //protected var posX = 300f
    //protected var posY = 500f
    protected var pos = Vec2(300f,500f)
    private val drawColor = R.color.colorPaint//ResourcesCompat.getColor(resources, R.color.colorPaint, null)
    protected var paint = Paint().apply{
        color = drawColor
        isAntiAlias = true //smooth edges
        isDither = true //how to down sample colours if needed
        style = Paint.Style.FILL_AND_STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    protected var sinProg = 0f

    override fun update(deltaT : Long){
        //Log.i("gameThread", "PosX: $posX")
        sinProg += 0.1f/deltaT.toFloat()//sin(100f * deltaT.toDouble()).toFloat()
        pos.x += sin(sinProg).toFloat()*10f / deltaT.toFloat()
    }

    override fun render(canvas: Canvas, screenPos: Vec2) {
        //Log.i("gameThread", "PosX2: $posX")
        var screenPos = WorldToScreenCoordinates(pos,screenPos)
        canvas.drawCircle(screenPos.x,screenPos.y,150f,paint)
    }
}