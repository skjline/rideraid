package com.skjline.rideraid.ant;

import androidx.annotation.LongDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Tire {
    @Retention(RetentionPolicy.SOURCE)
    @LongDef({
            CIRCUMFERENCE_MM_20,
            CIRCUMFERENCE_MM_23,
            CIRCUMFERENCE_MM_25,
            CIRCUMFERENCE_MM_28,
            CIRCUMFERENCE_MM_32,
            CIRCUMFERENCE_MM_35,
            CIRCUMFERENCE_MM_38,
            CIRCUMFERENCE_MM_44,
            CIRCUMFERENCE_MM_50,
            CIRCUMFERENCE_MM_56})
    public @interface TireCircumferenceMM {}

    @Retention(RetentionPolicy.SOURCE)
    @LongDef({
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
            CIRCUMFERENCE_IN_240})
    public @interface TireCircumferenceIN {}

    // num should be divided by 100000
    public static final long CIRCUMFERENCE_MM_20 = 207973;
    public static final long CIRCUMFERENCE_MM_23 = 209858;
    public static final long CIRCUMFERENCE_MM_25 = 211115;
    public static final long CIRCUMFERENCE_MM_28 = 213000;
    public static final long CIRCUMFERENCE_MM_32 = 215513;
    public static final long CIRCUMFERENCE_MM_35 = 217398;
    public static final long CIRCUMFERENCE_MM_38 = 219283;
    public static final long CIRCUMFERENCE_MM_44 = 223053;
    public static final long CIRCUMFERENCE_MM_50 = 226823;
    public static final long CIRCUMFERENCE_MM_56 = 230593;

    // num should be divided by 100
    public static final long CIRCUMFERENCE_IN_100 = 211366;
    public static final long CIRCUMFERENCE_IN_125 = 215356;
    public static final long CIRCUMFERENCE_IN_150 = 219346;
    public static final long CIRCUMFERENCE_IN_175 = 223336;
    public static final long CIRCUMFERENCE_IN_195 = 226509;
    public static final long CIRCUMFERENCE_IN_200 = 227326;
    public static final long CIRCUMFERENCE_IN_210 = 228922;
    public static final long CIRCUMFERENCE_IN_2125 = 229336;
    public static final long CIRCUMFERENCE_IN_220 = 230518;
    public static final long CIRCUMFERENCE_IN_225 = 231315;
    public static final long CIRCUMFERENCE_IN_230 = 232113;
    public static final long CIRCUMFERENCE_IN_235 = 232911;
    public static final long CIRCUMFERENCE_IN_240 = 233540;

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
