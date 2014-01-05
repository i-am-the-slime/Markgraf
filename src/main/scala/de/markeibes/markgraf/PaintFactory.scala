package de.markeibes.markgraf

import android.graphics._

object PaintFactory {
  def makePaint(color:Int, antiAlias:Boolean=false, style:Paint.Style=Paint.Style.FILL):Paint = {
    val p = new Paint()
    p.setColor(color)
    p.setAntiAlias(antiAlias)
    p.setStyle(Paint.Style.FILL)
    p
  }

  def makeGradientPaint(startColor:Int, endColor:Int, height:Int):Paint = {
    val p = new Paint()
    p.setShader(new LinearGradient(0,0,0,height, startColor, endColor, Shader.TileMode.CLAMP))
    p
  }

  def makeTextPaint(color:Int, textSize:Float, typeface:Option[Typeface]=None, textAlign:Paint.Align=Paint.Align.LEFT):Paint = {
    val p = makePaint(color, antiAlias = false)
    typeface.fold()(tf => p.setTypeface(tf))
    p.setTextSize(textSize)
    p.setTextAlign(textAlign)
    p
  }
}
