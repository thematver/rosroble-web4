package ru.rosroble.spring.models;

public class BasicPoint {

    protected double x;
    protected double y;
    protected double r;
    protected boolean hit;

    public void validate() {
        this.hit = isRectangleHit() || isCircleHit() || isTriangleHit();
    }

    private boolean isRectangleHit() {
        return x >= 0 && y >= 0 && x <= r / 2 && y <= r;
    }

    private boolean isCircleHit() {
        return x >= 0 && y <= 0 && (x*x + y*y <= r*r);
    }

    private boolean isTriangleHit() {
        return x <= 0 && y >= 0 && y <= 0.5 * x + r / 2;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
