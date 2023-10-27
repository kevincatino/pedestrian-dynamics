package ar.edu.itba.ss.step.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SimulationEngine {




    public Iterator<Pair<Double,Pedestrian>> simulate(Pedestrian pedestrian, StepProcessor stepProcessor, List<Pair<Double, Set<Pedestrian>>> othersIterator, SimulationHeuristic heuristic) {
        return new PedestrianIterator(pedestrian, othersIterator, stepProcessor, heuristic);
    }

    public Iterator<Pair<Double,Pedestrian>> simulate(Pedestrian pedestrian,  StepProcessor stepProcessor, double delta, double maxTime) {
        List<Pair<Double,Set<Pedestrian>>> list = new ArrayList<>();
        for (double time = 0; time < maxTime ; time+=delta) {
            list.add(Pair.of(time, Collections.emptySet()));
        }
        return simulate(pedestrian, stepProcessor, list, SimulationHeuristic.getDefault());
    }

    static class PedestrianIterator implements Iterator<Pair<Double,Pedestrian>> {
        private final Pedestrian pedestrian;


        private final List<Pair<Double, Set<Pedestrian>>> othersList;

        private final StepProcessor stepProcessor;

        private double time;

        private final SimulationHeuristic heuristic;
        private int idx;

        PedestrianIterator(Pedestrian pedestrian, List<Pair<Double, Set<Pedestrian>>> othersList, StepProcessor stepProcessor, SimulationHeuristic heuristic) {
            this.pedestrian = pedestrian;
            this.othersList = othersList;
            this.heuristic = heuristic;
            this.stepProcessor = stepProcessor;
        }

        @Override
        public boolean hasNext() {
            return idx < othersList.size()-1;
        }

        @Override
        public Pair<Double,Pedestrian> next() {
            Pair<Double,Pedestrian> toReturn = Pair.of(time,pedestrian.clone());
            Pair<Double,Set<Pedestrian>> next = othersList.get(idx);
            double delta = othersList.get(idx+1).getOne() - next.getOne();
            time += delta;
            heuristic.advance(pedestrian,next.getOther(), stepProcessor, delta);
            idx++;
            return toReturn;
        }
    }
}
