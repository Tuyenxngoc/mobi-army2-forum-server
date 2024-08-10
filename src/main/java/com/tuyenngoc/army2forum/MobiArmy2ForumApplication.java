package com.tuyenngoc.army2forum;

import com.tuyenngoc.army2forum.config.CloudinaryConfig;
import com.tuyenngoc.army2forum.config.MailConfig;
import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.service.CategoryService;
import com.tuyenngoc.army2forum.service.CharacterService;
import com.tuyenngoc.army2forum.service.RoleService;
import com.tuyenngoc.army2forum.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SpringBootApplication
@EnableConfigurationProperties({
        AdminInfo.class,
        MailConfig.class,
        CloudinaryConfig.class
})
@EnableScheduling
public class MobiArmy2ForumApplication {

    RoleService roleService;

    UserService userService;

    CategoryService categoryService;

    CharacterService characterService;

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
            roleService.initRoles();
            characterService.initCharacters();
            userService.initAdmin(adminInfo);
            categoryService.initCategories(adminInfo);
        };
    }

}
