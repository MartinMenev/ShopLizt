package com.example.myshoppingapp.service;
import com.example.myshoppingapp.model.AppUserDetails;
import com.example.myshoppingapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class ApplicationUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public ApplicationUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findUserEntityByUsername(username)
            .map(u -> new AppUserDetails(
                    u.getUsername(),
                    u.getPassword(),
                    u.getRoles().stream()
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole().name()))
                            .collect(Collectors.toList())
            )).orElseThrow(() -> new UsernameNotFoundException(username + " was not found!"));
  }
}
