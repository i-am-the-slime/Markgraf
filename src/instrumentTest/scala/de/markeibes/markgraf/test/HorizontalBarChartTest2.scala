package de.markeibes.markgraf.test

import android.test.ActivityInstrumentationTestCase2
import de.markeibes.markgraf.{HorizontalBarChartView, R}

class HorizontalBarChartTest2 extends ActivityInstrumentationTestCase2[TestActivity](classOf[TestActivity]) {
  lazy val activity = getActivity
  lazy val horizontalBC = activity.findViewById(R.id.horiz_bc_view).asInstanceOf[HorizontalBarChartView]

  override def setUp(): Unit = {
    super.setUp()
    setActivityInitialTouchMode(false)
    assert(activity!=null)
    assert(horizontalBC!=null)
  }

  def testPreConditions() = {
//    assertEquals("Shit", tv.getText.toString)
  }
}
