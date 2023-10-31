package ar.edu.itba.ss.step.models;


import ar.edu.itba.ss.step.dto.PedestrianDto;
import ar.edu.itba.ss.step.utils.Constants;

public class Pedestrian {
    private final double vd;
    private double currentVd;

    public void departure() {
        currentVd = vd;
    }

    public void arrival() {
        currentVd = 0;
    }


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

    public Vector getForce() {
        return force;
    }

    public void setForce(Vector force) {
        this.force = force;
    }

    private Vector force = Vector.of(0,0);
    public Vector geteTarget() {
        return targetProvider.getTarget(this).substract(position).normalize();
    }

    public double getDistanceToTarget() {
        return targetProvider.getTarget(this).getDistanceTo(position);
    }
    public double getVd() {
        return currentVd;
    }

    public double getMass() {
        return mass;
    }

    private final double mass;

    private final TargetProvider targetProvider;

    public Pedestrian(TargetProvider targetProvider, Vector position, double vd, double initialV, double mass) {
        this.targetProvider = targetProvider;
        this.vd = vd;
        this.currentVd = vd;
        this.position = position;
        this.mass = mass;
        this.velocity = geteTarget().scale(initialV);
    }

    public Vector getTarget() {
        return this.targetProvider.getTarget(this);
    }

   public static Pedestrian fromDto(PedestrianDto dto) {
        Pedestrian p =  new Pedestrian(new SimpleTargetProvider(Vector.of(dto.getTargetX(), dto.getTargetY())),
                Vector.of(dto.getX(), dto.getY()), 0, 0, Constants.MASS);
        p.velocity = Vector.of(dto.getVx(), dto.getVy());
        return p;
   }

    public Pedestrian(TargetProvider targetProvider, double vd,  double mass) {
       this(targetProvider,Vector.of(0,0), vd, 0, mass);
    }

    public Pedestrian clone() {
        Pedestrian clone =  new Pedestrian(targetProvider, vd, mass);
        clone.position = position;
        clone.velocity = velocity;
        return clone;
    }

    public boolean isColliding(Pedestrian other) {
        return other.position.substract(position).getMod() < 2* Constants.RADIUS;
    }






}
