package nam.nam.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "super-secret-key-change-me";

    public static String JwtUtil(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .sign(Algorithm.HMAC256(SECRET));
    }
}
