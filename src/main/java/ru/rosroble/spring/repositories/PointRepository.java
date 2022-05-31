package ru.rosroble.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rosroble.spring.models.Point;
import ru.rosroble.spring.models.User;

import java.util.List;



public interface PointRepository extends JpaRepository <Point, Long> {

    List<NoUserInfo> findAllByInitiator(User initiator);


    interface NoUserInfo {
        double getX();
        double getY();
        double getR();
        boolean isHit();
    }




}
