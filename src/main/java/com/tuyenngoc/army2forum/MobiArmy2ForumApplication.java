package com.tuyenngoc.army2forum;

import com.tuyenngoc.army2forum.config.CloudinaryConfig;
import com.tuyenngoc.army2forum.config.MailConfig;
import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.repository.RoleRepository;
import com.tuyenngoc.army2forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
@EnableConfigurationProperties({
        AdminInfo.class,
        MailConfig.class,
        CloudinaryConfig.class
})
@EnableScheduling
public class MobiArmy2ForumApplication {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        Environment env = SpringApplication.run(MobiArmy2ForumApplication.class, args).getEnvironment();
        String appName = env.getProperty("spring.application.name");
        if (appName != null) {
            appName = appName.toUpperCase();
        }
        String port = env.getProperty("server.port");
        log.info("-------------------------START " + appName
                + " Application------------------------------");
        log.info("   Application         : " + appName);
        log.info("   Url swagger-ui      : http://localhost:" + port + "/swagger-ui.html");
        log.info("-------------------------START SUCCESS " + appName
                + " Application----------------------");
    }

    @Bean
    CommandLineRunner init(AdminInfo adminInfo) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(1, "ROLE_ADMIN", Collections.emptyList()));
                roleRepository.save(new Role(2, "ROLE_USER", Collections.emptyList()));
            }
            //init admin
            if (userRepository.count() == 0) {
                try {
                    User user = new User();
                    user.setUsername(adminInfo.getUsername());
                    user.setEmail(adminInfo.getEmail());
                    user.setPassword(passwordEncoder.encode(adminInfo.getPassword()));
                    Role roleAdmin = new Role();
                    roleAdmin.setId(1);
                    user.setRole(roleAdmin);
                    userRepository.save(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
