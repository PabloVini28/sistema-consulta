package com.scnuvem.sc.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.scnuvem.sc.auth.entity.UserDetailsImp;
import com.scnuvem.sc.user.entity.User;
import com.scnuvem.sc.user.repository.UserRepository;

@Service
public class UserDetailsImpService implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws org.springframework.security.core.userdetails.UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username).isPresent() ? userRepository.findByEmail(username) : Optional.empty();
        if(user.isPresent()){
            return new UserDetailsImp(user.get());
        }
        user = userRepository.findByUsername(username).isPresent() ? userRepository.findByUsername(username) : Optional.empty();
        if(user.isPresent()){
            return new UserDetailsImp(user.get());
        }
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

    


}
