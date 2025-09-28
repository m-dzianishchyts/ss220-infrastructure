package club.ss220.port.actions.github.util;

import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public final class JwtUtils {

    private JwtUtils() {
    }

    @SneakyThrows
    public static String createJwt(String appId, Path keyPath, Duration jwtLifetime) {
        Instant now = Instant.now();
        PrivateKey key = loadPrivateKey(keyPath);

        return Jwts.builder()
                .issuedAt(Date.from(now))
                .issuer(appId)
                .signWith(key)
                .expiration(Date.from(now.plus(jwtLifetime)))
                .compact();
    }

    @SneakyThrows
    private static PrivateKey loadPrivateKey(Path keyPath) {
        byte[] keyBytes = Files.readAllBytes(keyPath);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
}
