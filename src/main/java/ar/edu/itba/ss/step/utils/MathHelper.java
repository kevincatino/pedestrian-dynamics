package ar.edu.itba.ss.step.utils;

import java.util.List;

public interface MathHelper {
    static double calculateSD(List<Double> numArray)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    static double modulo(double val, double mod) {
        return (val % mod + mod) % mod;
    }
}
