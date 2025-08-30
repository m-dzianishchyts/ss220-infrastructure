package club.ss220.core.util;

public final class StringUtils {

    private StringUtils() {
    }

    public static String truncate(String str, int length) {
        return truncate(str, length, "...");
    }

    public static String truncate(String str, int length, String suffix) {
        return str.length() > length ? str.substring(0, length) + suffix : str;
    }
}
