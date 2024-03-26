package in.mpapp.springsecurityjwtdemo.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import in.mpapp.springsecurityjwtdemo.constants.Constants;
import in.mpapp.springsecurityjwtdemo.dtos.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthTokenFilter extends GenericFilterBean {
	
	private final UserDetailsService userService;
	private final JWTUtil jwtUtil;
	
	public AuthTokenFilter(JWTUtil jwtUtil, UserDetailsService userDetailsService ) {
		this.userService =  userDetailsService;
		this.jwtUtil = jwtUtil;
	}



	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	        String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
	        String token = null;
	        String username = null;
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
	        if (authorization != null && authorization.startsWith(Constants.BEARER)) {
	            token = authorization.replace(Constants.BEARER, "");

	            try {
	                username = jwtUtil.getUsername(token);
	            } catch (IllegalArgumentException e) {
	                log.error("An error occured during getting username from token", e);
	            } catch (ExpiredJwtException e) {
	                log.error("Token is expired", e);
	            } catch (SignatureException e) {
	                log.error("Authentication Failed. Username or Password not valid.");
	            }
	        }
	        
	        if (username != null && authentication == null) {
	            UserPrincipal user = (UserPrincipal) userService.loadUserByUsername(username);


	            if ( user!=null && jwtUtil.isTokenValid(token, user)) {
	                Authentication authenticationToken = jwtUtil.getAuthenticationToken(token, user);

	                log.debug("Authenticated user: {}, setting security context", username);
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            }
	        }
	        chain.doFilter(request, response);
	        
	}

	
}
