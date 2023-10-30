package ar.edu.itba.ss.step.utils;

import ar.edu.itba.ss.step.models.Pair;

import java.util.List;
import java.util.function.Function;

public interface MathHelper {
    static <V> double calculateMSE(List<V> dataPoints, Function<V, Pair<Double,Double>> mapper) {
        if (dataPoints.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }

        double sumOfSquaredErrors = 0.0;

        for (V dataPoint : dataPoints) {
            Pair<Double,Double> pair = mapper.apply(dataPoint);
            double error = pair.getOne() - pair.getOther();
            sumOfSquaredErrors += Math.pow(error, 2);
        }

        double mse = sumOfSquaredErrors / dataPoints.size();
        return mse;
    }

    static <V> double calculateWeightedMSE(List<V> dataPoints, Function<V, Pair<Double,Double>> mapper, List<Double> weights) {
        if (dataPoints.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }

        double sumOfSquaredErrors = 0.0;
        int idx = 0;
        for (V dataPoint : dataPoints) {
            Pair<Double,Double> pair = mapper.apply(dataPoint);
            double error = pair.getOne() - pair.getOther();
            sumOfSquaredErrors += weights.get(idx)*Math.pow(error, 2);
        }

        double mse = sumOfSquaredErrors / weights.stream().reduce((a,b)->a+b).get();
        return mse;
    }
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
