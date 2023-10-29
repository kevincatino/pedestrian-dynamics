package ar.edu.itba.ss.step.models;

import java.util.Set;

public interface SimulationHeuristic {
    void advance(Pedestrian p, Set<Pedestrian> others, StepProcessor stepProcessor, double delta);

    static SimulationHeuristic getDefault() {
        return new SimulationHeuristic() {
            @Override
            public void advance(Pedestrian p, Set<Pedestrian> others, StepProcessor stepProcessor, double delta) {
                stepProcessor.advance(p, others, delta);
            }
        };
    }
}
