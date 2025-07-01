package musicopedia;

import musicopedia.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class MusicopediaBeApplicationTests {

    @Test
    void contextLoads() {
        // The test will pass if the application context loads successfully
    }

}
