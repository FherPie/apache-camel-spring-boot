package in.mpapp.springsecurityjwtdemo.services.impls;

import in.mpapp.springsecurityjwtdemo.dtos.UserDTO;
import in.mpapp.springsecurityjwtdemo.dtos.UserPrincipal;
import in.mpapp.springsecurityjwtdemo.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
@Slf4j
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching User by username: {} ", username);
        final UserDTO userDTO = userService.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));
        log.info("Retrieved User: {} ", userDTO);
        return new UserPrincipal(userDTO);
    }
}
