package in.mpapp.springsecurityjwtdemo.controllers;

import in.mpapp.springsecurityjwtdemo.dtos.TokenDTO;
import in.mpapp.springsecurityjwtdemo.dtos.UserDTO;
import in.mpapp.springsecurityjwtdemo.enums.AccountStatus;
import in.mpapp.springsecurityjwtdemo.models.requests.AuthenticationRequest;
import in.mpapp.springsecurityjwtdemo.models.requests.RegistrationRequest;
import in.mpapp.springsecurityjwtdemo.services.IUserService;
import in.mpapp.springsecurityjwtdemo.utils.DataMapperUtil;
import in.mpapp.springsecurityjwtdemo.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import in.mpapp.springsecurityjwtdemo.models.responses.*;
import in.mpapp.springsecurityjwtdemo.utils.ControllersUtils;

@RestController
@RequestMapping()
@Slf4j
public class HomeController {

	/*
	 * What ever username and password we are sending to server we can verify them
	 * manually. (encode and decode and then match and all). But this thing is
	 * already doing by AuthenticationManager that's why we injected this
	 * 
	 * It is a security layer which doesn't understand anything about DTO, model,
	 * Entity It only understands One class and that is
	 * UsernamePasswordAuthenticationToken
	 * 
	 * That's we created a new UsernamePasswordAuthenticationToken object where
	 * we're manually converting the username and password which is sent to the
	 * AuthenticationRequest
	 * 
	 * then we are passing that converted token to the AuthenticationManager This
	 * guy will internally authenticate the username and password
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/*
	
	 */
	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private IUserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/empleados/{username}")
	public ResponseEntity<?> getEmpleado(@PathVariable String username) {
		ResponseGenerico<UserDTO> response = new ResponseGenerico<>();
		UserDTO dto = userService.getByUsername(username).get();
		return ControllersUtils.repuestaGenericoExitoObject(response, dto);
	}

	@GetMapping("/empleados")
	public ResponseEntity<?> hello() {
		return ResponseEntity.ok(userService.getAll());
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@Validated @RequestBody final AuthenticationRequest authenticationRequest) {

		log.info("Received authentication request C: {} ", authenticationRequest.getUsername());
		Optional<UserDTO> user = userService.getByUsername(authenticationRequest.getUsername());
		
		
		if (user.isEmpty()) {
			return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);

		} else {
			log.info("User :: {}", user);
			if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.get().getPassword()) || user.get().getStatus()!=AccountStatus.ACTIVE) {
				return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
			}

		}

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtUtil.generateToken(user.get());
		user.get().setPassword("");
		TokenDTO tokenDto = new TokenDTO(token, null, user.get());

		log.info("Generated Token DTO: ", token);
		final AuthenticatedTokenResponse response = DataMapperUtil.convertTo(tokenDto,
				AuthenticatedTokenResponse.class);
		log.info("response ", response);
		return ResponseEntity.ok(response);

	}

	// TODO **
	@PutMapping("/empleados")
	public ResponseEntity<?> update(@Validated @RequestBody final RegistrationRequest registrationRequest) {
		log.info("Received Registration Request C: {} ", registrationRequest.getUsername());
		UserDTO userDTO = DataMapperUtil.convertTo(registrationRequest, UserDTO.class);
		userDTO = userService.updateUser(userDTO);
		ResponseGenerico<RegisteredResponse> response1 = new ResponseGenerico<>();
		final RegisteredResponse response = DataMapperUtil.convertTo(userDTO, RegisteredResponse.class);
		return ControllersUtils.repuestaGenericoExitoObject(response1, response);

	};

	@DeleteMapping("/empleados/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		ResponseGenerico<RegisteredResponse> response1 = new ResponseGenerico<>();
		return ControllersUtils.repuestaGenericoExitoObject(response1, userService.inactivateUser(id));

	};
	
	@PutMapping("/activeempleados/{id}")
	public ResponseEntity<?> activeempleados(@PathVariable Long id) {
		ResponseGenerico<RegisteredResponse> response1 = new ResponseGenerico<>();
		return ControllersUtils.repuestaGenericoExitoObject(response1, userService.activateUser(id));
	};
	
}

//
//final UsernamePasswordAuthenticationToken token =
//        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
/*
 * This part is doing the authentication task
 */
//try {
//    Authentication authentication = authenticationManager.authenticate(token);
//} catch (final Exception e) {
//
//}
//catch (final DisabledException | LockedException | BadCredentialsException e) {
//    log.error("Error Authenticating Username: {} ", authenticationRequest.getUsername());
//    if(e instanceof BadCredentialsException) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//    } else {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//    }
//}
/*
 * If authentication succeed then will be geting the userDetails in the UserDTO
 */
//UserDTO userDTO = userService.getByUsername(authenticationRequest.getUsername())
//        .orElseThrow(() -> new RuntimeException(("User with username: " + authenticationRequest.getUsername() + " not found")));

/*
 * In JWTUtil we are passing the user object and It will generate a TokenDTO
 * which contains two things 1. token, 2. expiry
 * 
 */
//final TokenDTO tokenDTO = jwtUtil.generateToken(userDTO);
