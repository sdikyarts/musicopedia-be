package musicopedia.service.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
@ComponentScan(basePackages = {"musicopedia.service", "musicopedia.repository"})
public class ServiceTestConfig {
}
