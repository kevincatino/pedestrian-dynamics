package ar.edu.itba.ss.step.models;

import java.util.Arrays;
import java.util.Collections;

public class TargetHelper {
    Pair<Double,Vector>[] targets;
    public TargetHelper(Pair<Double,Vector>... targets) {
        Arrays.sort(targets,(a,b) -> a.getOne().compareTo(b.getOne()));
        this.targets = targets;
    }

    public Vector getTarget(double time) {
        for (Pair<Double,Vector> pair : targets) {
            if (time < pair.getOne()) {
                return pair.getOther();
            }
        }
        throw new RuntimeException();
    }
}
