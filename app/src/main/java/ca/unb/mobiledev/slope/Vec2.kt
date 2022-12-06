package ca.unb.mobiledev.slope

import kotlin.math.sqrt

// Vector 2 class. Keeps position data in 2 dimensions
class Vec2(var x :Float, var y:Float) {

    // Addition operation with this vector and another vector. Returns resulting vector
    operator fun plus(b:Vec2):Vec2{
        return Vec2(x+b.x, y+b.y)
    }

    // Subtraction operation with this vector and another vector. Returns resulting vector
    operator fun minus(b:Vec2):Vec2{
        return Vec2(x-b.x, y-b.y)
    }

    // Multiplication operation with this vector and another vector. Returns resulting vector
    operator fun times(b:Vec2):Vec2{
        return Vec2(x*b.x,y*b.y)
    }

    // Multiplication operation with this vector and a number. Returns resulting vector
    operator fun times(b:Float):Vec2{
        return Vec2(x*b,y*b)
    }

    // Division operation with this vector and a number. Returns resulting vector
    operator fun div(b:Float):Vec2{
        return Vec2(x/b,y/b)
    }

    // Reverses vector to the negative version of the same vector. (Rotated 180 degrees)
    operator fun unaryMinus() = Vec2(-x,-y)

    // Returns length of this vector
    fun length():Float {
        return sqrt((x*x) + (y*y))
    }

    // Ensures length of vector remains the same even if position in 2D space changes
    fun normalize(){
        x /= this.length()
        y /= this.length()
    }

    // Returns a normalized vector based on this vector
    fun normalized():Vec2{
        return Vec2(x/this.length(),y/this.length())
    }

    // ToString method to display vector in text form
    override fun toString(): String {
        return "{$x,$y}"
    }
}