package club.ss220.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class InetUtils {

    private InetUtils() {
    }

    public static long toCompactIPv4(Inet4Address address) {
        byte[] byteAddress = address.getAddress();
        return ((long) (byteAddress[0] & 0xFF) << 24)
               | ((byteAddress[1] & 0xFF) << 16)
               | ((byteAddress[2] & 0xFF) << 8)
               | (byteAddress[3] & 0xFF);
    }

    public static Inet4Address fromCompactIPv4(long compact) throws UnknownHostException {
        byte[] byteAddress = new byte[] {
                (byte) ((compact >> 24) & 0xFF),
                (byte) ((compact >> 16) & 0xFF),
                (byte) ((compact >> 8) & 0xFF),
                (byte) (compact & 0xFF)
        };
        return (Inet4Address) InetAddress.getByAddress(byteAddress);
    }
}
