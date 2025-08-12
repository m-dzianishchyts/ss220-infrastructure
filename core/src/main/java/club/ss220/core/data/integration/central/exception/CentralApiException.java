package club.ss220.core.data.integration.central.exception;

import org.springframework.dao.DataAccessException;

@Deprecated
public class CentralApiException extends DataAccessException {

    public CentralApiException(String msg) {
        super(msg);
    }

    public CentralApiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
