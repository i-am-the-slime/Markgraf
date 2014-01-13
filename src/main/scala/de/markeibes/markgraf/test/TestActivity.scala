package de.markeibes.markgraf.test

import android.app.Activity
import android.os.Bundle
import de.markeibes.markgraf.{DataPoint, HorizontalBarChartView, R}
import java.util
import android.graphics.Typeface
import android.widget.TextView

class TestActivity extends Activity {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.horizontal_bar_chart_test)
    val horizontalBarChart = findViewById(R.id.horiz_bc_view).asInstanceOf[HorizontalBarChartView]
    val title = findViewById(R.id.title).asInstanceOf[TextView]
    val lato = Typeface.createFromAsset(getAssets, "fonts/lato-reg.ttf")
    title.setTypeface(lato)
    horizontalBarChart.setTypeface(lato)

    getActionBar.hide
    val javaList2:java.util.ArrayList[DataPoint[java.lang.String, java.lang.Float]] = new util.ArrayList[DataPoint[String, java.lang.Float]]()
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
      ("Neinnein", 3.0f),
      ("Schmatzmeister", 7.0f),
      ("Ratte", 9.0f),
      ("Biber", 2.0f),
      ("Nagetier", 2.0f),
      ("Neinnein", 3.0f),
      ("Schmatzmeister", 7.0f),
      ("Ratte", 9.0f),
      ("Biber", 2.0f),
      ("Nagetier", 2.0f),
      ("Neinnein", 3.0f),
      ("Schmatzmeister", 7.0f),
      ("Ratte", 9.0f),
      ("Biber", 2.0f),
      ("Nagetier", 2.0f),
      ("Neinnein", 3.0f),
      ("Raseoiongesicht", 4.0f)).foreach(
        x => javaList2.add(new DataPoint[String, java.lang.Float](x._1, x._2))
      )
    horizontalBarChart.setData(javaList2)
  }
}
