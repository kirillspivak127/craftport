package dev.craftport.config;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.craftport.domain.Role;
import dev.craftport.store.jpa.UserEntity;
import dev.craftport.store.jpa.UserJpaRepository;

@Component
public class AdminBootstrap implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public AdminBootstrap(UserJpaRepository userJpaRepository,
            PasswordEncoder passwordEncoder,
            Environment environment) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        String password = environment.getProperty("ADMIN_PASSWORD");

        if (password == null || password.isBlank()) {
            log.warn("ADMIN_PASSWORD is not set; skipping admin bootstrap");
            return;
        }

        if (userJpaRepository.existsByUsername("admin")) {
            log.info("admin already exists; skipping bootstrap");
            return;
        }

        userJpaRepository.save(new UserEntity(
                "admin",
                passwordEncoder.encode(password),
                Role.ADMIN,
                Instant.now()));
        log.info("admin user created");
    }
}
