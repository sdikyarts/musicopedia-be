package musicopedia.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test configuration for the Musicopedia backend tests.
 * Spring Boot auto-configuration handles the database setup.
 */
@TestConfiguration
@ActiveProfiles("test")
@ComponentScan(basePackages = {"musicopedia.repository"})
public class TestConfig {
    // We rely on Spring Boot's auto-configuration for test databases
    // and JPA setup when using @SpringBootTest with properties
}
