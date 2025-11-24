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
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${JWT_SECRET}")
    private String secret_key;

    // Get Signature Key
    private SecretKey getSignInKey(){
        //Change secret key text into a real security key (make a SecretKey object to sign JWT tokens.)
        byte[] decode = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(decode);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token){
        // Takes a JWT token, checks if it is valid, then reads all information inside the token (claims)
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract specific claim from token
    private <T> T extractClaims(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extract Username
    private String extractUserEmail(String token){
        return extractClaims(token, Claims::getSubject);
    }

    // Generate JWT Token
    public String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .claims()
                .add(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .and()
                .signWith(getSignInKey())
                .compact()
                ;
    }

    // Validate token
    private boolean isTokenValid(String token, UserDetails userDetails){
        final String userEmail = extractUserEmail(token);
        return (userDetails.getUsername().equals(userEmail) && !isTokenExpired(token));
    }

    // Check if token expired
    private boolean isTokenExpired(String token){
        // Checks if the token’s expiration date is before now
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date
    private Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }
}
