package musicopedia.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfigTest.Config.class, WebConfig.class})
class WebConfigTest {
    @TestConfiguration
    static class Config {
        @Bean
        public CudAdminTokenInterceptor cudAdminTokenInterceptor() {
            return mock(CudAdminTokenInterceptor.class);
        }
    }

    @Autowired
    private WebConfig webConfig;
    @Autowired
    private CudAdminTokenInterceptor cudAdminTokenInterceptor;

    @Test
    void interceptorIsRegistered() {
        assertNotNull(webConfig);
        assertNotNull(cudAdminTokenInterceptor);
    }
}
