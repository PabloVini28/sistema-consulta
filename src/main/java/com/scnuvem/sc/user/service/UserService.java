package com.scnuvem.sc.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scnuvem.sc.user.dtos.response.UserResponseDto;
import com.scnuvem.sc.user.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(user)).collect(Collectors.toList());
    }

    public UserResponseDto getUserByName(String name) {
        return userRepository.findByName(name)
                .map(user -> new UserResponseDto(user))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public UserResponseDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserResponseDto(user))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserResponseDto(user))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDto(user))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
    
    public void deleteUserByName(String name) {
        userRepository.deleteByName(name);
    }
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}
