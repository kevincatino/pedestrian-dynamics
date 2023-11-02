package ar.edu.itba.ss.step.models;

import ar.edu.itba.ss.step.utils.Constants;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public class CustomSFMHeuristic implements SimulationHeuristic {
    private static final double DMIN = 0.5;
    private static final double DMAX = 5;
    private static final double DMID = 2;
      public double getCollisionTime(Pedestrian one, Pedestrian other) {
        Vector deltaR = one.getPosition().substract(other.getPosition());
        Vector deltaV = one.getVelocity().substract(other.getVelocity());

        double omega = Constants.RADIUS * 2;

        double dv_dr = deltaV.dotProduct(deltaR);

        if (dv_dr >= 0) {
            return Double.MAX_VALUE;
        }

        double dv_dv = deltaV.dotProduct(deltaV);
        double d = Math.pow(dv_dr,2) - dv_dv*(deltaR.dotProduct(deltaR) - omega*omega);
        if (d < 0) {
            return Double.MAX_VALUE;
        }

        return  - (dv_dr + Math.sqrt(d))/dv_dv;
    }

    private static double evaluateForce(double d) {
          Function<Double,Double> f = (x) -> 800/Math.exp(x);
          if (d > DMAX)
              return 0;
          if (d > DMID) {
              double m = f.apply(DMID)/(DMAX - DMID);
              double y0 = f.apply(DMID);
              return y0 + m*(d - DMID);
          }
          if (d > DMIN) {
              return f.apply(DMIN);
          }
          return f.apply(d);
    }
    @Override
    public void advance(Pedestrian p, Set<Pedestrian> others, StepProcessor stepProcessor, double delta) {
        double minCollisionTime = Double.MAX_VALUE;
        Pedestrian toCollide = null;
        Vector force = Vector.of(0,0);

        for (Pedestrian other : others) {
            double collisionTime = getCollisionTime(p,other);
            if (collisionTime == Double.MAX_VALUE)
                continue;
            if (collisionTime < minCollisionTime) {
                minCollisionTime = collisionTime;
                toCollide = other;
            }
        }
        if (toCollide != null) {
            Vector iFuture = p.getPosition().add(p.getVelocity().multiply(minCollisionTime));
            Vector jFuture = toCollide.getPosition().add(toCollide.getVelocity().multiply(minCollisionTime));
            Vector deltaR = iFuture.substract(jFuture);
            double d = iFuture.substract(p.getPosition()).getMod() + (iFuture.substract(jFuture).getMod() - Constants.RADIUS*2);
            force = force.add(deltaR.scale(evaluateForce(d)));
        }
        p.setForce(force);

        stepProcessor.advance(p, others, delta);
    }

}
