package ar.edu.itba.ss.step.models;

import java.util.LinkedList;
import java.util.Queue;

public class PathTargetProvider implements TargetProvider{
    private final Queue<Vector> nextTargets = new LinkedList<>();
    private Vector target = null;

    private final static double DELTA = 0.1;

    public PathTargetProvider(Vector... targets) {
        for (Vector target : targets) {
            nextTargets.offer(target);
        }
    }

    @Override
    public Vector getTarget(Pedestrian p) {
        if (target == null) {
            target = nextTargets.poll();
            return target;
        }

        if (p.getPosition().substract(target).getMod() < DELTA) {
            target = nextTargets.poll();
        }

        return target;

    }
}
