package ca.unb.mobiledev.slope.old

class textureObj(id: Int) : GameObject(id) {
    //protected var posX = 300f
    //protected var posY = 500f
   /* protected var pos = Vec2(300f,500f)
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


    companion object {
        private const val BITMAP_SIZE = 64
        private const val MAX_STEP = 10
        private const val REFRESH_RATE = 40
    }
    //private var scaledBitmap: Bitmap



    override fun start() {
        super.start()
        //val bitmap = BitmapFactory.decodeResource(resources, R.drawable.b64)
        //scaledBitmap = Bitmap.createScaledBitmap(bitmap, BITMAP_SIZE, BITMAP_SIZE,false)
    }

    override fun update(deltaT : Long){
        //Log.i("gameThread", "PosX: $posX")
        sinProg += 0.1f/deltaT.toFloat()//sin(100f * deltaT.toDouble()).toFloat()
        pos.x += sin(sinProg).toFloat()*10f / deltaT.toFloat()
    }

    override fun render(canvas: Canvas, objScreenPos: Vec2) {
        //Log.i("gameThread", "PosX2: $posX")
        var objScreenPos = WorldToScreenCoordinates(pos,objScreenPos)
        canvas.drawCircle(objScreenPos.x,objScreenPos.y,150f,paint)
    }*/
}