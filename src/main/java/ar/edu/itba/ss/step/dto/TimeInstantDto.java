package ar.edu.itba.ss.step.dto;

import java.util.Collection;
import java.util.List;

public class TimeInstantDto {
    public TimeInstantDto() {

    }
    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Collection<PedestrianDto> getPedestrians() {
        return pedestrians;
    }

    public void setPedestrians(List<PedestrianDto> pedestrians) {
        this.pedestrians = pedestrians;
    }

    private double time;
    private Collection<PedestrianDto> pedestrians;

    public TimeInstantDto(double time, Collection<PedestrianDto> pedestrians) {
        this.time = time;
        this.pedestrians = pedestrians;
    }
}
