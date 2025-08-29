package club.ss220.core.util;

public final class ByondUtils {

    private ByondUtils() {
    }

    public static String sanitizeCkey(String ckey) {
        if (ckey == null) {
            return null;
        }
        
        return ckey.toLowerCase().replaceAll("[^a-z0-9_]", "");
    }
}
