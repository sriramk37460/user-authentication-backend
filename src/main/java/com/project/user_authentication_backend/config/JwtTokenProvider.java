package com.project.user_authentication_backend.config;


import com.project.user_authentication_backend.dao.UserRepository;
import com.project.user_authentication_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    @Autowired
    UserRepository userRepository;

    @Value("${SALT_SECRET}")
    private String SECRET_KEY;

    private Date calculateNextMonthEndDate(){
        LocalDate endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        return Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private final Date VALIDITY_IN_MONTHS = calculateNextMonthEndDate();

    public String createToken(int userId,String userName,String role){
        Date now = new Date();
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.YEAR,10);
        Date adminExpiration = calendar.getTime();
        Claims claims=Jwts.claims()
                .setIssuer(userName)
                .setSubject(Integer.toString(userId))
                .setIssuedAt(now);

        claims.put("userName",userName);
        claims.put("userId",Integer.toString(userId));
        claims.put("role",role);

        Optional<User> user= userRepository.findById(userId);

        if(user.isPresent()){
            if(user.get().isAdmin())
            {
                claims.setExpiration(adminExpiration);
            }
            else {
                claims.setExpiration(VALIDITY_IN_MONTHS);
            }
        }
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        System.out.println("EXTRACT ..");
        return claimsResolver.apply(claims);
    }

    // Extract user ID from token
    public String extractUserId(String token) {
        if(token != null && token.startsWith("Bearer "))
        {
            token = token.substring(7);
        }
        System.out.println("EXTRACT USER ID..");
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    // Extract role from token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Extract username (issuer) from token
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("userName", String.class));
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authenticate");
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
