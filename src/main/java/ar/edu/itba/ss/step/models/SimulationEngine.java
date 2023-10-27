package ar.edu.itba.ss.step.models;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class SimulationEngine {




    public Iterator<Pair<Double,Pedestrian>> simulate(Pedestrian pedestrian, StepProcessor stepProcessor, Iterator<Set<Pedestrian>> othersIterator,  SimulationHeuristic heuristic) {
        return new PedestrianIterator(pedestrian, othersIterator, stepProcessor, heuristic);
    }

    public Iterator<Pair<Double,Pedestrian>> simulate(Pedestrian pedestrian,  StepProcessor stepProcessor) {
        return simulate(pedestrian, stepProcessor, new Iterator<Set<Pedestrian>>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Set<Pedestrian> next() {
                return Collections.emptySet();
            }
        }, SimulationHeuristic.getDefault());
    }

    static class PedestrianIterator implements Iterator<Pair<Double,Pedestrian>> {
        private final Pedestrian pedestrian;


        private final Iterator<Set<Pedestrian>> othersIterator;

        private final StepProcessor stepProcessor;

        private double time;

        private final SimulationHeuristic heuristic;

        PedestrianIterator(Pedestrian pedestrian, Iterator<Set<Pedestrian>> othersIterator, StepProcessor stepProcessor, SimulationHeuristic heuristic) {
            this.pedestrian = pedestrian;
            this.othersIterator = othersIterator;
            this.heuristic = heuristic;
            this.stepProcessor = stepProcessor;
        }

        @Override
        public boolean hasNext() {
            return othersIterator.hasNext();
        }


        @Override
        public Pair<Double,Pedestrian> next() {
            Pair<Double,Pedestrian> toReturn = Pair.of(time,pedestrian.clone());

            time = heuristic.advance(pedestrian,othersIterator.next(), stepProcessor, time);

            return toReturn;
        }
    }
}
