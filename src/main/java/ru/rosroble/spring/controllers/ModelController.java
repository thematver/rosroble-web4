package ru.rosroble.spring.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rosroble.spring.util.jwt.JwtUtils;
import ru.rosroble.spring.models.BasicPoint;
import ru.rosroble.spring.models.Point;
import ru.rosroble.spring.models.User;
import ru.rosroble.spring.repositories.PointRepository;
import ru.rosroble.spring.services.PointService;
import ru.rosroble.spring.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private PointService pointService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/points", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<?> getPoints(HttpServletRequest request) {
        String jwtToken = jwtUtils.extractJwtToken(request);
        if (jwtToken == null) return ResponseEntity.badRequest().body("Bad token");
        List<PointRepository.NoUserInfo> pointList = pointService.getAllPointsByInitiator(getUserByJwtToken(jwtToken));
        return ResponseEntity.ok(pointList);
    }

    @GetMapping(value = "/points/graph", params={"r"},  produces = "application/json")
    @CrossOrigin
    public ResponseEntity<?> recalculatedRadiusPoints(HttpServletRequest request, @RequestParam(value = "r") double r) {
        String jwtToken = jwtUtils.extractJwtToken(request);
        if (jwtToken == null) return ResponseEntity.badRequest().body("Bad token");
        List<PointRepository.NoUserInfo> pointList = pointService.getAllPointsByInitiator(getUserByJwtToken(jwtToken));
        List<BasicPoint> basicPoints = new ArrayList<>();
        for (PointRepository.NoUserInfo p: pointList) {
            BasicPoint basicPoint = new BasicPoint();
            basicPoint.setX(p.getX());
            basicPoint.setY(p.getY());
            basicPoint.setR(r);
            basicPoint.validate();
            basicPoints.add(basicPoint);
        }
        return ResponseEntity.ok(basicPoints);
    }

    @PostMapping(value = "/points")
    @CrossOrigin
    public ResponseEntity<Point> addPoint(HttpServletRequest req, @RequestBody Point point) {
        User initiator = getUserByJwtToken(jwtUtils.extractJwtToken(req));
        point.setInitiator(initiator);
        point.validate();
        if(!pointService.savePoint(point)) return ResponseEntity.badRequest().body(point);
        pointService.update(point);
        return ResponseEntity.ok(point);
    }

    @GetMapping("/check")
    @CrossOrigin
    private ResponseEntity<String> check() {
        return ResponseEntity.ok("ok");
    }

    private User getUserByJwtToken(String jwtToken) {
        return (User) userService.loadUserByUsername(jwtUtils.getUsernameFromJwtToken(jwtToken));
    }
}
