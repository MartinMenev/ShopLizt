package com.example.myshoppingapp.service;
import com.example.myshoppingapp.domain.enums.UserRole;
import com.example.myshoppingapp.domain.users.UserEntity;
import com.example.myshoppingapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

public class ApplicationUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public ApplicationUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return
            userRepository.
                    findUserEntityByUsername(username).
                    map(this::map).
                    orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found!"));
  }

  private UserDetails map(UserEntity userEntity) {
    return new User(
        userEntity.getUsername(),
        userEntity.getPassword(),
        extractAuthorities());
  }

  private List<GrantedAuthority> extractAuthorities() {
    return Arrays.stream(UserRole.values()).
        map(this::mapRole).
        toList();
  }

  private GrantedAuthority mapRole(UserRole userRole) {
    return new SimpleGrantedAuthority("ROLE_" + userRole.name());
  }
}
