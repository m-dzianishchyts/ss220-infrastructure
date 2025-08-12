package club.ss220.storage.central.spring.web.v1.exception;

import org.springframework.dao.DataAccessException;

public class CentralApiException extends DataAccessException {

    public CentralApiException(String msg) {
        super(msg);
    }

    public CentralApiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
