package interfaces;

import java.io.Serializable;

public class Vector2D implements Serializable {
    private static final long serialVersionUID = 1L;

    public double x;
    public double y;

    public Vector2D() {
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
    }

    @Override
    public String toString() {
        return "Vector2D{x=" + x + ", y=" + y + "}";
    }
}
