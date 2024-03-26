package in.mpapp.springsecurityjwtdemo.models.responses;

import lombok.Data;

import java.time.LocalDateTime;

import in.mpapp.springsecurityjwtdemo.dtos.UserDTO;

@Data
public class AuthenticatedTokenResponse implements IResponse {

    private String token;
    private LocalDateTime expiry;
    private UserDTO user;

}
