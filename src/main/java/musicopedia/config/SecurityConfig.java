package musicopedia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    private static final String API_PATH_PATTERN = "/api/**";

    @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:8080,https://musicopedia.vercel.app/}")
    private String allowedOrigins;

    @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${app.cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${app.cors.allow-credentials:false}")
    private boolean allowCredentials;

    @Value("${app.cors.max-age:3600}")
    private long maxAge;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(API_PATH_PATTERN)
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods(allowedMethods.split(","))
                .allowedHeaders(allowedHeaders.equals("*") ? new String[]{"*"} : allowedHeaders.split(","))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Set allowed origins from configuration
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);
        
        // Set allowed methods
        configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        
        // Set allowed headers
        if ("*".equals(allowedHeaders)) {
            configuration.addAllowedHeader("*");
        } else {
            configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        }
        
        // Configure credentials
        configuration.setAllowCredentials(allowCredentials);
        
        // Set max age for preflight requests
        configuration.setMaxAge(maxAge);
        
        // Expose common headers
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(API_PATH_PATTERN, configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername(adminUsername)
                .password("{noop}" + adminPassword) // {noop} means no password encoder
                .roles("ADMIN")
                .build()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(API_PATH_PATTERN).hasRole("ADMIN");
                auth.anyRequest().permitAll();
            })
            .formLogin(form -> {
                form.loginPage("/login");
                form.permitAll();
            })
            .logout(LogoutConfigurer::permitAll);
        return http.build();
    }
}
