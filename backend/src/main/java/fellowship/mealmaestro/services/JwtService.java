package fellowship.mealmaestro.services;

import java.security.Key;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static Dotenv dotenv = Dotenv.load();

    private static final String SIGNING_KEY = dotenv.get("JWT_SECRET");
    
    public String extractUserEmail(String token){
        
        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    private Key getSignInKey() {
        byte[] signingKeyBytes = Decoders.BASE64.decode(SIGNING_KEY);
        return Keys.hmacShaKeyFor(signingKeyBytes);
    }
}
