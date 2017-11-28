package com.ift604.udes.myspot.Utility;

import com.ift604.udes.myspot.Entites.Drinking;
import com.ift604.udes.myspot.Entites.Player;

import java.util.Date;
import java.util.List;

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

    private static final double CTE1 = 1.0;
    private static final double CTE2 = 0.11786;
    private static final double CTE3 = 0.00357;
    private static final double CTE4 = 0.00025;

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

    public static double timeNeededToDrinkInSeconds(int level) {
        final int multi = 10;
        return multi*CTE4*Math.pow(level,3) - multi*CTE3*Math.pow(level,2) + multi*CTE2*level + CTE1;
    }

    public static double timeNeededToProcessStomachInMinutes(int level) {
        final int multi = 30;
        return multi*CTE4*Math.pow(level,3) - multi*CTE3*Math.pow(level,2) + multi*CTE2*level + multi*CTE1;
    }

    public static double timeNeededToProcessStrengthInHours(int level) {
        return CTE4*Math.pow(level,3) - CTE3*Math.pow(level,2) + CTE2*level + CTE1;
    }

    public static class Liquids { public double blader; public double stomach; }

    public static Liquids calculateLiquids(List<Drinking> drinkings, Player player) {
        double time = timeNeededToProcessStomachInMinutes(player.getLevel()) * 60 * 1000; // ml/ms
        double maxSize = levelToBladerSize(player.getLevel()); // ml;

        Functions.Liquids liquids = new Liquids();
        liquids.stomach = 0.0; // ml
        liquids.blader = player.getBlader(); // ml

        for (Drinking drinking : drinkings) {
            if (drinking.isEmptied()) {
                continue;
            }

            double timePercent = Math.min((new Date().getTime() - drinking.getDate().getTime()) / time, 1.0);
            double stomachPercent = Math.min(timePercent * maxSize / (drinking.getAmount() - drinking.getUsed()), 1.0);
            double used = drinking.getAmount() * stomachPercent - drinking.getUsed();

            liquids.blader += used;
            liquids.stomach += drinking.getAmount() - used;
        }

        return liquids;
    }
}
