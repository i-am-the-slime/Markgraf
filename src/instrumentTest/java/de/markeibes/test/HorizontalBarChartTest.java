package de.markeibes.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import de.markeibes.HorizontalBarChart;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class de.markeibes.test.HorizontalBarChartTest \
 * de.markeibes.tests/android.test.InstrumentationTestRunner
 */
public class HorizontalBarChartTest extends ActivityInstrumentationTestCase2<HorizontalBarChart> {
    Activity mActivity;
    TextView mTv;


    public HorizontalBarChartTest() {
        super(HorizontalBarChart.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        mActivity = getActivity();

        mTv = (TextView) mActivity.findViewById( de.markeibes.R.id.tv );
    }

    public void testPreConditions() {
        assertEquals("Shit", mTv.getText().toString());
    }

    public void testSetText() {
        mActivity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        mTv.requestFocus();
                        mTv.setText("Gruen");
                    }
                }
        );
    }
}
