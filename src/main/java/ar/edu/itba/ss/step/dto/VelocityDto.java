package ar.edu.itba.ss.step.dto;

public class VelocityDto {
    public VelocityDto() {

    }
    public VelocityDto(Double vSim, double time, double vExp) {
        this.vSim = vSim;
        this.time = time;
        this.vExp = vExp;
    }
    public VelocityDto( double time, double vExp) {
        this.time = time;
        this.vExp = vExp;
    }

    public Double getvSim() {
        return vSim;
    }

    public void setvSim(Double vSim) {
        this.vSim = vSim;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getvExp() {
        return vExp;
    }

    public void setvExp(double vExp) {
        this.vExp = vExp;
    }

    private Double vSim;
    private double time;
    private double vExp;
}
