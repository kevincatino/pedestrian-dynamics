package ar.edu.itba.ss.step.dto;

public class TauErrorDto {
    double tau;

    public TauErrorDto(double tau, double error) {
        this.tau = tau;
        this.error = error;
    }

    public double getTau() {
        return tau;
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    double error;

}
