package com.pss9.rideraid;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 * TODO: Update to use with Instrumentation Registry
 */
@RunWith(AndroidJUnit4.class)
public class RiderAidInstrumentedTest {
    private Context context;

    @Before
    public void useAppContext() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void validApplicationContextLoaded() throws Exception {
        assertEquals("com.skjline.rideraid", context.getPackageName());
    }
}