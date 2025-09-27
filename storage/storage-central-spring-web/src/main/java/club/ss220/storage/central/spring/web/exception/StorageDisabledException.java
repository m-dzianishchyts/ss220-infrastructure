package club.ss220.storage.central.spring.web.exception;

public class StorageDisabledException extends RuntimeException {

    public StorageDisabledException(Class<?> spi) {
        super(buildMessage(spi));
    }

    private static String buildMessage(Class<?> spi) {
        return "Storage is unavailable: " + spi.getSimpleName() + "."
               + " No providers discovered for " + spi.getName() + "."
               + " Ensure the appropriate adapter module is on the classpath and configured.";
    }
}
