package ca.unb.mobiledev.slope

import kotlin.math.sqrt

class Vec2(var x :Float, var y:Float) {
    //add, sub, dot(mul), div, scale, magnitude, normalized, normalize, set, get
    //length
    operator fun plus(b:Vec2):Vec2{
        return Vec2(x+b.x, y+b.y)
    }
    operator fun minus(b:Vec2):Vec2{
        return Vec2(x-b.x, y-b.y)
    }
    operator fun times(b:Vec2):Vec2{
        return Vec2(x*b.x,y*b.y)
    }
    operator fun times(b:Float):Vec2{
        return Vec2(x*b,y*b)
    }
    operator fun div(b:Float):Vec2{
        return Vec2(x/b,y/b)
    }
    operator fun unaryMinus() = Vec2(-x,-y)

    fun length():Float {
        return sqrt((x*x) + (y*y))
    }
    fun normalize(){
        x /= this.length()
        y /= this.length()
    }
    fun normalized():Vec2{
        return Vec2(x/this.length(),y/this.length())
    }
    /*fun getX():Float{
        return x
    }*/
}