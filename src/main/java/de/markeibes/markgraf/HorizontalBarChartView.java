package de.markeibes.markgraf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import de.markeibes.markgraf.HorizontalBarChart;

import java.util.List;

public class HorizontalBarChartView extends View
{
    private HorizontalBarChart hbc;

    public HorizontalBarChartView(Context context) {
        super(context);
        Log.e("markgraf", "constructor 1");
        hbc = new HorizontalBarChart(context, null, null, this);
    }

    public HorizontalBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("markgraf", "constructor 1");
        hbc = new HorizontalBarChart(context, attrs, null, this);
    }

    public HorizontalBarChartView(Context context, AttributeSet attrs, int styleInt) {
        super(context, attrs, styleInt);
        Log.e("markgraf", "constructor 1");
        hbc = new HorizontalBarChart(context, attrs, styleInt, this);
    }

    public void setData(List<DataPoint> dataPoints) {
        hbc.setData(dataPoints);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        HorizontalBarChart.WidthHeightTuple widthAndHeight = hbc.onSizeChanged(w,h);
        super.onSizeChanged(widthAndHeight.w(), widthAndHeight.h(), oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        HorizontalBarChart.WidthHeightTuple widthAndHeight = hbc.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int msW = MeasureSpec.makeMeasureSpec(widthAndHeight.w(), MeasureSpec.EXACTLY);
        int msH = MeasureSpec.makeMeasureSpec(widthAndHeight.h(), MeasureSpec.EXACTLY);
        setMeasuredDimension(msW, msH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return hbc.onTouchEvent(event);
    }

    public void setTypeface(Typeface tf){
        hbc.setTypeface(tf);
    }

    int[] location = {0,0};

    @Override
    protected void onDraw(Canvas canvas) {
        getLocationOnScreen(location);
        hbc.onDraw(canvas, location[1]);
    }

}
