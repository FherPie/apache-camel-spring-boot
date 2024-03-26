package in.mpapp.springsecurityjwtdemo.models.responses;

import in.mpapp.springsecurityjwtdemo.enums.AccountStatus;
import in.mpapp.springsecurityjwtdemo.enums.RoleAuthority;
import lombok.Data;

@Data
public class RegisteredResponse implements IResponse{

    private String firstName;
    private String lastName;
    private RoleAuthority role;
    private AccountStatus status;
    private String username;
}
