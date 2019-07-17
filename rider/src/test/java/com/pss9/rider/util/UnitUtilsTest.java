package com.pss9.rider.util;

import com.pss9.rider.R;
import com.pss9.rider.presenter.module.Tick;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class UnitUtilsTest {
    @Test
    public void unitConversionToKMH() throws Exception {
        Assert.assertEquals(3.6,
                UnitUtils.INSTANCE.convertCalcSpeed(1, R.string.unit_speed_kmh),
                0.00001);
    }

    @Test
    public void unitConversionToMPH() throws Exception {

        Assert.assertEquals(2.23694,
                UnitUtils.INSTANCE.convertCalcSpeed(1, R.string.unit_speed_mph),
                0.00001);
    }

    @Test
    public void tickObserverTest() throws Exception {
        final int period = 3; // check for 3 times;
        final CountDownLatch count = new CountDownLatch(period);

        final long start = System.currentTimeMillis();
        final Tick ticker = new Tick(second -> {
            // Mock View handles updating seconds

            // should increment on each second
            Assert.assertEquals(second, (int) ((System.currentTimeMillis() - start) / 1000));
            count.countDown();
        });
        ticker.start();

        synchronized (count) {
            count.await(5, TimeUnit.SECONDS);
        }
        // should spend exactly the duration specified on period
        Assert.assertEquals(period, (int) ((System.currentTimeMillis() - start) / 1000));
    }
}
