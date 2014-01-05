package de.markeibes.markgraf;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HorizontalBarChartView extends View
{
    private Context ctx;
    private ScaleGestureDetector pinchToZoomListener;
    private int mWidth = 1;
    private float textSize = 30.0f;
    private int textColour = Color.rgb(200, 200, 200);
    private float barHeight = 40.0f;
    private Drawable barDrawable = new ColorDrawable(Color.rgb(80,120,200));
    private float offsetFactor = 1.2f;
    private Paint labelTextPaint = new Paint();
    private Paint valueTextPaint = new Paint();
    private Rect r = new Rect();

    //Pinch and zoom
    private boolean scaling = false;
    private float scale = 1.0f;

    //Data
    private List<DataPoint> data = new ArrayList<DataPoint>();
    private float largestDataPoint = 0;
    private int numberOfDataPoints = 0;

    public void mixPaint(){
       labelTextPaint.setColor(textColour);
       labelTextPaint.setTextSize(textSize);
       valueTextPaint.setColor(textColour);
       valueTextPaint.setTextSize(textSize);
       valueTextPaint.setTextAlign(Paint.Align.RIGHT);
    }

    public void init(Context context, AttributeSet as){
        ctx = context;
        if(as!=null) setAttributes(as);
        mixPaint();
        pinchToZoomListener = new ScaleGestureDetector(ctx, new PinchToZoomListener());
        //Bars are all left aligned.
        r.left = getPaddingLeft();
    }

    public HorizontalBarChartView(Context context) {
        super(context);
        init(context, null);
    }

    public HorizontalBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalBarChartView(Context context, AttributeSet attrs, int styleInt) {
        super(context, attrs, styleInt);
        init(context, attrs);
    }

    public void setAttributes(AttributeSet as){
        Resources.Theme theme = ctx.getTheme();
        if(theme!=null){
            TypedArray typedArray = theme.obtainStyledAttributes(as, R.styleable.HorizontalBarChartView, 0, 0);
            if(typedArray!=null){
                textColour = typedArray.getColor(R.styleable.HorizontalBarChartView_textColor, textColour);
                textSize = typedArray.getDimension(R.styleable.HorizontalBarChartView_textSize, textSize);
                barHeight = typedArray.getDimension(R.styleable.HorizontalBarChartView_barHeight, barHeight);
                int barDrawableResource = typedArray.getResourceId(R.styleable.HorizontalBarChartView_barDrawable, 0);
                if(barDrawableResource != 0){
                    barDrawable = ctx.getResources().getDrawable(barDrawableResource);
                }
                typedArray.recycle();
            }
        }
    }

    public void setData(List<DataPoint> dataPoints) {
        data = dataPoints;
        largestDataPoint = 0;
        for(DataPoint p : dataPoints){
            if(p.value > largestDataPoint){
                largestDataPoint = p.value;
            }
        }
        numberOfDataPoints = data.size();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int msH = MeasureSpec.makeMeasureSpec(heightFromData(), MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, msH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return pinchToZoomListener.onTouchEvent(event);
    }

    public void setTypeface(Typeface tf){
        valueTextPaint.setTypeface(tf);
        labelTextPaint.setTypeface(tf);
    }

    public int heightFromData() {
        if(textSize*scale>30)
            return (int)(scale*numberOfDataPoints * barHeight * offsetFactor) +1 + getPaddingTop() + getPaddingBottom();
        else
            return (int)(scale*numberOfDataPoints * barHeight) + 1 +getPaddingBottom() + getPaddingTop();
    }

    class PinchToZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            if(numberOfDataPoints > 200)
                scaling = true;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float f = detector.getScaleFactor();
            if(numberOfDataPoints>0 && scale*f <= 1.0f && scale*f > 0.25f){
                scale *= f;
                labelTextPaint.setTextSize(textSize*scale);
                valueTextPaint.setTextSize(textSize*scale);
                requestLayout();
            }
            return super.onScale(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            scaling = false;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas c) {
        r.top =  getPaddingTop();
        r.bottom = (int)(barHeight*scale)+getPaddingTop();

        for(DataPoint p : data) {
            r.right = (int)(p.value/largestDataPoint*(mWidth-getPaddingLeft()-getPaddingRight())) + getPaddingRight();
            barDrawable.setBounds(r);
            barDrawable.draw(c);

            if(textSize*scale>10 && !scaling){
                c.drawText(p.label, scale*textSize*0.2f+getPaddingLeft(), scale*barHeight+r.top-textSize*scale-labelTextPaint.ascent()/3, labelTextPaint);
                c.drawText(p.value.toString(), mWidth-getPaddingRight()-0.2f*textSize, scale*barHeight+r.top-textSize*scale-valueTextPaint.ascent()/3, valueTextPaint);
            }
            if(textSize*scale>30){
                r.top += (int)(offsetFactor*barHeight*scale);
                r.bottom += (int)(offsetFactor*barHeight*scale);
            }
            else{
                r.top += (int) barHeight*scale;
                r.bottom += (int) barHeight*scale;
            }
        }
    }
}
