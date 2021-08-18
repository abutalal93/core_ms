package restaurant.ms.core.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import restaurant.ms.core.dto.requests.SpLoginRq;
import restaurant.ms.core.dto.responses.SpLoginRs;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.entities.SpUser;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.RestaurantUserRepo;
import restaurant.ms.core.repositories.SpUserRepo;
import restaurant.ms.core.services.SpUserService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

@Component
public class JwtTokenProvider {

    @Value("${secret.key}")
    private String secretKey;

    @Value("${secret.timeout}")
    private long validityInMilliseconds;

    @Autowired
    private SpUserRepo spUserRepo;

    @Autowired
    private RestaurantUserRepo restaurantUserRepo;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(JwtUser jwtUser) {

        Claims claims = Jwts.claims().setSubject(jwtUser.getUsername());
        claims.put("userType", jwtUser.getUserType());
        claims.put("id", jwtUser.getId());

        Date now = new Date();

        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }

    public Authentication getAuthentication(String token, Locale locale) {
        UserDetails userDetails = getUserDetail(token, locale);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    public UserDetails getUserDetail(String token, Locale locale) {

        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        String userType = claims.get("userType").toString();

        UserDetails userDetails = null;
        switch (userType) {
            case "SP":
                SpUser spUser = spUserRepo.findSpUserByUsername(claims.getSubject());
                if(spUser == null){
                    throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_not_found",locale);
                }

                JwtUser spJwtUser = new JwtUser(spUser.getId(), spUser.getUsername(), spUser.getPassword(), "SP", "SUPER_ADMIN");
                userDetails = new CustomUserDetails(spJwtUser);
                break;
            case "REST":
                RestaurantUser restaurantUser = restaurantUserRepo.findRestaurantUserByUsername(claims.getSubject());
                if(restaurantUser == null){
                    throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_not_found",locale);
                }

                JwtUser restJwtUser = new JwtUser(restaurantUser.getId(), restaurantUser.getUsername(), restaurantUser.getPassword(), "REST", restaurantUser.getRestaurantUserType().name());
                userDetails = new CustomUserDetails(restJwtUser);
                break;
        }

        if (userDetails == null) {
            throw new HttpServiceException(HttpStatus.UNAUTHORIZED, "invalid_login", locale);
        }

        return userDetails;
    }

    public String resolveToken(HttpServletRequest req, Locale locale) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token, Locale locale) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new HttpServiceException(HttpStatus.UNAUTHORIZED, "Expired or invalid JWT token");
        }
    }
}