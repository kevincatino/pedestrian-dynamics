package ar.edu.itba.ss.step.models;

public class Vector {
    private final double x;
    private final double y;

    public double getMod() {
        return mod;
    }

    private final double mod;


    private Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.mod = Math.sqrt(x*x + y*y);
    }

    public Vector multiply(double scalar) {
        return Vector.of(scalar*x, scalar*y);
    }

    public Vector add(Vector v) {
        return Vector.of(x + v.x, y + v.y);
    }

    public Vector substract(Vector v) {
        return add(v.multiply(-1));
    }


    public static Vector from(Vector c) {
        return Vector.of(c.getX(), c.getY());
    }

    public Vector scale(double desiredMod) {
double currentMod = getMod();

        if (currentMod == 0.0) {
            // Handle the zero vector if needed
            return new Vector(0, 0);
        }

        double scaleFactor = desiredMod / currentMod;
        double scaledX = getX() * scaleFactor;
        double scaledY = getY() * scaleFactor;

        return new Vector(scaledX, scaledY);
    }

    public Vector normalize() {
        return Vector.of(x,y).multiply(1/getMod());
    }


    public double getDistanceTo(Vector c) {
        return Math.sqrt(Math.pow(x - c.x, 2) + Math.pow(y - c.y, 2));
    }

    public static Vector of(double x, double y) {
        return new Vector(x, y);
    }

    public String toString() {
        return String.format("{%3.2f,%3.2f}", x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

