package ar.edu.itba.ss.step.models;

import ar.edu.itba.ss.step.utils.Constants;

import java.util.Set;

public class SFMStepProcessor implements StepProcessor {
    private final double tau;

    public SFMStepProcessor(double tau) {
        this.tau = tau;
    }



     private Vector drivingForce(Pedestrian pedestrian) {
            return pedestrian.geteTarget().multiply(pedestrian.getVd()).substract(pedestrian.getVelocity()).multiply(pedestrian.getMass()/tau);
    }

    private Vector collisionForce(Pedestrian pedestrian, Set<Pedestrian> others) {
        Vector collisionForce = Vector.of(0,0);
        for (Pedestrian other : others) {
            Vector diff = other.getPosition().substract(pedestrian.getPosition());
            double epsilon = diff.getMod() - 2* Constants.RADIUS;
            Vector diffVersor = diff.normalize();
            if (pedestrian.isColliding(other)) {
                collisionForce = collisionForce.add(diffVersor.multiply(-Constants.K*epsilon));
            }
        }
        return collisionForce;
    }


    @Override
    public void advance(Pedestrian pedestrian, Set<Pedestrian> otherPedestrians, double delta) {
            Vector totalForce = drivingForce(pedestrian).add(collisionForce(pedestrian, otherPedestrians));
            Vector acceleration = totalForce.multiply(1/ pedestrian.getMass());
            Vector newPosition = pedestrian.getPosition().add(pedestrian.getVelocity().multiply(delta)).add(acceleration.multiply(0.5*delta*delta));
            pedestrian.setPosition(newPosition);
            pedestrian.setVelocity(pedestrian.getVelocity().add(acceleration.multiply(delta)));
    }


}
