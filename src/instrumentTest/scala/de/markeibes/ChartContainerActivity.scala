package de.markeibes

import android.app.Activity
import android.os.Bundle

/**
 * Created by mark on 03.01.14.
 */
class ChartContainerActivity extends Activity{
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
  }
}
