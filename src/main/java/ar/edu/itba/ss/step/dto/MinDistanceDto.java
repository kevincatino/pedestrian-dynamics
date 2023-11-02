package ar.edu.itba.ss.step.dto;

public class MinDistanceDto {
    private double time;

    public MinDistanceDto(double time, double dMin, int id) {
        this.time = time;
        this.dMin = dMin;
        this.id = id;
    }

    public MinDistanceDto() {

    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getdMin() {
        return dMin;
    }

    public void setdMin(double dMin) {
        this.dMin = dMin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double dMin;
    private int id;
}
