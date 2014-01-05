package de.markeibes.markgraf

import android.util.{Log, AttributeSet}
import android.content.Context
import android.graphics._
import scala.collection.JavaConverters._
import scala.concurrent.future
import android.view.View.MeasureSpec
import android.view.{WindowManager, Display, MotionEvent, ScaleGestureDetector}
import android.graphics.drawable.Drawable

class HorizontalBarChart(val ctx:Context, as:AttributeSet, style:Integer, val v:HorizontalBarChartView){
  val pinchToZoomListener = new ScaleGestureDetector(ctx, new PinchToZoomListener)
  var barColor = Color.argb(100,40,90,240)
  var captionTextColour = Color.rgb(200,200,200)
  var captionTextSize = 30.0f
  var valueTextColour = Color.rgb(200,200,200)
  var mWidth, mHeight:Int = 1
  var barDrawable:Drawable = null
  var valueTextSize = 30.0f
  var barHeight = 50.0f
  val offsetFactor = 1.2f
//  lazy val barPaint = PaintFactory.makePaint(barColor)
  lazy val barPaint = PaintFactory.makeGradientPaint(Color.rgb(50,200,255), Color.rgb(50,230, 220), barHeight.toInt)
  lazy val captionTextPaint = PaintFactory.makeTextPaint(captionTextColour, captionTextSize)
  lazy val valueTextPaint = PaintFactory.makeTextPaint(captionTextColour, valueTextSize, textAlign=Paint.Align.RIGHT)

  def updateWidthAndHeight(width:Int, height:Int):WidthHeightTuple = {
    if(width > 0 && height > 0){
      mWidth = width
      mHeight = height
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
    val attrBarDrawable = typedArray.getResourceId(R.styleable.HorizontalBarChartView_barDrawable, 0)
    if(attrBarDrawable!=0) barDrawable = ctx.getResources.getDrawable(attrBarDrawable)
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

  def heightFromData():Int = (data.length * barHeight * offsetFactor).toInt + 1 + v.getPaddingTop + v.getPaddingBottom

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
  private var numberOfDataPoints = 0
  var maxValue:Float = 0

  def setData(list:java.util.List[DataPoint]) = {
    if (!list.isEmpty){
      data =  list.asScala.toList
      numberOfDataPoints = data.length
      maxValue = data.map(x => x.value).max
    }
  }

  def onTouchEvent(event:MotionEvent):Boolean = {
    pinchToZoomListener.onTouchEvent(event)
  }

  var scale = 1.0f
  var scaling = false
  class PinchToZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

    override def onScaleBegin(detector: ScaleGestureDetector): Boolean = {
      //Smoother scaling by not rendering text temporarily
      if(numberOfDataPoints > 200)
        scaling = true
      true
    }

    override def onScale(detector: ScaleGestureDetector): Boolean = {
      val f = detector.getScaleFactor
      if(numberOfDataPoints>0 && scale*f <= 1.0f && scale*f > 0.25f){
        scale *= f
        captionTextSize*=f
        captionTextPaint.setTextSize(captionTextSize)
        valueTextSize*=f
        valueTextPaint.setTextSize(valueTextSize)
        barHeight*=f
        v.requestLayout()
        v.invalidate()
      }
      true
    }

    override def onScaleEnd(detector: ScaleGestureDetector): Unit = {
      if(numberOfDataPoints > 200)
        scaling=false
      v.invalidate()
      true
    }
  }

  var visibleRect = new Rect()

  def onDraw(c:Canvas, offset:Int) {
    v.getWindowVisibleDisplayFrame(visibleRect)
    Log.e("Markgraf", "\nTop: " + visibleRect.top + "\nBottom: " + visibleRect.bottom)
    data.foldLeft(new Rect(0, 0, 0, barHeight.toInt))( (rect,dataPoint) => {
      rect.right = (dataPoint.value/maxValue*mWidth).toInt
      if(barDrawable!=null){
        barDrawable.setBounds(rect)
        barDrawable.draw(c)
      } else {
        c.drawRect(rect, barPaint)
      }

        if(captionTextSize>20 && !scaling)
          c.drawText(dataPoint.label, 0+captionTextSize*0.2f, barHeight+rect.top-captionTextSize-captionTextPaint.ascent()/3, captionTextPaint)
        if(valueTextSize>20 && !scaling)
          c.drawText("%3.0f" format dataPoint.value, mWidth-valueTextSize*0.2f, barHeight+rect.top-valueTextSize-valueTextPaint.ascent()/3, valueTextPaint)
        if(captionTextSize>30){
          rect.top+= (offsetFactor*barHeight).toInt
          rect.bottom += (offsetFactor*barHeight).toInt
          rect
        }
        else{
          rect.top+= barHeight.toInt
          rect.bottom += barHeight.toInt
          rect
        }
    })

    }
}
