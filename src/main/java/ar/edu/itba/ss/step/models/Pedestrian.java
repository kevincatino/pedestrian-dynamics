package ar.edu.itba.ss.step.models;


public class Pedestrian {
    private final double vd;


    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector vector) {
        this.position = vector;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    private Vector velocity;
    private Vector position;

    public Vector geteTarget() {
        return eTarget;
    }

    public void seteTarget(Vector eTarget) {
        this.eTarget = eTarget;
    }

    private Vector eTarget;

    public double getVd() {
        return vd;
    }

    public double getMass() {
        return mass;
    }

    private final double mass;

    public Pedestrian(Vector eTarget, Vector position, double vd, double initialV, double mass) {
        this.eTarget = eTarget.normalize();
        this.vd = vd;
        this.position = position;
        this.mass = mass;
        this.velocity = eTarget.scale(initialV);
    }

    public Pedestrian(Vector eTarget, double vd,  double mass) {
       this(eTarget,Vector.of(0,0), vd, 0, mass);
    }

    public Pedestrian clone() {
        Pedestrian clone =  new Pedestrian(eTarget, vd, mass);
        clone.position = position;
        clone.velocity = velocity;
        return clone;
    }






}
