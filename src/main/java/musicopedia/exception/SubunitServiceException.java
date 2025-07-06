package musicopedia.exception;

public class SubunitServiceException extends RuntimeException {
    public SubunitServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public SubunitServiceException(Throwable cause) {
        super(cause);
    }
}
