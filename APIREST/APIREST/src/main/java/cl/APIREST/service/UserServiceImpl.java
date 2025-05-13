package cl.APIREST.service;

import cl.APIREST.dto.*;
import cl.APIREST.entity.*;
import cl.APIREST.exception.EmailAlreadyRegisteredException;
import cl.APIREST.exception.InvalidEmailFormatException;
import cl.APIREST.repository.UserRepository;
import cl.APIREST.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        // Validar email existente
      if (userRepository.existsByEmail(request.getEmail())){
          throw new EmailAlreadyRegisteredException("El correo ya registrado");
      }

        // Validar formato del email
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new InvalidEmailFormatException("formato incorrecto");
        }

        // Crear usuario
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);
        user.setActive(true);

        String token = jwtUtil.generateToken(request.getEmail());
        user.setToken(token);

        // Mapear teléfonos
        List<Phone> phones = request.getPhones().stream().map(p -> {
            Phone phone = new Phone();
            phone.setNumber(p.getNumber());
            phone.setCitycode(p.getCitycode());
            phone.setContrycode(p.getContrycode());
            phone.setUser(user);
            return phone;
        }).toList();

        user.setPhones(phones);

        // Guardar en BD
        User savedUser = userRepository.save(user);

        // Devolver respuesta
        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setCreated(savedUser.getCreated());
        response.setModified(savedUser.getModified());
        response.setLastLogin(savedUser.getLastLogin());
        response.setToken(savedUser.getToken());
        response.setActive(savedUser.isActive());

        return response;
    }
}