package ar.edu.itba.ss.step.models;


public class Pedestrian {
    private final double vd;

    public double getVd() {
        return vd;
    }

    public double getMass() {
        return mass;
    }

    public double getTau() {
        return tau;
    }

    private final double mass;
    private final double tau;

    private double x;

    public double getX() {
        return x;
    }

    public double getV() {
        return v;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setV(double v) {
        this.v = v;
    }

    private double v;



    public Pedestrian(double vd, double v, double mass, double tau) {
        this.vd = vd;
        this.v = v;
        this.mass = mass;
        this.tau = tau;
    }






}
