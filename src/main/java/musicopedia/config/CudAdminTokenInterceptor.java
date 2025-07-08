package musicopedia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CudAdminTokenInterceptor implements HandlerInterceptor {
    @Value("${admin.token}")
    private String adminToken;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
            String auth = request.getHeader("Authorization");
            if (auth == null || !auth.equals("Bearer " + adminToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }
}
