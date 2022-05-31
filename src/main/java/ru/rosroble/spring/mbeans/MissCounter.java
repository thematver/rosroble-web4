package ru.rosroble.spring.mbeans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import ru.rosroble.spring.models.Point;

import javax.management.Notification;
import java.util.HashMap;
import java.util.Map;

@ManagedResource
@Component
public class MissCounter {

    @Autowired
    PointCounter userPointCounter;
    private final Map<String, Integer> userMisses = new HashMap<>();

    public void addPoint(Point point) {
        String username = point.getInitiator().getUsername();
        if (!point.isHit()) {
            int misses = 0;
            if (userMisses.containsKey(username)) misses = userMisses.remove(username);
            userMisses.put(username, misses + 1);
        }
    }


    @ManagedOperation
    public double getMissPercent(String username) {
        int missCount = userMisses.getOrDefault(username, 0);
        int totalCount = this.userPointCounter.getUserPointsCount(username);

        if (totalCount == 0) return 0;
        return  ((double) (missCount)/ (double) totalCount) * 100;
    }

}
