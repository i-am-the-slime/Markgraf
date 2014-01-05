Markgraf Graph Library
=======================================

For the moment there is only HorizontalBarChartView which can be used like this:


```xml
<?xml version="1.0" encoding="utf-8"?>
<de.markeibes.markgraf.MarkgrafHorizontalScrollView
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:markgraf="http://schemas.android.com/apk/res/de.markeibes.markgraf"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <de.markeibes.markgraf.HorizontalBarChartView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:padding="30dp"
                android:id="@+id/horiz_bc_view"
                markgraf:barColor="@android:color/holo_red_dark"
                markgraf:textColor="@android:color/white"
                markgraf:barHeight="50dip"
                markgraf:captionTextSize="19sp"
                markgraf:valueTextSize="19sp"
                markgraf:barDrawable="@drawable/turquoise_gradient"
                />
</de.markeibes.markgraf.MarkgrafHorizontalScrollView>
```


To add data simply do this
```java
import de.markeibes.markgraf.DataPoint;
import de.markeibes.markgraf.HorizontalBarChartView;

ArrayList<DataPoint> someJavaList = new ArrayList<DataPoint>();
// fill the list

// find the bar chart by doing
HorizontalBarChartView horizontalBarChart = (HorizontalBarChartView)findViewById(R.id.horiz_bc_view);
horizontalBarChart.setData(someJavaList);

```