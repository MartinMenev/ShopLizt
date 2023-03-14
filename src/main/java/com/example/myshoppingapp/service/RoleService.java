package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.roles.RoleEntity;
import com.example.myshoppingapp.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;

    }

    @PostConstruct
    private void postConstruct() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.saveAllAndFlush(Arrays.stream(UserRole.values())
                    .map(role -> RoleEntity.builder()
                            .role(role)
                            .build())
                    .collect(Collectors.toList()));
        }
    }
}
