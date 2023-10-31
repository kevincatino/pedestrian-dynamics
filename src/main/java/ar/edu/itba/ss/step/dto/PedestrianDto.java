package ar.edu.itba.ss.step.dto;

import ar.edu.itba.ss.step.models.Pedestrian;

import java.util.Objects;

public class PedestrianDto implements Comparable<PedestrianDto> {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    private double x;
    private double y;

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    private double targetX;
    private double targetY;

    private double vx;

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public PedestrianDto(int id, double x, double y, double targetX, double targetY, double vx, double vy) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.vx = vx;
        this.vy = vy;
    }

    private double vy;

    public PedestrianDto(double x, double y,  int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }
    public static PedestrianDto from(Pedestrian p, int id) {
        return new PedestrianDto(id, p.getPosition().getX(), p.getPosition().getY(), p.getTarget().getX(), p.getTarget().getY(), p.getVelocity().getX(), p.getVelocity().getY());
    }
    public PedestrianDto() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedestrianDto that = (PedestrianDto) o;
        return id == that.id;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(PedestrianDto o) {
        return id - o.id;
    }
}
