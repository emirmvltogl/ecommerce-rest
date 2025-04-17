package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserDetailsService;

@RestController
@RequestMapping("/api")
public class UserController {
  
  private UserDetailsService userService;

  @Autowired
  public UserController(UserDetailsService userService) {
    this.userService = userService;
  }

  @GetMapping("/user/{username}")
  public ResponseEntity<User> getSingleUser(@PathVariable String username) {
      User user = userService.findByUsername(username);
      if (user == null) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      return ResponseEntity.ok(user);
  }
  
  @PostMapping("/user/register")
  public ResponseEntity<User> createUser(@RequestBody User user){
    User savedUser = userService.saveUser(user);
    return new ResponseEntity<User>(savedUser,HttpStatus.CREATED);
  }

  @PostMapping("/def/register")
  public ResponseEntity<User>saveUser(@RequestBody User user){
    return new ResponseEntity<>(userService.saveDefaultUser(user),HttpStatus.CREATED);
  }
}
