package de.markeibes.test

import android.test.ActivityInstrumentationTestCase2
import android.app.Activity
import android.widget.TextView
import junit.framework.Assert._
import de.markeibes.HorizontalBarChart

class HorizontalBarChartTest2 extends ActivityInstrumentationTestCase2[HorizontalBarChart](classOf[HorizontalBarChart]) {
  lazy val activity = getActivity
  lazy val tv = activity.findViewById(de.markeibes.R.id.tv).asInstanceOf[TextView]

  override def setUp(): Unit = {
    super.setUp()
    setActivityInitialTouchMode(false)
    assert(activity!=null)
    assert(tv!=null)
  }

  def testPreConditions() = {
    assertEquals("Shit", tv.getText.toString)
  }
}
