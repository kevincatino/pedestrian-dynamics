package ar.edu.itba.ss.step.models;

import java.util.Set;

public interface SimulationHeuristic {
    double advance(Pedestrian p, Set<Pedestrian> others, StepProcessor stepProcessor, double time);

    static SimulationHeuristic getDefault() {
        return new SimulationHeuristic() {
            @Override
            public double advance(Pedestrian p, Set<Pedestrian> others, StepProcessor stepProcessor, double time) {
                return stepProcessor.advance(p, others, time);
            }
        };
    }
}
