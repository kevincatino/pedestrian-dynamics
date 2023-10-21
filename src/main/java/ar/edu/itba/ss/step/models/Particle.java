package ar.edu.itba.ss.step.models;


import ar.edu.itba.ss.step.utils.MathHelper;

import java.util.Collection;
import java.util.Objects;

public class Particle {

    private static int ID = 1;

    private final int id;


    public double getMaxSpeed() {
        return u;
    }

    public int getId() {
        return id;
    }


    public double getVelocity() {
        return v;
    }

    public void setVelocity(double velocity) {
        this.v = velocity;
    }

    public double getPosition() {
        return x;
    }


    private double x;

    private final double u;

    private double v;

    public double getActualX() {
        return actualX;
    }

    private double actualX;





    public Particle(double x, double u) {
        this.id = ID;
        this.u = u;
        this.v = u;
        this.x = x;
        ID++;
    }

    public boolean isOverlapping(Particle p) {
        return  (getDistanceTo(p) - Constants.RADIUS*2) < 0;
    }

    public void setPosition(double x) {
        this.x = MathHelper.modulo(x,Constants.LINE_LENGTH);
        this.actualX = x;
    }

    public static boolean overlapExists(Collection<Particle> particles, Particle p) {
        for (Particle particle : particles) {
            if (particle.isOverlapping(p)) {
                return true;
            }
        }
        return false;
    }

    public double getDistanceTo(Particle p) {
        return Math.min(Math.abs(x - p.x), Constants.LINE_LENGTH - Math.abs(x - p.x));
    }



    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Particle aux)) {
            return false;
        }
        return this.id == aux.id;
    }
}
