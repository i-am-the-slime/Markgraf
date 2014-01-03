package de.markeibes;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class de.markeibes.HorizontalBarChartTest \
 * de.markeibes.tests/android.test.InstrumentationTestRunner
 */
public class HorizontalBarChartTest extends ActivityInstrumentationTestCase2<HorizontalBarChart> {

    public HorizontalBarChartTest() {
        super("de.markeibes", HorizontalBarChart.class);
    }

}
