package musicopedia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final CudAdminTokenInterceptor cudAdminTokenInterceptor;

    public WebConfig(CudAdminTokenInterceptor cudAdminTokenInterceptor) {
        this.cudAdminTokenInterceptor = cudAdminTokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cudAdminTokenInterceptor)
                .addPathPatterns("/**");
    }
}
