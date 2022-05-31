package ru.rosroble.spring.models;

import javax.persistence.*;

@Entity
@Table(name = "point_table")
public class Point extends BasicPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double x;
    private double y;
    private double r;
    private boolean hit;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User initiator;

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

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
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

    public void validate() {
        this.hit = isRectangleHit() || isCircleHit() || isTriangleHit();
    }
}
