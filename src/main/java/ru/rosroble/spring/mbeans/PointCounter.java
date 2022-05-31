package ru.rosroble.spring.mbeans;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;
import ru.rosroble.spring.models.Point;

import javax.management.Notification;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ManagedResource
@Component
public class PointCounter implements NotificationPublisherAware, Serializable {
    private NotificationPublisher notificationPublisher;
    private final Map<String, Integer> usersPoints = new HashMap<>();
    private final Map<String, Integer> usersHits = new HashMap<>();
    private int sequenceNumber;

    public void addPoint(Point point) {
        String username = point.getInitiator().getUsername();
        int points = usersPoints.containsKey(username) ? usersPoints.get(point.getInitiator().getUsername()) : 0;
        usersPoints.put(username, points + 1);
        if (point.isHit()) {
            int hits = 0;
            if (usersHits.containsKey(username)) hits = usersHits.remove(username);
            usersHits.put(username, hits + 1);
        } else {
                notificationPublisher.sendNotification(new Notification(
                        "Notification", this, sequenceNumber++, System.currentTimeMillis(),
                        String.format("You missed, %s!", username)
                ));
        }
    }

    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    @ManagedAttribute
    public Map<String, Integer> getUserPoints() {
        return this.usersPoints;
    }

    @ManagedAttribute
    public Map<String, Integer> getUserHits() {
        return this.usersHits;
    }

    @ManagedOperation
    public Integer getPoints(String username) {
        return this.usersPoints.get(username);
    }

    @ManagedOperation
    public Integer getHits(String username) {
        return this.usersHits.get(username);
    }

    @ManagedOperation
    public Integer getUserPointsCount(String username) {
        return this.usersPoints.get(username);
    }

    @ManagedOperation
    public Integer getUserHitCount(String username) {
        return this.usersHits.get(username);
    }

}

