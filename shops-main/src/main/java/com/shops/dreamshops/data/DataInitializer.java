package com.shops.dreamshops.data;

import com.shops.dreamshops.model.Role;
import com.shops.dreamshops.model.User;
import com.shops.dreamshops.repository.RoleRepository;
import com.shops.dreamshops.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
@Transactional
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN","ROLE_USER");
        createDefaultUserNotExists();
        createDefaultAdminNotExists();
        createDefaultRoleIfNotExits(defaultRoles);
    }

    private void createDefaultUserNotExists() {

        Role userRole = roleRepository.findByName("ROLE_USER");
        for(int i=0;i<=5;i++){
            String defaultEmail="user"+i+"@email.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user=new User();
            user.setFirstName("The User");
            user.setLastName("User"+i);
            user.setEmail("user"+i+"@email.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);

        }


    }

    private void createDefaultAdminNotExists() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            throw new IllegalStateException("ROLE_ADMIN does not exist in the database. Make sure to create it first.");
        }
        for (int i = 0; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole)); // Voeg de gevonden rol toe
            userRepository.save(user);
        }
    }

    private void createDefaultRoleIfNotExits(Set<String> roles) {
        roles.stream()
                .filter(roleName -> roleRepository.findByName(roleName) == null) // Controleer expliciet op null
                .map(Role::new) // Maak een nieuwe Role
                .forEach(roleRepository::save); // Sla op in de database
    }

}
