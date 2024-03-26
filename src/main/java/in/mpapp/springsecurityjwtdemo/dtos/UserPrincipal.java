package in.mpapp.springsecurityjwtdemo.dtos;

import in.mpapp.springsecurityjwtdemo.enums.AccountStatus;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserPrincipal implements UserDetails {

    private final UserDTO userDTO;

    public UserPrincipal(final UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userDTO.getRole().name()));
    }

    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDTO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !AccountStatus.INACTIVE.equals((userDTO.getStatus()));
    }

    @Override
    public boolean isAccountNonLocked() {
        return !AccountStatus.SUSPENDED.equals(userDTO.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return AccountStatus.ACTIVE.equals(userDTO.getStatus());
    }
}
