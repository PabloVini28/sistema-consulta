package com.scnuvem.sc.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scnuvem.sc.user.dtos.response.UserResponseDto;
import com.scnuvem.sc.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/name")
    public ResponseEntity<UserResponseDto> getUserByName(@RequestParam String name) {
        UserResponseDto user = userService.getUserByName(name);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username")
    public ResponseEntity<UserResponseDto> getUserByUsername(@RequestParam String username) {
        UserResponseDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam String email) {
        UserResponseDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id")
    public ResponseEntity<UserResponseDto> getUserById(@RequestParam Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deleteUserById(@RequestParam Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/username")
    public ResponseEntity<Void> deleteUserByUsername(@RequestParam String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/name")
    public ResponseEntity<Void> deleteUserByName(@RequestParam String name) {
        userService.deleteUserByName(name);
        return ResponseEntity.noContent().build();
    }

}
