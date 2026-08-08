package com.infinitycart.controller;

import com.infinitycart.model.User;
import com.infinitycart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> createUserhandler(@RequestHeader("Authorization") String jwt ) throws  Exception{
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(user);
    }



}
