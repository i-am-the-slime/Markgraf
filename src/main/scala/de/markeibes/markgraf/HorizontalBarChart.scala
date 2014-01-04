package de.markeibes.markgraf

import android.util.{Log, AttributeSet}
import android.content.Context
import android.graphics._
import scala.collection.JavaConverters._
import android.view.View.MeasureSpec

class HorizontalBarChart(val ctx:Context, as:AttributeSet, style:Integer, val v:HorizontalBarChartView){
  var barColor = Color.argb(100,40,90,240)
  var captionTextColour = Color.rgb(200,200,200)
  var captionTextSize = 30.0f
  var valueTextColour = Color.rgb(200,200,200)
  var valueTextSize = 30.0f
  var barHeight = 50.0f
  val offsetFactor = 1.2f
  var bmp:Option[Bitmap] = None
  lazy val barPaint = PaintFactory.makePaint(barColor)
  lazy val captionTextPaint = PaintFactory.makeTextPaint(captionTextColour, captionTextSize)
  lazy val valueTextPaint = PaintFactory.makeTextPaint(captionTextColour, valueTextSize, textAlign=Paint.Align.RIGHT)

  def updateWidthAndHeight(width:Int, height:Int):WidthHeightTuple = {
    if(width > 0 && height > 0){
      updateBitmap(width-v.getPaddingLeft-v.getPaddingRight, height-v.getPaddingTop-v.getPaddingBottom)
    }
    WidthHeightTuple(width, height)
  }

  if(Option(as).isDefined){
    val typedArray = ctx.getTheme.obtainStyledAttributes(as, R.styleable.HorizontalBarChartView, 0, 0)
    val attrBarColor = typedArray.getColor(R.styleable.HorizontalBarChartView_barColor, 0)
    if(attrBarColor!=0) barColor = attrBarColor
    val attrTextColor = typedArray.getColor(R.styleable.HorizontalBarChartView_textColor, 0)
    if(attrTextColor!=0)  captionTextColour = attrTextColor
    val attrBarHeight:Float = typedArray.getDimension(R.styleable.HorizontalBarChartView_barHeight, 0)
    if(attrBarHeight!=0) barHeight = attrBarHeight
    val attrCaptionTextSize = typedArray.getDimension(R.styleable.HorizontalBarChartView_captionTextSize, 0)
    if(attrCaptionTextSize!=0) captionTextSize = attrCaptionTextSize
    val attrValueTextSize = typedArray.getDimension(R.styleable.HorizontalBarChartView_valueTextSize, 0)
    if(attrValueTextSize!=0) valueTextSize = attrValueTextSize
    typedArray.recycle()
  }

  case class WidthHeightTuple(w:Int, h:Int)

  def onMeasure(w:Int, h:Int):WidthHeightTuple = {
    val newWidth = MeasureSpec.getMode(w) match {
      case MeasureSpec.UNSPECIFIED => 1
      case _ => MeasureSpec.getSize(w)
    }
    val newHeight = MeasureSpec.getMode(h) match {
      case MeasureSpec.UNSPECIFIED => heightFromData()
      case _ => MeasureSpec.getSize(h)
    }
    WidthHeightTuple(newWidth, newHeight)
  }

  def heightFromData():Int = (data.length * barHeight * offsetFactor).toInt + 1

  def onSizeChanged(w:Int, h:Int):WidthHeightTuple = {
    if(w>0 && h>0){
      updateWidthAndHeight(w,h)
    } else if (w>0 && h==0) {
      updateWidthAndHeight(w, heightFromData())
    } else if (w==0 && h>0) {
      updateWidthAndHeight(300, heightFromData())
    } else {
      updateWidthAndHeight(1,1)
    }
  }


  def setTypeface(tf:Typeface){
    captionTextPaint.setTypeface(tf)
    valueTextPaint.setTypeface(tf)
  }

  private var data:List[DataPoint] = List()
  var maxValue:Float = 0

  def setData(list:java.util.List[DataPoint]) = {
    if (!list.isEmpty){
      data =  list.asScala.toList
      maxValue = data.map(x => x.value).max
    }
    if(bmp.isDefined){
      drawDataOnBitmap(bmp.get)
    }
  }

  def updateBitmap(width:Int, height:Int){
    bmp = Some(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888))
    if(!data.isEmpty){
      drawDataOnBitmap(bmp.get)
    }
  }


  def drawDataOnBitmap(bmp:Bitmap){
    val canvas = new Canvas(bmp)
    val width = canvas.getWidth
    data.foldLeft(0.0f)( (yOffset,dataPoint) => {
      canvas.drawRect(new RectF(0, yOffset, (dataPoint.value/maxValue)*width, barHeight+yOffset), barPaint)
      canvas.drawText(dataPoint.label, 0+captionTextSize*0.2f, barHeight+yOffset-0.5f*captionTextSize, captionTextPaint)
      canvas.drawText("%3.0f" format dataPoint.value, width-valueTextSize*0.2f, barHeight+yOffset-0.5f*valueTextSize, valueTextPaint)
      yOffset + offsetFactor*barHeight
    })
    v.invalidate()
  }

  def onDraw(c:Canvas){
    if(bmp.isDefined) c.drawBitmap(bmp.get, v.getPaddingLeft, v.getPaddingTop, null)
  }

}
