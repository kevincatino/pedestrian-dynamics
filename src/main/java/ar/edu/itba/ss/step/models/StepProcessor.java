package ar.edu.itba.ss.step.models;

import java.util.Set;

public interface StepProcessor {

    void advance(Pedestrian pedestrian, Set<Pedestrian> otherPedestrians, double delta);
}
