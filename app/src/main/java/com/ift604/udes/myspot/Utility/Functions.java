package com.ift604.udes.myspot.Utility;

/**
 * Created by Squirrel on 2017-11-25.
 */

public class Functions {
    // https://www.vetinfo.com/normal-urinary-frequency-in-dogs.html
    private static final double LITER_PER_POUND = 0.2;
    private static final double PEE_TIME_SECONDS = 12.0;
    private static final double PEE_DELAY_SECONDS = 2.0;

    private static final double COS_OF_ZERO = 1.0;
    private static final double INTEGRAL_OF_SIN_X_FOR_0_TO_PI = 2.0;

    public static double levelToBladerSize(int level) {
        return level * LITER_PER_POUND; // 1 level = 1 pound
    }

    // https://www.researchgate.net/profile/Dirk_De_Ridder/publication/40030544/figure/fig9/AS:267725830356998@1440842220058/Figure-3-A-schematic-representation-of-urine-flow-over-time.png
    public static double urineFlowOverTime(double timeSinceStarted) {
        if(timeSinceStarted > PEE_TIME_SECONDS || timeSinceStarted < PEE_DELAY_SECONDS) {
            return 0;
        }
        return Math.sin((timeSinceStarted - PEE_DELAY_SECONDS) * Math.PI / PEE_TIME_SECONDS);
    }

    // Integral of urineFlowOverTime
    public static double urineFlowedOverTime(double timeSinceStarted) {
        if(timeSinceStarted > PEE_TIME_SECONDS || timeSinceStarted < PEE_DELAY_SECONDS) {
            return 0;
        }
        return - (Math.cos((timeSinceStarted - PEE_DELAY_SECONDS) * Math.PI / PEE_TIME_SECONDS) - COS_OF_ZERO) / INTEGRAL_OF_SIN_X_FOR_0_TO_PI;
    }
}
