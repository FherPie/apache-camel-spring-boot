package in.mpapp.springsecurityjwtdemo.configurations;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import in.mpapp.springsecurityjwtdemo.dtos.UserPrincipal;
import in.mpapp.springsecurityjwtdemo.services.impls.UserPrincipalService;
import in.mpapp.springsecurityjwtdemo.utils.AuthTokenFilter;
import in.mpapp.springsecurityjwtdemo.utils.JWTUtil;
import in.mpapp.springsecurityjwtdemo.utils.UnauthorizedEntryPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfiguration {


    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JWTUtil tokenProvider) throws Exception {
        http.httpBasic().and().cors().and().csrf().disable()
            .authorizeHttpRequests()
            //.antMatchers("/auth/**").permitAll()
            .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
            .anyRequest().authenticated().and()
            .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(new AuthTokenFilter(tokenProvider, userDetailsService()), UsernamePasswordAuthenticationFilter.class);
        http.logout(logout -> logout.permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                }));
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(UserPrincipalService userService, BCryptPasswordEncoder passwordEncoder) throws Exception {
        return authentication -> {
            String username = authentication.getPrincipal().toString();
            String password = authentication.getCredentials().toString();
            UserPrincipal user = (UserPrincipal) userService.loadUserByUsername(username);
            log.info("Credentials password: {}", password);
            log.info("User :: {}", user);

            return new UsernamePasswordAuthenticationToken(username, "", user.getAuthorities());
        };
    }
    
    
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Value("#{'${spring.cors.allowed-origins}'.split(',')}")
            private List<String> allowedOrigins;

            @Value("#{'${spring.cors.allowed-methods}'.split(',')}")
            private List<String> allowedMethods;

            @Value("#{'${spring.cors.allowed-headers}'.split(',')}")
            private List<String> allowedHeaders;

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins(allowedOrigins.toArray(new String[0]))
                    .allowedMethods(allowedMethods.toArray(new String[0]))
                    .allowedHeaders(allowedHeaders.toArray(new String[0]))
                    .allowCredentials(true);
            }
        };
    }
    
    
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserPrincipalService();
    }
    
    
    
    
//    @Bean
//    public ObjectMapper getMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        return objectMapper;
//    }
    
    

//    @Override
//    @Bean
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userPrincipalService).passwordEncoder(passwordEncoder);
//    }

//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
////        super.configure(http);
//        http.cors().and().csrf().disable()
//                .authorizeRequests()
////                .antMatchers(HttpMethod.POST,"/registrations").permitAll()
//                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
//                .anyRequest().authenticated()
//                .and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource(){
    //     CorsConfiguration configuration= new CorsConfiguration();
    //     configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    //     configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
