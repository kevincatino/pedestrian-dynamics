package ar.edu.itba.ss.step.dto;

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

    public PedestrianDto(double x, double y,  int id) {
        this.x = x;
        this.y = y;
        this.id = id;
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
