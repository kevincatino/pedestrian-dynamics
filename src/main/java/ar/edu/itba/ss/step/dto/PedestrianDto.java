package ar.edu.itba.ss.step.dto;

import ar.edu.itba.ss.step.models.Particle;

public class PedestrianDto {
    private int id;
    private double x;
    private double y;

    public PedestrianDto(double x, double y,  int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public PedestrianDto() {

    }
}
