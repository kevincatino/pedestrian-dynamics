package ar.edu.itba.ss.step.models;

import java.util.Iterator;

public interface Simulation {
    Iterator<Pair<Double, Pedestrian>> simulate(double delta, double maxTime, Pedestrian pedestrian);
}
