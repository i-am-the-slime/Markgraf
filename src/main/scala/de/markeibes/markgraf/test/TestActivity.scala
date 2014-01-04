package de.markeibes.markgraf.test

import android.app.Activity
import android.os.Bundle
import de.markeibes.markgraf.{DataPoint, HorizontalBarChartView, R}
import java.util
import android.graphics.Typeface

class TestActivity extends Activity {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.horizontal_bar_chart_test)
    val horizontalBarChart = findViewById(R.id.horiz_bc_view).asInstanceOf[HorizontalBarChartView]

    val javaList2:java.util.ArrayList[DataPoint] = new util.ArrayList[DataPoint]()
    List(("Bier", 22.0f),
      ("Nagetier", 12.0f),
      ("Kerle", 12.0f),
      ("Kerle", 13.0f),
      ("Kerle", 14.0f),
      ("Kerle", 15.0f),
      ("Kerle", 16.0f),
      ("Kerle", 11.0f),
      ("Kerle", 02.0f),
      ("Kerle", 12.0f),
      ("Nagetier", 12.0f),
      ("Raseoiongesicht", 44.0f)).foreach(
        x => javaList2.add(new DataPoint(x._1, x._2))
      )
    Thread.sleep(500)
    horizontalBarChart.setData(javaList2)
  }
}
