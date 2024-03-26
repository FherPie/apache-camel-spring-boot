package in.mpapp.springsecurityjwtdemo.models.requests;

import in.mpapp.springsecurityjwtdemo.enums.RoleAuthority;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {

	
	private Long id;
    @NotNull(message = "First Name cannot be null")
    @NotEmpty(message = "First tName cannot be empty")
    @Size(min = 3, max = 100, message = "First Name should be within 3 to 100 characters long")
    private String firstName;

    @NotEmpty(message = "Last Name cannot be empty")
    @Size(min = 3, max = 100, message = "Last Name should be within 3 to 100 characters long")
    private String lastName;

    //@NotNull(message = "Role cannot be null")
    private RoleAuthority role;

    @NotNull(message = "username cannot be null")
    @NotEmpty(message = "username cannot be empty")
    @Size(min = 1, message = "username should be at least 1 character long")
    private String username;

    @NotNull(message = "password cannot be null")
    @NotEmpty(message = "password cannot be empty")
    @Size(min = 1, max = 100, message = "password should be at least 8 characters long")
    private String password;

}
