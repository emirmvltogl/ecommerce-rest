package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;

@Service
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

  User findByUsername(String username);

  User saveUser(User user);

  User saveDefaultUser(User user);

}
