package musicopedia.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubunitServiceExceptionTest {
    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new Exception("root");
        SubunitServiceException ex = new SubunitServiceException("msg", cause);
        assertEquals("msg", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void testConstructorWithCauseOnly() {
        Throwable cause = new Exception("root");
        SubunitServiceException ex = new SubunitServiceException(cause);
        assertEquals(cause, ex.getCause());
    }
}
