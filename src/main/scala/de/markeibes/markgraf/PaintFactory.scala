package de.markeibes.markgraf

import android.graphics.{Typeface, Color, Paint}

object PaintFactory {
  def makePaint(color:Int, antiAlias:Boolean=false, style:Paint.Style=Paint.Style.FILL):Paint = {
    val p = new Paint()
    p.setColor(color)
    p.setAntiAlias(antiAlias)
    p.setStyle(Paint.Style.FILL)
    p
  }

  def makeTextPaint(color:Int, textSize:Float, typeface:Option[Typeface]=None, textAlign:Paint.Align=Paint.Align.LEFT):Paint = {
    val p = makePaint(color, antiAlias = true)
    typeface.fold()(tf => p.setTypeface(tf))
    p.setTextSize(textSize)
    p.setTextAlign(textAlign)
    p
  }
}
