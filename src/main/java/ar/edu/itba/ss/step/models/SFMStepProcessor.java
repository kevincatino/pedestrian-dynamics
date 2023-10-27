package ar.edu.itba.ss.step.models;

import java.util.Set;

public class SFMStepProcessor implements StepProcessor {
    private final double tau;
    private final double delta;

    public SFMStepProcessor(double tau, double delta) {
        this.tau = tau;
        this.delta = delta;
    }



     private Vector drivingAcceleration(Pedestrian pedestrian) {
            return pedestrian.geteTarget().multiply(pedestrian.getVd()).substract(pedestrian.getVelocity()).multiply(1/tau);
        }


    @Override
    public double advance(Pedestrian pedestrian, Set<Pedestrian> otherPedestrians, double time) {
            Vector drivingAcceleration = drivingAcceleration(pedestrian);
            Vector newPosition = pedestrian.getPosition().add(pedestrian.getVelocity().multiply(delta)).add(drivingAcceleration.multiply(0.5*delta*delta));
            pedestrian.setPosition(newPosition);
            pedestrian.setVelocity(pedestrian.getVelocity().add(drivingAcceleration.multiply(delta)));
            time += delta;
            return time;
    }


}
