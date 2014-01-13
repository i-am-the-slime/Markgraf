package de.markeibes.markgraf;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;

import java.security.InvalidParameterException;
import java.util.*;

public class CalendarChartView extends View{
  final static Float COS60 = 0.5f;
  final static Float SIN60 = 0.86602540378f;
  final static Float SQRT3= 1.73205080757f;
  final static Float TAN60 = 1.73205080757f;
  final static Float ONE_SIXTH = 0.16666666667f;
  final static Float FIVE_SIXTHS = 0.83333333333f;
  private Context ctx;
  private int mWidth, mHeight = 0;
  private List<DataPoint<DateTime, Float>> data = new ArrayList<DataPoint<DateTime,Float>>();
  private Paint circlePaint = new Paint();
  private Paint textPaint = new Paint();
  private Paint fillPaint = new Paint();
  private Paint linePaint = new Paint();
  private Paint daysPaint = new Paint();
  private Paint smileyBackground = new Paint();
  private Paint smileyMouth = new Paint();
  private float middleOfText = 0;
  private float blockWidth = 0;
  Map<Integer, Float> weekdayAverages = new HashMap<Integer, Float>();
  Map<Integer, Float> weeklyAverages = new HashMap<Integer, Float>();
  private Calendar cal = Calendar.getInstance();

  private void init(Context context) {
    ctx = context;
    mixPaint();
  }

  private void mixPaint(){
    circlePaint.setColor(Color.rgb(30, 30, 30));
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setAntiAlias(true);
    textPaint.setColor(Color.rgb(30, 30, 30));
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setAntiAlias(true);
    linePaint.setColor(0xffAAAAAA);
    daysPaint.setColor(Color.rgb(30, 30, 30));
    fillPaint.setColor(Color.rgb(30, 30, 30));
    fillPaint.setStyle(Paint.Style.FILL);
    smileyBackground.setStyle(Paint.Style.STROKE);
    smileyBackground.setColor(Color.TRANSPARENT);
    smileyBackground.setAntiAlias(true);
    smileyMouth.setStyle(Paint.Style.STROKE);
    smileyMouth.setStrokeWidth(5);
    smileyMouth.setAntiAlias(true);
    smileyMouth.setStrokeCap(Paint.Cap.ROUND);
    smileyMouth.setColor(Color.rgb(30,30,30));
  }

  public CalendarChartView(Context context) {
    super(context);
    init(context);
  }

  public CalendarChartView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CalendarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public int calculateHeight(int mWidth) {
    return (int) ((mWidth / 8.0f) * 6);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mWidth = MeasureSpec.getSize(widthMeasureSpec);
    mHeight = calculateHeight(mWidth);
    int msH = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
    setMeasuredDimension(widthMeasureSpec, msH);
//    textPaint.setTextSize(mWidth / 16.0f);
    textPaint.setTextSize(mWidth / 48.0f);
    circlePaint.setStrokeWidth(0.1f * mWidth / 16.0f);
    middleOfText = (textPaint.ascent() + textPaint.descent()) / 2;
    blockWidth = mWidth/8;
  }

  public void setData(List<DataPoint<DateTime, Float>> dataPointsForOneMonth) throws InvalidParameterException {
    //TODO: Sort the data
    int month=0;
    if(!dataPointsForOneMonth.isEmpty()){
      month = dataPointsForOneMonth.get(0).label.getMonthOfYear();
    }
    for (DataPoint<DateTime, Float> dataPoint : dataPointsForOneMonth) {
      if (dataPoint.label.getMonthOfYear() != month) {
        throw new InvalidParameterException("The data provided is not all in the same month");
      }
      
      data = dataPointsForOneMonth;
      HashMap<Integer, ArrayList<Float>> weekdays = new HashMap<Integer, ArrayList<Float>>();
      HashMap<Integer, ArrayList<Float>> weeks = new HashMap<Integer, ArrayList<Float>>();
      for(DataPoint<DateTime, Float> dp: data){
        Integer weekday = dp.label.getDayOfWeek();
        ArrayList<Float> weekdayValues = weekdays.get(weekday);
        if(weekdayValues == null){
          weekdayValues = new ArrayList<Float>();
        }
        weekdayValues.add(dp.value);
        Integer week = dp.label.getDayOfMonth()/7 + 1;
        ArrayList<Float> weekValues = weekdays.get(weekday);
        if(weekValues == null){
          weekValues = new ArrayList<Float>();
        }
        weekValues.add(dp.value);
      }
      for(int i=1; i<=7; i++){
        ArrayList<Float> values = weekdays.get(i);
        if(values == null){
          values = new ArrayList<Float>();
        }
        values.add(dp.value);
      }
    }
  }

  public void setTypeface(Typeface tf){
    textPaint.setTypeface(tf);
  }

  final Path makePath(Float rotation, Float xPos, Float yPos, Path p, Float offsetX, Float offsetY){
    Float x = xPos;
    Float y = yPos;
    if(rotation==0){
      p.moveTo(offsetX+xPos, offsetY+yPos);
    } else {
      Float xRot = (float) (xPos*Math.cos(rotation) - yPos*Math.sin(rotation));
      Float yRot = (float) (xPos*Math.sin(rotation) + yPos*Math.cos(rotation));
      p.moveTo(offsetX+xRot, offsetY+yRot);
    }
    for(int i=0; i<5; i++){
      Float xNew = x*COS60 - y*SIN60;
      Float yNew = x*SIN60 + y*COS60;
      if(rotation==0){
        p.lineTo(offsetX+xNew, offsetY+yNew);
      } else {
        Float xRot = (float) (xNew*Math.cos(rotation) - yNew*Math.sin(rotation));
        Float yRot = (float) (xNew*Math.sin(rotation) + yNew*Math.cos(rotation));
        p.lineTo(offsetX+xRot, offsetY+yRot);
      }
      x = xNew;
      y = yNew;
    }
    p.close();
    return p;
  }

  final Path makeFill(Float xPos, Float yPos, Path p, Float offsetx, Float offsety, Float fill, Float dampening) {
    //Edge length
    Float s = (float) Math.sqrt(xPos*xPos + yPos*yPos);
    Float totalArea = (xPos*xPos + yPos*yPos)*1.5f*SQRT3;
    Float area = fill * totalArea;
    p.moveTo(offsetx + xPos, offsety + yPos);
    if(fill <= ONE_SIXTH){
      Float x = (float)(Math.sqrt(area/2)*TAN60);
      Float y = x/TAN60;
      Float left = xPos+offsetx-x;
      Float right = xPos+offsetx+x;
      Float top = yPos+offsety-y;
      Float bottom = yPos+offsety;
      p.lineTo(xPos+offsetx-x, yPos+offsety-y);
//      drawWave(left, right, top, p, fill, dampening);
      p.lineTo(xPos + offsetx + x, yPos + offsety - y);
    }
    else if(fill <= FIVE_SIXTHS){
      Float x = s*SIN60;
      Float y = s*COS60;
      Float ySquare = (area-ONE_SIXTH*totalArea)/(2*x);

      //Helpers
      Float middle = xPos+offsetx;
      Float left = middle-x;
      Float right = middle+x;
      Float bottom = yPos+offsety-y;
      Float top = bottom-ySquare;

      p.lineTo(left, bottom);
      p.lineTo(left, top);
      p.lineTo(xPos + offsetx + x, yPos + offsety - y - ySquare);
//      drawWave(left, right, top, p, fill, dampening);
      p.lineTo(right, bottom);
    } else {
      Float lowX = s*SIN60;
      Float lowY = s*COS60;
      Float middleY = s+lowY;
      Float x = (float)(Math.sqrt((totalArea-area)/2)*TAN60);
      Float y = x/TAN60;

      p.lineTo(xPos + offsetx - lowX, yPos + offsety - lowY);
      p.lineTo(xPos + offsetx - lowX, yPos + offsety - middleY);
      p.lineTo(xPos + offsetx - x, yPos + offsety - middleY - (lowY - y));
//      drawWave(xPos + offsetx - x, xPos + offsetx + x, yPos + offsety - middleY - (lowY - y), p, fill, dampening);
      p.lineTo(xPos + offsetx + x, yPos + offsety - middleY - (lowY - y));
      p.lineTo(xPos + offsetx + lowX, yPos + offsety - middleY);
      p.lineTo(xPos + offsetx + lowX, yPos + offsety - lowY);
    }
    p.close();
    return p;
  }

  public Canvas drawSmiley(float happiness, float x, float y, float radius, Path p, Canvas c, Paint stroke, Paint fill) {
    //Check for invalid values
    if(happiness < 0.0f || happiness > 5.0f){ return c; }
    //Draw face
    c.drawCircle(x, y, radius, smileyBackground);
    //Draw right eye
    c.drawCircle(x+0.4f*radius, y-radius/3, radius/7, fill);
    //Draw left eye
    c.drawCircle(x-0.4f*radius, y-radius/3, radius/7, fill);
    //Draw mouth
    p.reset();
    float lipOffset = (2.5f - happiness)/5;
    float mouthY = y + 0.30f*radius + lipOffset * 0.2f *radius;
    float mouthXFromCenter = 0.02f*happiness*radius+ 0.50f*radius;
    p.moveTo(x-mouthXFromCenter, mouthY);
    p.quadTo(x,mouthY-lipOffset*(2.0f*radius), x+mouthXFromCenter, mouthY);
    if(happiness>3.5f){
      p.close();
      c.drawPath(p, fill);
    } else{
      c.drawPath(p, smileyMouth);
    }

    return c;
  }

  Path p = new Path();

  @Override
  protected void onDraw(Canvas canvas) {

    cal.set(2014, 00, 06);
    for(int day=0; day<8; day++){
      Locale loc = ctx.getResources().getConfiguration().locale;
      String currentDay;
      textPaint.setTextSize(mWidth/20.0f);
      if(day<7){
        currentDay = cal.getDisplayName(java.util.Calendar.DAY_OF_WEEK, Calendar.SHORT, loc);
        canvas.drawText(currentDay.substring(0, 3), day * blockWidth + 0.5f * blockWidth, 0.5f * blockWidth, textPaint);
      } else {
        currentDay = "O";
        canvas.drawText(currentDay, day * blockWidth + 0.5f * blockWidth, 0.5f * blockWidth, textPaint);
        canvas.drawText("/", day * blockWidth + 0.5f * blockWidth, 0.5f * blockWidth, textPaint);
      }
      canvas.drawLine((day+1) * blockWidth, 0, (day+1) * blockWidth , mHeight, linePaint);
      textPaint.setTextSize(mWidth / 24.0f);
      cal.add(Calendar.DAY_OF_WEEK, 1);
    }
    //Draw dividing line
    canvas.drawLine(0, 0.75f*blockWidth, mWidth, 0.75f *blockWidth, linePaint);

    for(DataPoint<DateTime, Float> dp : data){
      float x = (dp.label.getDayOfWeek()-1) * 1.0f*blockWidth + 0.5f*blockWidth;
      float y = ((dp.label.getDayOfMonth()+1)/7)*(blockWidth+blockWidth/16) + 1.5f*blockWidth;
      drawSmiley(dp.value, x, y-0.12f*blockWidth, 0.47f*blockWidth, p, canvas, circlePaint, textPaint);

      //Hexagons
//      p.reset();
//      canvas.drawPath(makePath(0f, 0f, 0.5f * blockWidth, p, x, y), circlePaint);
//      p.reset();
//      canvas.drawPath(makeFill(0f,0.40f*blockWidth, p, x, y, dp.value, 1.0f), fillPaint);
    }


    //Draw the numbers of the calendar
    if(!data.isEmpty()) {
      textPaint.setTextAlign(Paint.Align.RIGHT);
      MutableDateTime dt = new MutableDateTime(data.get(0).label);
      for (int day = 1; day <= dt.dayOfMonth().getMaximumValue(); day++) {
        dt.setDayOfMonth(day);
//        float x = (dt.getDayOfWeek() - 1) * 1.0f*blockWidth + 1.5f*blockWidth;
        float x = (dt.getDayOfWeek() -1) * 1.0f*blockWidth + 1.5f*blockWidth;
        float y = ((dt.getDayOfMonth()+1)/7) * (blockWidth+blockWidth/16) - middleOfText + 1.5f*blockWidth;
        canvas.drawText(String.valueOf(day), x-blockWidth/2, y-blockWidth/2, textPaint);
        if(dt.getDayOfWeek()==1 && dt.getDayOfMonth()!=1){
          float lineY = ((dt.getDayOfMonth()+1)/7) * (blockWidth+blockWidth/16) + 4*middleOfText + blockWidth;
          canvas.drawLine(0, lineY, mWidth, lineY, linePaint);
        }
      }
      textPaint.setTextAlign(Paint.Align.CENTER);
    }
  }

  //For testing smileys please keep in
//  @Override
//  protected void onDraw(Canvas canvas){
//    for(int j=0; j<5; j++){
//      for(int i=0; i<9; i++){
//        drawSmiley((i+9*j)/7.5f, i/10f*mWidth+1/10f*mWidth, j/10f*mWidth+1/10f*mWidth, 0.05f*mWidth, p, canvas, circlePaint, textPaint);
//      }
//    }
//  }
}
