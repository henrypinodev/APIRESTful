package cl.APIREST.service;

import cl.APIREST.dto.UserRegistrationRequest;
import cl.APIREST.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRegistrationRequest request);
}
