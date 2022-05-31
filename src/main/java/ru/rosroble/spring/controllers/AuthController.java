package ru.rosroble.spring.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosroble.spring.util.jwt.JwtUtils;
import ru.rosroble.spring.util.jwt.UserTokenEntity;
import ru.rosroble.spring.models.User;
import ru.rosroble.spring.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService service;
    @Autowired
    private JwtUtils jwtUtils;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    @CrossOrigin
    private ResponseEntity<?> register(@Validated @RequestBody User user, HttpSession session) {
        if (!service.saveUser(user)) {
            return ResponseEntity.badRequest().body("Name " + user.getUsername() + " is already used");
        }
        return ResponseEntity.ok().body(user.getUsername());
    }

    @PostMapping("/login")
    @CrossOrigin
    private ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
        if (user.getUsername().startsWith("vk_")) {
            try {
                User vkUser = (User) service.loadUserByUsername(user.getUsername());
                String jwtToken = jwtUtils.generateJwtToken(user.getUsername());
                return ResponseEntity.ok(new UserTokenEntity(user.getUsername(), jwtToken));

            } catch (UsernameNotFoundException e) {
                user.setPassword(user.getUsername());
                register(user, session);
            }
            String jwtToken = jwtUtils.generateJwtToken(user.getUsername());
            return ResponseEntity.ok(new UserTokenEntity(user.getUsername(), jwtToken));
        }

        try {
            User dbUser = (User) service.loadUserByUsername(user.getUsername());
            if (!bCryptPasswordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
                throw new IllegalArgumentException();
            }

            String jwtToken = jwtUtils.generateJwtToken(user.getUsername());
            return ResponseEntity.ok(new UserTokenEntity(user.getUsername(), jwtToken));

        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Wrong username or/and password");
        }
    }

}
