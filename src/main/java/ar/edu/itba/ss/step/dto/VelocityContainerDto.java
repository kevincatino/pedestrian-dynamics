package ar.edu.itba.ss.step.dto;

import java.util.List;

public class VelocityContainerDto {
    private int id;
public VelocityContainerDto() {

}
    public VelocityContainerDto(int id, double error, List<VelocityDto> velocities) {
        this.id = id;
        this.error = error;
        this.velocities = velocities;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    private double error;

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
