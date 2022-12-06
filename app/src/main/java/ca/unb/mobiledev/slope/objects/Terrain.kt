package ca.unb.mobiledev.slope.objects

import android.content.Context
import android.graphics.*
import android.util.Log
import android.widget.RelativeLayout
import ca.unb.mobiledev.slope.ObjectView
import ca.unb.mobiledev.slope.R
//import ca.unb.mobiledev.slope.R
import ca.unb.mobiledev.slope.Vec2
import kotlin.math.atan
import kotlin.random.Random

import ca.unb.mobiledev.slope.R.drawable.coin as texture

import ca.unb.mobiledev.slope.noise.SimplexNoise_Octave as Noise

class Terrain(context: Context?, val displayWidth: Int, val displayHeight: Int, objId: Int,
              val obstacles: MutableList<Obstacle>, val bg:MutableList<Background>)
    :ObjectView(context,displayWidth,displayHeight,objId) {

    private val SEGMENT_WIDTH = 200f //250f
    private val NOISE_STEP = 0.2f //1f
    private val HEIGHT_SPREAD = 150f //how much //100f

    override val defaultBitmap = texture


    val y= Random.nextInt()

    //0 = random seed
    val noise = Noise(0)

    private var segments = mutableListOf<Segment>()

    private var verts_mutable = mutableListOf<Float>()
    private lateinit var verts:FloatArray

    var offset = Vec2(0f,-600f)

    private var lastSegment = 0
    private var mPlayerDistance = 0

    var vertExtraOffset = Vec2(0f,0f)

    val triPaint = Paint().apply{
        val bitmap = BitmapFactory.decodeResource(
            resources, R.drawable.ground
        )
        val shader = BitmapShader(
            bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        this.shader = shader
    }

    //TODO: find a way to fix rotation, it seems to get the right rotation,
    //but it also displaces the player up or down
    //** seems mitigated by having the player to the left of the screen
    //so it seems affected by screen coordinate

    override fun start(objMap:Map<String,ObjectView>){
        generateNewSegments(12,0f)
        for(ob in obstacles){
            cycleObstacle(false)
        }

        //addObstacle(objMap)
    }

    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

        //val obstacle:Obstacle = objMap["Obstacle1"] as Obstacle
        //obstacle.position = playerCollide(obstacle.position)//segments[2].getSurfacePos(5f)
    }

    //(lastSegment+Random(System.currentTimeMillis()).nextFloat())*SEGMENT_WIDTH
    //Vec2(800f,0f)
    fun cycleObstacle(checkOnScreen:Boolean=true){
        val sortedObstacles = obstacles.sortedBy { it.position.x }

        if((!sortedObstacles[0].isOnScreen() && checkOnScreen) || !checkOnScreen){
            val rand = Random(System.currentTimeMillis()).nextFloat()*3
            val x = (lastSegment.toFloat()+rand-3f)*SEGMENT_WIDTH
            //Log.i("LAST SEG",lastSegment.toString())
            sortedObstacles[0].position = playerCollide(Vec2(x,0f))
            sortedObstacles[0].collider.position = sortedObstacles[0].position
            //Log.i("CYCLE OB",x.toString())
        }

    }

    fun cycleBackground(){
        val sortedObstacles = bg.sortedBy { it.position.x }

        if(sortedObstacles[0].isOnScreen()){
            val rand = Random(System.currentTimeMillis()).nextFloat()*3
            val x = (lastSegment.toFloat()+rand-3f)*SEGMENT_WIDTH
            //Log.i("LAST SEG",lastSegment.toString())
            sortedObstacles[0].position = playerCollide(Vec2(x,0f))
            sortedObstacles[0].position.y += sortedObstacles[0].yOffset
            //Log.i("CYCLE OB",x.toString())
        }
    }

    /*private var obstacleCount=  0
    fun addObstacle(objMap:Map<String,ObjectView>){
        val ob = Obstacle(context, displayWidth,displayHeight,10)
        mFrame.addView(ob)
        ob.start(objMap)
        ob.position = playerCollide(Vec2((lastSegment+Random(System.currentTimeMillis()).nextFloat())*SEGMENT_WIDTH,0f))
        obstacles += ob
        //gameObjects += Pair("Obstacle"+obstacleCount.toString(),ob)
        //obstacleCount++
    }*/

    private val overallSlope = -0.25f
    fun gradient(seg:Int):Float{
        val x = seg*SEGMENT_WIDTH
        return overallSlope*x
    }

    fun generateNewSegments(numSegments:Int=2, yOffset: Float) {
//        segments = mutableListOf<Segment>()
        for(i in 0..numSegments){
            val height_1 = noise.noise((i+lastSegment)*NOISE_STEP.toDouble(),0.0).toFloat()
            val height_2 = noise.noise((i+1+lastSegment)*NOISE_STEP.toDouble(),0.0).toFloat()
            segments.add(Segment(
                Vec2((i+lastSegment)*SEGMENT_WIDTH + offset.x,(height_1*HEIGHT_SPREAD) + gradient(lastSegment+i) + offset.y),
                Vec2((i+1+lastSegment)*SEGMENT_WIDTH + offset.x,(height_2*HEIGHT_SPREAD)+ gradient(lastSegment+1+i) + offset.y))
            )
        }
        lastSegment += numSegments+1
        segments.forEach {
            verts_mutable.addAll(it.getVertices(yOffset))
        }
//        if (segments.size >= 40){
//            removeOldSegments(segments.size-20)
//        }
        Log.d("Segments", segments.size.toString())
    }

//    fun removeOldSegments(/*numSegments:Int=2*/){
////        for(i in 0..numSegments){
////            segments.removeAt(0)
////        }
////        lastSegment = segments.size
//        segments = segments.subList(segments.size-20, segments.size)
//        lastSegment = segments.size
//    }

//    fun removeOldSegments(){
//        mPlayerDistance += playerDistance
//
//    }

    fun displaceSegs(d:Vec2){
        segments.forEach {
        //    it.l += d
        //    it.r += d
            it.updateCoords(d)
        }

    }

    private fun displaceVerts(ar:MutableList<Float>,d:Vec2):MutableList<Float>{
        val x = mutableListOf<Float>()
        var i = 0
        ar.forEach {
            if(i.mod(2)==0){
                x.add(it + d.x)
            }
            else{
                x.add(it + d.y)
            }
            i += 1
        }
        //Log.i("Terrain",x.toString())
        return x
    }


    //we don't want to draw a texture since terrain is autogenerated
    @Synchronized
    override fun onDraw(canvas: Canvas) {
        //val verts = floatArrayOf(200f,100f,250f, 300f, 400f, 150f)

        //needs to match the length of verts we ask to draw
        /*val verticesColors = intArrayOf(
            Color.RED, Color.RED, Color.RED,
            Color.RED, Color.RED, Color.RED
        )*/

        //triangle_strip lets us reuse some verts
        canvas.drawVertices(Canvas.VertexMode.TRIANGLES,//TRIANGLE_STRIP,//TRIANGLES,
            verts.size, verts,0,
            verts,0,
        null,0,//verticesColors,0,
        null,0, 0,
            triPaint//Paint()
        )

    }

    fun getPlayerAngle(playerPos:Vec2):Float{
        val segment = getSegmentByPlayerPos(playerPos)
        if(segment != null){
            //Log.i("ROTATE PLAYER",segment.getAngle().toString())
            return Math.toDegrees(segment.getAngle().toDouble()).toFloat()
        }
        return 0f;
    }

    fun playerCollide(playerPos:Vec2):Vec2{
        val segment = getSegmentByPlayerPos(playerPos)
        if(segment != null){
            return segment.getSurfacePos(playerPos.x)
        }
        Log.i("T-P Collide","NO SEGMENT")
        return Vec2(-1f,-1f)
    }

    fun checkPlayerCollide(playerPos:Vec2):Boolean{
        val segment = getSegmentByPlayerPos(playerPos)
        if(segment != null) {
            return segment.checkPlayerCollision(playerPos)
        }
        return false
    }

    //TODO: if removeSegment is re-enabled, getSegmentNum seems to give the wrong segment

    fun getSegmentNum(playerPos: Vec2):Int{
        val segmentNum = (playerPos.x/SEGMENT_WIDTH).toInt()
        if(segmentNum < segments.size) return segmentNum
        return -1
    }

    fun getNumSegments():Int{
        return lastSegment// + mPlayerDistance
    }

    private fun getSegmentByPlayerPos(playerPos:Vec2):Segment?{
        val segmentNum = (playerPos.x/SEGMENT_WIDTH).toInt()
        //Log.i("SEGMENT NUM",segmentNum.toString())
        if(segmentNum < segments.size){
            return segments[segmentNum]
        }
        return null
    }

    override fun render(camera: Vec2) {
        //super.render(camera)

        //displace the vertices' screenposition
        verts = displaceVerts(verts_mutable,-camera).toFloatArray()
        this@Terrain.postInvalidate()

    }



    class Segment(val lIn: Vec2, val rIn: Vec2){
        var l = lIn
        var r = rIn

        fun updateCoords(offset:Vec2){
            l = lIn+offset
            r = rIn+offset
        }
        private val m = getSlope()
        private val b = -l.y //getOffset()

        private val horizontalOffset = l.x
        //get m from y=mx+b
        private fun getSlope():Float{
            return -(r.y-l.y)/(r.x-l.x) //negative? >>
        }

        fun getAngle():Float{
            //atan = inverse tan
            return atan(m)
        }

        fun getSurfacePos(x:Float):Vec2{
            val slopeY = (x-horizontalOffset)*m +b//x/m + b
            return Vec2(x,slopeY) //return playerPos
        }

        fun checkPlayerCollision(playerPos:Vec2):Boolean{
            return playerPos.y > getSurfacePos(playerPos.x).y
        }

        fun getVertices(yOffset:Float): MutableList<Float> {
            return mutableListOf<Float>(
                //tri 1
                l.x,-l.y,
                r.x,-r.y,
                l.x,1500f+yOffset,

                //tri 2
                r.x,-r.y,
                r.x,1500f+yOffset,
                l.x,1500f+yOffset
            )
        }
        fun getVerticesFinal(): FloatArray {
            return floatArrayOf(
                //tri 1
                l.x,-l.y,
                r.x,-r.y,
                l.x,1000f,

                //tri 2
                r.x,-r.y,
                r.x,1000f,
                l.x,1000f
            )
        }
    }

}