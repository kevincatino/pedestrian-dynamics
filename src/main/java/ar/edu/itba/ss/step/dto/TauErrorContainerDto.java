package ar.edu.itba.ss.step.dto;

import java.util.List;

public class TauErrorContainerDto {
    private int id;

    public List<TauErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(List<TauErrorDto> errors) {
        this.errors = errors;
    }

    private List<TauErrorDto> errors;

    public double getMinTau() {
        return minTau;
    }

    public void setMinTau(double minTau) {
        this.minTau = minTau;
    }

    public double getMinError() {
        return minError;
    }

    public void setMinError(double minError) {
        this.minError = minError;
    }

    private double minTau;
    private double minError = Double.MAX_VALUE;

    public TauErrorContainerDto(int id, List<TauErrorDto> errors, double startTime, double endTime) {
        this.id = id;
        this.errors = errors;
        this.startTime = startTime;
        this.endTime = endTime;
        for (TauErrorDto dto : errors) {
            if (dto.getError() < minError) {
                minError = dto.getError();
                minTau = dto.getTau();
            }
        }
    }

    public int getId() {
        return id;
    }

    public TauErrorContainerDto() {

    }


    public void setId(int id) {
        this.id = id;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

   private double startTime;
   private double endTime;
}
