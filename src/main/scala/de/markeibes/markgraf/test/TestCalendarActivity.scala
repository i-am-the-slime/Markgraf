package de.markeibes.markgraf.test

import android.app.Activity
import android.os.Bundle
import de.markeibes.markgraf.{CalendarChartView, DataPoint, R}
import android.graphics.Typeface
import java.util
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class TestCalendarActivity extends Activity{

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.calendar_chart_test)
    val chart = findViewById(R.id.calendar_chart).asInstanceOf[CalendarChartView]
    val lato = Typeface.createFromAsset(getAssets, "fonts/lato-reg.ttf")
    chart.setTypeface(lato)

    getActionBar.hide()
    val javaList2:java.util.ArrayList[DataPoint[DateTime, java.lang.Float]] = new util.ArrayList[DataPoint[DateTime, java.lang.Float]]()
    val format = DateTimeFormat.forPattern("yyyy-MM-dd")
    List(
      (DateTime.parse("2014-01-01", format), 2.0f),
      (DateTime.parse("2014-01-02", format), 4.0f),
      (DateTime.parse("2014-01-17", format), 2.0f),
      (DateTime.parse("2014-01-03", format), 2.5f),
      (DateTime.parse("2014-01-04", format), 0.0f),
      (DateTime.parse("2014-01-20", format), 4.2f)
      ).foreach(
        x => javaList2.add(new DataPoint[DateTime, java.lang.Float](x._1, x._2))
      )
    chart.setData(javaList2)
  }
}
