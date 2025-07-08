package musicopedia.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CudAdminTokenInterceptorTest {
    private CudAdminTokenInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object handler = new Object();

    @BeforeEach
    void setUp() {
        interceptor = new CudAdminTokenInterceptor();
        ReflectionTestUtils.setField(interceptor, "adminToken", "test-token");
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void allowsGetWithoutToken() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        assertTrue(interceptor.preHandle(request, response, handler));
        verify(response, never()).setStatus(anyInt());
    }

    // POST tests
    @Test
    void deniesPostWithoutToken() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader("Authorization")).thenReturn(null);
        assertFalse(interceptor.preHandle(request, response, handler));
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void deniesPostWithWrongToken() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader("Authorization")).thenReturn("Bearer wrong-token");
        assertFalse(interceptor.preHandle(request, response, handler));
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void allowsPostWithCorrectToken() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");
        assertTrue(interceptor.preHandle(request, response, handler));
        verify(response, never()).setStatus(anyInt());
    }

    // PUT tests
    @Test
    void deniesPutWithoutToken() throws Exception {
        when(request.getMethod()).thenReturn("PUT");
        when(request.getHeader("Authorization")).thenReturn(null);
        assertFalse(interceptor.preHandle(request, response, handler));
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void deniesPutWithWrongToken() throws Exception {
        when(request.getMethod()).thenReturn("PUT");
        when(request.getHeader("Authorization")).thenReturn("Bearer wrong-token");
        assertFalse(interceptor.preHandle(request, response, handler));
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void allowsPutWithCorrectToken() throws Exception {
        when(request.getMethod()).thenReturn("PUT");
        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");
        assertTrue(interceptor.preHandle(request, response, handler));
        verify(response, never()).setStatus(anyInt());
    }

    // DELETE tests
    @Test
    void deniesDeleteWithoutToken() throws Exception {
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getHeader("Authorization")).thenReturn(null);
        assertFalse(interceptor.preHandle(request, response, handler));
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void deniesDeleteWithWrongToken() throws Exception {
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getHeader("Authorization")).thenReturn("Bearer wrong-token");
        assertFalse(interceptor.preHandle(request, response, handler));
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void allowsDeleteWithCorrectToken() throws Exception {
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");
        assertTrue(interceptor.preHandle(request, response, handler));
        verify(response, never()).setStatus(anyInt());
    }
}
