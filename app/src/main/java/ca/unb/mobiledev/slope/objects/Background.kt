package ca.unb.mobiledev.slope.objects

import android.content.Context
import ca.unb.mobiledev.slope.ObjectView
import ca.unb.mobiledev.slope.R

class Background(context: Context?, displayWidth: Int, displayHeight: Int, objId: Int)
    :ObjectView(context,displayWidth,displayHeight,objId) {

    override val defaultBitmap = R.drawable.cloud//texture

    var obType:Int = 0

    var yOffset:Float = 500f

    override fun start(objMap:Map<String,ObjectView>){
        setTexture(obType)
    }

    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

    }

    fun setTexture(type:Int=0){
        if(type == 1){ //snowman
            val texture = R.drawable.mountain
            setBitmap(texture)
        }
        else{ //crate
            setBitmap()
        }
    }

}