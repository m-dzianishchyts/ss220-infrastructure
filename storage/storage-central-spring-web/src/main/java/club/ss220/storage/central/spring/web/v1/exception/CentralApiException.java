package club.ss220.storage.central.spring.web.v1.exception;

public class CentralApiException extends RuntimeException {

    public CentralApiException(String msg) {
        super(msg);
    }

    public CentralApiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
