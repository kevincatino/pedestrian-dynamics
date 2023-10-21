package ar.edu.itba.ss.step.models;

import java.util.Iterator;

public class SFM implements Simulation {
    private final double tau;

    public SFM(double tau) {
        this.tau = tau;
    }

    public Iterator<Pair<Double,Pedestrian>> simulate(double delta, double maxTime, Pedestrian pedestrian) {
        return new PedestrianIterator(delta, maxTime, pedestrian, tau);
    }

    static class PedestrianIterator implements Iterator<Pair<Double,Pedestrian>> {
        private final double delta;
        private final Pedestrian pedestrian;

        private final double maxTime;

        private double time;

        private final double tau;
        PedestrianIterator(double delta, double maxTime, Pedestrian pedestrian, double tau) {
            this.delta = delta;
            this.pedestrian = pedestrian;
            this.maxTime = maxTime;
            this.tau = tau;
        }

        @Override
        public boolean hasNext() {
            return time < maxTime;
        }
        private double drivingForce() {
            return pedestrian.getMass()*(pedestrian.getVd() - pedestrian.getV())/tau;
        }
        private void advance(double delta) {
            double drivingForce = drivingForce();
            double a = drivingForce/ pedestrian.getMass();
            pedestrian.setX(pedestrian.getX() + pedestrian.getV()*delta + a/2 * delta*delta);
            pedestrian.setV(pedestrian.getV() + a*delta);
            time += delta;
        }

        @Override
        public Pair<Double,Pedestrian> next() {
            advance(delta);
            return Pair.of(time,pedestrian);
        }
    }
}
