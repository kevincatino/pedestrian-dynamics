package ar.edu.itba.ss.step.models;

public class SimpleTargetProvider implements TargetProvider{
    private final Vector v;
    public SimpleTargetProvider(Vector v) {
        this.v = v;
    }
    @Override
    public Vector getTarget(Pedestrian p) {
        return v;
    }
}
