package in.mpapp.springsecurityjwtdemo.controllers;

import in.mpapp.springsecurityjwtdemo.dtos.UserDTO;
import in.mpapp.springsecurityjwtdemo.models.requests.RegistrationRequest;
import in.mpapp.springsecurityjwtdemo.models.responses.RegisteredResponse;
import in.mpapp.springsecurityjwtdemo.services.IUserService;
import in.mpapp.springsecurityjwtdemo.utils.DataMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.mpapp.springsecurityjwtdemo.models.responses.*;
import in.mpapp.springsecurityjwtdemo.utils.ControllersUtils;

@RestController
@RequestMapping(value = "registrations")
@Slf4j
public class RegistrationController {

    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<?> register(
            @Validated @RequestBody final RegistrationRequest registrationRequest) {
        log.info("Received Registration Request C: {} ", registrationRequest.getUsername());
        UserDTO userDTO = DataMapperUtil.convertTo(registrationRequest, UserDTO.class);
        userDTO = userService.createUser(userDTO);
        log.info("Created User C: {} ", userDTO);
        ResponseGenerico<RegisteredResponse> response1 = new ResponseGenerico<>();
        final RegisteredResponse response = DataMapperUtil.convertTo(userDTO, RegisteredResponse.class);
        return ControllersUtils.repuestaGenericoExitoObject(response1, response);
        //return ResponseEntity.ok(response);
    }
}
