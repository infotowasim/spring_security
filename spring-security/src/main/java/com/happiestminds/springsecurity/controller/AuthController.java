package com.happiestminds.springsecurity.controller;

import com.happiestminds.springsecurity.entities.Role;
import com.happiestminds.springsecurity.entities.User;
import com.happiestminds.springsecurity.payload.LoginDTO;
import com.happiestminds.springsecurity.payload.SignUpDTO;
import com.happiestminds.springsecurity.repositories.RoleRepository;
import com.happiestminds.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // http://localhost:8080/api/auth/signin
    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("User signed-in successfully");
    }


    // http://localhost:8080/api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDTO){

        // add check for username exists in a DB
        if (userRepository.existsByUsername(signUpDTO.getUsername())){
            return new ResponseEntity<>("Username is already taken!!!", HttpStatus.BAD_REQUEST);
        }

        // add check for email exists in DB
        if (userRepository.existsByEmail(signUpDTO.getEmail())){
            return new ResponseEntity<>("Email is already exists!!!", HttpStatus.BAD_REQUEST);
        }

        // create user object
        User user= User.builder()
                .name(signUpDTO.getName())
                .email(signUpDTO.getEmail())
                .username(signUpDTO.getUsername())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .build();

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        return ResponseEntity.ok("User registered successfully");


    }
}
