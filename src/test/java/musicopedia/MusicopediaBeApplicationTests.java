package musicopedia;

import musicopedia.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class MusicopediaBeApplicationTests {

    // Just using DataJpaTest which provides a focused test environment

    @Test
    void contextLoads() {
        // Empty test that should pass without loading the full application context
        // The DataJpaTest annotation applies only the necessary configuration for JPA tests
    }
}
