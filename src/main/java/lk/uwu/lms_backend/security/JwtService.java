package lk.uwu.lms_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    @Value("${JWT_SECRET}")
    private String secret_key;

    // --- HELPER METHODS ---
    // Converts the secret key string into a SecretKey object for signing JWTs
    private SecretKey getSignInKey(){
        byte[] decode = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(decode);
    }

    // Extracts all claims (the payload) from the JWT token.
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract expiration date
    private Date extractExpiration(String token){
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    // Extract Username (Email)
    public String extractUserEmail(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    // Extracts the token from the Authorization header
    public String extractToken(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    // --- GENERATION METHODS ---
    // JWT Access Token
    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, TimeUnit.MINUTES.toMillis(30)); // 30 Minutes
    }

    // JWT Refresh Token
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, TimeUnit.DAYS.toMillis(30)); // 30 Days
    }

    // Build Token
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    // --- VALIDATION METHODS ---
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userEmail = extractUserEmail(token);
        return (userDetails.getUsername().equals(userEmail) && isTokenNotExpired(token));
    }

    public boolean isTokenNotExpired(String token){
        return !extractExpiration(token).before(new Date());
    }
}
