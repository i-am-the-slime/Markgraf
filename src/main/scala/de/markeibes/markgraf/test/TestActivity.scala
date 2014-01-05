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
    horizontalBarChart.setTypeface(Typeface.createFromAsset(getAssets, "fonts/lato-reg.ttf"))
    val javaList2:java.util.ArrayList[DataPoint] = new util.ArrayList[DataPoint]()
    List(("Bier", 1.0f),
      ("Nagetier", 2.0f),
      ("Schmatzmeister", 7.0f),
      ("Ratte", 9.0f),
      ("Biber", 2.0f),
      ("Nagetier", 2.0f),
      ("Neinnein", 3.0f),
      ("Schmatzmeister", 7.0f),
      ("Ratte", 9.0f),
      ("Biber", 2.0f),
      ("Nagetier", 2.0f),
      ("Neinnein", 3.0f),      ("Schmatzmeister", 7.0f),
      ("Ratte", 9.0f),
      ("Biber", 2.0f),
      ("Nagetier", 2.0f),
      ("Neinnein", 3.0f),      ("Schmatzmeister", 7.0f),
      ("Ratte", 9.0f),
      ("Biber", 2.0f),
      ("Nagetier", 2.0f),
      ("Neinnein", 3.0f),      ("Schmatzmeister", 7.0f),
      ("Ratte", 9.0f),
      ("Biber", 2.0f),
      ("Nagetier", 2.0f),
      ("Neinnein", 3.0f),
      ("Raseoiongesicht", 4.0f)).foreach(
        x => javaList2.add(new DataPoint(x._1, x._2))
      )
    Thread.sleep(500)
    horizontalBarChart.setData(javaList2)
  }
}
