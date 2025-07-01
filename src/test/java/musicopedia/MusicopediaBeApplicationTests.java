package musicopedia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@SpringBootTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class MusicopediaBeApplicationTests {

    @Test
    void contextLoads() {
        // The test will pass if the application context loads successfully with our test database settings
    }

    @Test
    void mainMethodShouldStartApplication() {
        try (MockedStatic<SpringApplication> springApplicationMock = Mockito.mockStatic(SpringApplication.class)) {
            // When
            MusicopediaBeApplication.main(new String[]{});
            
            // Then
            springApplicationMock.verify(() -> SpringApplication.run(MusicopediaBeApplication.class, new String[]{}));
        }
    }
}
