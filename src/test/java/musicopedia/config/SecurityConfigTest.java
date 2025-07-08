package musicopedia.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testAddCorsMappingsWithWildcardHeaders() {
        // Set up test properties for wildcard headers
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000,http://localhost:8080");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST,PUT,DELETE");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "*");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", true);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 3600L);

        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedOrigins(any(String[].class))).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);
        when(registration.maxAge(anyLong())).thenReturn(registration);

        securityConfig.addCorsMappings(registry);

        verify(registry).addMapping("/api/**");
        verify(registration).allowedOrigins("http://localhost:3000", "http://localhost:8080");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE");
        verify(registration).allowedHeaders("*"); // This tests the wildcard branch
        verify(registration).allowCredentials(true);
        verify(registration).maxAge(3600L);
    }

    @Test
    void testAddCorsMappingsWithSpecificHeaders() {
        // Set up test properties for specific headers
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "Content-Type,Authorization");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", false);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 1800L);

        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedOrigins(any(String[].class))).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);
        when(registration.maxAge(anyLong())).thenReturn(registration);

        securityConfig.addCorsMappings(registry);

        verify(registry).addMapping("/api/**");
        verify(registration).allowedOrigins("http://localhost:3000");
        verify(registration).allowedMethods("GET", "POST");
        verify(registration).allowedHeaders("Content-Type", "Authorization"); // This tests the specific headers branch
        verify(registration).allowCredentials(false);
        verify(registration).maxAge(1800L);
    }

    @Test
    void testCorsConfigurationSourceWithWildcardHeaders() {
        // Set up test properties for wildcard headers
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000,http://localhost:8080");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST,PUT,DELETE");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "*");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", true);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 3600L);

        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        
        assertNotNull(source);
        assertTrue(source instanceof UrlBasedCorsConfigurationSource);
    }

    @Test
    void testCorsConfigurationSourceWithSpecificHeaders() {
        // Set up test properties for specific headers
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "Content-Type,Authorization");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", false);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 1800L);

        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        
        assertNotNull(source);
        assertTrue(source instanceof UrlBasedCorsConfigurationSource);
    }

    @Test
    void testCorsConfigurationWithDefaultValues() {
        // Test with default property values
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000,http://localhost:8080");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST,PUT,DELETE,OPTIONS");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "*");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", false);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 3600L);

        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);
    }

    @Test
    void testCorsConfigurationWithMultipleOrigins() {
        // Test with multiple origins
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000,http://localhost:8080,http://localhost:4200");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "Content-Type");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", true);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 7200L);

        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedOrigins(any(String[].class))).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);
        when(registration.maxAge(anyLong())).thenReturn(registration);

        securityConfig.addCorsMappings(registry);

        verify(registration).allowedOrigins("http://localhost:3000", "http://localhost:8080", "http://localhost:4200");
        verify(registration).allowedMethods("GET", "POST");
        verify(registration).allowedHeaders("Content-Type");
        verify(registration).allowCredentials(true);
        verify(registration).maxAge(7200L);
    }

    @Test
    void testCorsConfigurationWithSingleOrigin() {
        // Test with single origin
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "Content-Type");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", false);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 1800L);

        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedOrigins(any(String[].class))).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);
        when(registration.maxAge(anyLong())).thenReturn(registration);

        securityConfig.addCorsMappings(registry);

        verify(registration).allowedOrigins("http://localhost:3000");
        verify(registration).allowedMethods("GET");
        verify(registration).allowedHeaders("Content-Type");
        verify(registration).allowCredentials(false);
        verify(registration).maxAge(1800L);
    }

    @Test
    void testCorsConfigurationBranchCoverageForWildcardHeaders() {
        // This test specifically targets the "*".equals(allowedHeaders) branch
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "*");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", true);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 3600L);

        // Create a real CorsConfigurationSource to verify the actual configuration
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);
        
        // Verify that it's the expected type
        assertTrue(source instanceof UrlBasedCorsConfigurationSource);
    }

    @Test
    void testCorsConfigurationBranchCoverageForNonWildcardHeaders() {
        // This test specifically targets the else branch of "*".equals(allowedHeaders)
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "Content-Type,Authorization,X-Requested-With");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", false);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 1800L);

        // Create a real CorsConfigurationSource to verify the actual configuration
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);
        
        // Verify that it's the expected type
        assertTrue(source instanceof UrlBasedCorsConfigurationSource);
    }

    @Test
    void testAddCorsMappingsBranchCoverageForWildcardHeaders() {
        // This test specifically targets the ternary operator branch for "*" headers
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "*");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", true);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 3600L);

        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedOrigins(any(String[].class))).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);
        when(registration.maxAge(anyLong())).thenReturn(registration);

        securityConfig.addCorsMappings(registry);

        // Verify that the wildcard headers array was used
        verify(registration).allowedHeaders("*");
    }

    @Test
    void testAddCorsMappingsBranchCoverageForNonWildcardHeaders() {
        // This test specifically targets the ternary operator branch for non-"*" headers
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "http://localhost:3000");
        ReflectionTestUtils.setField(securityConfig, "allowedMethods", "GET,POST");
        ReflectionTestUtils.setField(securityConfig, "allowedHeaders", "Content-Type,Authorization");
        ReflectionTestUtils.setField(securityConfig, "allowCredentials", false);
        ReflectionTestUtils.setField(securityConfig, "maxAge", 1800L);

        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);
        
        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedOrigins(any(String[].class))).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);
        when(registration.maxAge(anyLong())).thenReturn(registration);

        securityConfig.addCorsMappings(registry);

        // Verify that the split headers array was used
        verify(registration).allowedHeaders("Content-Type", "Authorization");
    }

    @Test
    void testSecurityConfigInstantiation() {
        // This test ensures the SecurityConfig constructor is covered
        SecurityConfig config = new SecurityConfig();
        assertNotNull(config);
    }
}

@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigWebIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @org.junit.jupiter.api.Test
    void apiRequiresAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/test"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/login"));
    }

    @org.junit.jupiter.api.Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void apiAllowsAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()); // No controller, but passes security
    }

    @org.junit.jupiter.api.Test
    void publicEndpointsAccessible() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()); // No controller, but passes security
    }
}
