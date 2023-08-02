package com.happiestminds.springsecurity.security;

import com.happiestminds.springsecurity.entities.Role;
import com.happiestminds.springsecurity.entities.User;
import com.happiestminds.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("user not found with username or email:" + usernameOrEmail));


        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }
}
