package ar.edu.itba.ss.step.dto;

import java.util.List;

public class VelocityContainerDto {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VelocityDto> getVelocities() {
        return velocities;
    }

    public void setVelocities(List<VelocityDto> velocities) {
        this.velocities = velocities;
    }

    private List<VelocityDto> velocities;
}
