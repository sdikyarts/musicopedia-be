package musicopedia.controller.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
@ActiveProfiles("test")
@ComponentScan(basePackages = {"musicopedia.controller", "musicopedia.service", "musicopedia.repository"})
public class ControllerTestConfig implements WebMvcConfigurer {
}
