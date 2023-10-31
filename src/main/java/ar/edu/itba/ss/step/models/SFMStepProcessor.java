package ar.edu.itba.ss.step.models;

import ar.edu.itba.ss.step.utils.Constants;

import java.util.Set;

public class SFMStepProcessor implements StepProcessor {
    private final double tau;
    private final double da;

    public SFMStepProcessor(double tau, double da) {
        this.tau = tau;
        this.da = da;
    }

    public SFMStepProcessor(double tau) {
        this(tau, 0);
    }



     private Vector drivingForce(Pedestrian pedestrian) {
            Vector target = pedestrian.geteTarget().multiply(pedestrian.getVd());
         Vector current =pedestrian.getVelocity();
         return target.substract(current).multiply(pedestrian.getMass()/tau);
    }

    private Vector collisionForce(Pedestrian pedestrian, Set<Pedestrian> others) {
        Vector collisionForce = Vector.of(0,0);
        for (Pedestrian other : others) {
            Vector diff = pedestrian.getPosition().substract(other.getPosition());
            double epsilon = diff.getMod() - 2* Constants.RADIUS;
            Vector diffVersor = diff.normalize();
            if (pedestrian.isColliding(other)) {
                Vector deltaF = diffVersor.multiply(-Constants.K*epsilon);
                collisionForce = collisionForce.add(deltaF);
            }
        }
        return collisionForce;
    }


    @Override
    public void advance(Pedestrian pedestrian, Set<Pedestrian> otherPedestrians, double delta) {
        double distanceToTarget = pedestrian.getDistanceToTarget();
            if (distanceToTarget <= da && pedestrian.getVelocity().getMod() > 0.1) {
                pedestrian.arrival();
            } else {
                pedestrian.departure();
            }

        Vector totalForce = drivingForce(pedestrian).add(collisionForce(pedestrian, otherPedestrians));
            Vector acceleration = totalForce.multiply(1/ pedestrian.getMass());
            Vector newPosition = pedestrian.getPosition().add(pedestrian.getVelocity().multiply(delta)).add(acceleration.multiply(0.5*delta*delta));
            pedestrian.setPosition(newPosition);
            pedestrian.setVelocity(pedestrian.getVelocity().add(acceleration.multiply(delta)));
    }


}
