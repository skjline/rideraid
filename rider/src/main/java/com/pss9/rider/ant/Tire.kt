package com.pss9.rider.ant

import androidx.annotation.LongDef

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

object Tire {

    // num should be divided by 100000
    const val CIRCUMFERENCE_MM_20: Long = 207973
    const val CIRCUMFERENCE_MM_23: Long = 209858
    const val CIRCUMFERENCE_MM_25: Long = 211115
    const val CIRCUMFERENCE_MM_28: Long = 213000
    const val CIRCUMFERENCE_MM_32: Long = 215513
    const val CIRCUMFERENCE_MM_35: Long = 217398
    const val CIRCUMFERENCE_MM_38: Long = 219283
    const val CIRCUMFERENCE_MM_44: Long = 223053
    const val CIRCUMFERENCE_MM_50: Long = 226823
    const val CIRCUMFERENCE_MM_56: Long = 230593

    // num should be divided by 100
    const val CIRCUMFERENCE_IN_100: Long = 211366
    const val CIRCUMFERENCE_IN_125: Long = 215356
    const val CIRCUMFERENCE_IN_150: Long = 219346
    const val CIRCUMFERENCE_IN_175: Long = 223336
    const val CIRCUMFERENCE_IN_195: Long = 226509
    const val CIRCUMFERENCE_IN_200: Long = 227326
    const val CIRCUMFERENCE_IN_210: Long = 228922
    const val CIRCUMFERENCE_IN_2125: Long = 229336
    const val CIRCUMFERENCE_IN_220: Long = 230518
    const val CIRCUMFERENCE_IN_225: Long = 231315
    const val CIRCUMFERENCE_IN_230: Long = 232113
    const val CIRCUMFERENCE_IN_235: Long = 232911
    const val CIRCUMFERENCE_IN_240: Long = 233540

    @Retention(RetentionPolicy.SOURCE)
    @LongDef(
        CIRCUMFERENCE_MM_20,
        CIRCUMFERENCE_MM_23,
        CIRCUMFERENCE_MM_25,
        CIRCUMFERENCE_MM_28,
        CIRCUMFERENCE_MM_32,
        CIRCUMFERENCE_MM_35,
        CIRCUMFERENCE_MM_38,
        CIRCUMFERENCE_MM_44,
        CIRCUMFERENCE_MM_50,
        CIRCUMFERENCE_MM_56
    )
    annotation class TireCircumferenceMM

    @Retention(RetentionPolicy.SOURCE)
    @LongDef(
        CIRCUMFERENCE_IN_100,
        CIRCUMFERENCE_IN_125,
        CIRCUMFERENCE_IN_150,
        CIRCUMFERENCE_IN_175,
        CIRCUMFERENCE_IN_195,
        CIRCUMFERENCE_IN_200,
        CIRCUMFERENCE_IN_210,
        CIRCUMFERENCE_IN_2125,
        CIRCUMFERENCE_IN_220,
        CIRCUMFERENCE_IN_225,
        CIRCUMFERENCE_IN_230,
        CIRCUMFERENCE_IN_235,
        CIRCUMFERENCE_IN_240
    )

    annotation class TireCircumferenceIN

    //    public enum TIRE_700C {
    //        MM_20 ("700C/20mm", (float) 2.07973),
    //        MM_23 ("700C/23mm", (float) 2.09858),
    //        MM_25 ("700C/25mm", (float) 2.11115),
    //        MM_28 ("700C/28mm", (float) 2.13000),
    //        MM_32 ("700C/32mm", (float) 2.15513),
    //        MM_35 ("700C/35mm", (float) 2.17398),
    //        MM_38 ("700C/38mm", (float) 2.19283),
    //        MM_44 ("700C/44mm", (float) 2.23053),
    //        MM_50 ("700C/50mm", (float) 2.26823),
    //        MM_56 ("700C/56mm", (float) 2.30593),
    //
    //        IN_100 ("700C/1.00in", 2113.66f),
    //        IN_125 ("700C/1.25in", 2153.56f),
    //        IN_150 ("700C/1.50in", 2193.46f),
    //        IN_175 ("700C/1.75in", 2233.36f),
    //        IN_195 ("700C/1.95in", 2265.09f),
    //        IN_200 ("700C/2.00in", 2273.26f),
    //        IN_210 ("700C/2.10in", 2289.22f),
    //        IN_2125 ("700C/2.125in", 2293.36f),
    //        IN_220 ("700C/2.20in", 2305.18f),
    //        IN_225 ("700C/2.25in", 2313.15f),
    //        IN_230 ("700C/2.30in", 2321.13f),
    //        IN_235 ("700C/2.35in", 2329.11f),
    //        IN_240 ("700C/2.40in", 2335.40f);
    //
    //        final float circumference;
    //        final String description;
    //
    //        TIRE_700C(String desc, float value) {
    //            this.description = desc;
    //            circumference = value;
    //        }
    //
    //        public String getDescription() {
    //            return description;
    //        }
    //
    //        public float getCircumference() {
    //            return circumference;
    //        }
    //    }
}
