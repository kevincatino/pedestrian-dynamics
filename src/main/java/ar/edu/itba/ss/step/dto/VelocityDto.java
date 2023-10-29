package ar.edu.itba.ss.step.dto;

public class VelocityDto {
    public VelocityDto() {

    }
    public VelocityDto(Double vSim, double time, double vExp, double da) {
        this.vSim = vSim;
        this.time = time;
        this.vExp = vExp;
        this.da = da;
    }
    public VelocityDto( double time, double vExp, double da) {
        this.time = time;
        this.vExp = vExp;
        this.da = da;
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

    public double getDa() {
        return da;
    }

    public void setDa(double da) {
        this.da = da;
    }

    private double da;
    private double time;
    private double vExp;
}
