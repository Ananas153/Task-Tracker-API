package nam.nam.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import nam.nam.exception.user.InvalidTokenException;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "super-secret-key-change-me";

    public static String JwtUtil(String email, int userID) {

        return JWT.create()
                .withSubject(email)
                .withClaim("UserID", userID)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Invalid or expired JWT token");
        }
    }

    public static int extractUserIdFromToken(String token){
        try{
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token)
                    .getClaim("UserID")
                    .asInt();
        }catch (JWTVerificationException e){
            throw new InvalidTokenException("Invalid or expired JWT token");
        }
    }
}
