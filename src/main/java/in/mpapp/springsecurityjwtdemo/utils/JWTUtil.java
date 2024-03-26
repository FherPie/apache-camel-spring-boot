package in.mpapp.springsecurityjwtdemo.utils;

import in.mpapp.springsecurityjwtdemo.dtos.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;

import java.io.Serializable;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiry}")
	private Long expiry;

	@Value("${jwt.authority.key}")
	private String authorityKey;

	private Key key;

	@PostConstruct
	public void init() {
		final byte[] signingKeyBytes = Base64.getDecoder().decode(secret);
		key = new SecretKeySpec(signingKeyBytes, 0, signingKeyBytes.length, SignatureAlgorithm.HS256.getJcaName());

	}

	public String getUsername(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDate(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	};

	private boolean isExpired(String token) {
		final Date expirationDate = getExpirationDate(token);
		return expirationDate.before(new Date());
	}

	public String generateToken(final UserDTO userDTO) {
		String authorities = userDTO.getRole().toString();

		return Jwts.builder().setSubject(userDTO.getUsername()).claim(authorityKey, authorities).signWith(key)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiry)).compact();
	}

//	public boolean isTokenValid(String token, UserDetails userDetails) {
//		final String username = getUsername(token);
//		return (username.equals(userDetails.getUsername()) && !isExpired(token));
//	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = getUsername(token);
		return (username.equals(userDetails.getUsername()) && !isExpired(token));
	}
	
	
	public Authentication getAuthenticationToken(final String token, final UserDetails userDetails) {
		final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
		final Claims claims = claimsJws.getBody();
		final Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(authorityKey).toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
          return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
}


//	public Authentication getAuthenticationToken(final String token, final UserDTO userDTO,
//			final UserDetails userDetails) {
//		final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
//
//		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
//
//		final Claims claims = claimsJws.getBody();
//
//		final Collection<? extends GrantedAuthority> authorities = Arrays
//				.stream(claims.get(authorityKey).toString().split(",")).map(SimpleGrantedAuthority::new)
//				.collect(Collectors.toList());
//
//		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
//	}

//  public String generateToken(Authentication authentication) {
//  String authorities = authentication.get .getAuthorities().stream()
//          .map(GrantedAuthority::getAuthority)
//          .collect(Collectors.joining(","));
//  
//  return Jwts.builder()
//          .setSubject(authentication.getName())
//          .claim(authoritiesKey, authorities)
//          .signWith(key)
//          .setIssuedAt(new Date())
//          .setExpiration(new Date(System.currentTimeMillis() + tokenExpires))
//          .compact();
//}
	/*
	 * jwt utility -------> parsing token
	 */
//    public boolean validateToken(final String token, final UserDTO userDTO) {
//        log.info("Validating Token for User Details: {} ", token, userDTO);
//        Jwts.parserBuilder()
//    }

//  public Authentication getAuthenticationToken(final String token, final Authentication authentication, final UserDetails userDetails) {
//  final JwtParser jwtParser = Jwts.parserBuilder()
//          .setSigningKey(key)
//          .build();
//  
//  final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
//
//  final Claims claims = claimsJws.getBody();
//
//  final Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(authoritiesKey).toString().split(","))
//          .map(SimpleGrantedAuthority::new)
//          .collect(Collectors.toList());
//  
//  return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
//}

	/*
	 * 1 jwt utility -----> Creating token
	 */
//public TokenDTO generateToken(final UserDTO userDTO) {
//   final Map<String, Object> claims = new HashMap<>();
//   claims.put("account", userDTO.getId());
//   final LocalDateTime currentTime = LocalDateTime.now();
//   log.info("Current Local Date Time: {} ", currentTime);
//   final LocalDateTime expiryLDT = currentTime.plus(expiry, ChronoUnit.MINUTES);
//   log.info("Expiry Local Date Time: {} ", expiryLDT);
//   // This line will basically convert Locale Date Time to Date
//   final Date expiryDate = Date.from(expiryLDT.atZone(ZoneId.systemDefault()).toInstant());
//   log.info("Local Date Time to Date: {} ", expiryLDT, " " , expiryDate);
//   final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//   final String token = Jwts.builder()
//           .setClaims(claims)
//           .setSubject(userDTO.getUsername())
//           .setExpiration(expiryDate)
//           .signWith(secretKey, SignatureAlgorithm.HS512)
//           .compact();
//   log.info("Generated Token: {} ", token);
//   return new TokenDTO(token, expiryLDT);
//}
//
}
