package com.mk.usermanagement.service;

import com.mk.usermanagement.dto.*;
import com.mk.usermanagement.entity.Role;
import com.mk.usermanagement.entity.RoleName;
import com.mk.usermanagement.entity.User;
import com.mk.usermanagement.exception.UsernameAlreadyExistsException;
import com.mk.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void register(UserRegisterRequest registerRequest) {

        // sprawdź czy uzytkownik z takim username albo email już istnieje,
        // jeśli tak - wyrzuć wyjatęk
        if (userRepository.existsByUsername(registerRequest.username()) || userRepository.existsByEmail(registerRequest.email())) {
            throw new UsernameAlreadyExistsException("User already exists");
        }

        // przypisz nowemu użytkownikowi domyślną rolę - ROLE_USER
        Role role = roleService.findByName(String.valueOf(RoleName.ROLE_USER));

        // stwórz nowy obiekt użytkownika
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        // BCrypt przekazanego hasła
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEnabled(true);
        user.setRoles(Set.of(role));

        //TODO: replace with save(user) once its implemented
        // zapisz użytkownika do bazy danych
        userRepository.save(user);
    }

    @Override
    public AuthResponseDto login(UserLoginRequest loginRequest) {

        // tworzenie obiektu uwierzytelniającego na podstawie przekazanego username oraz password na endpoincie /login
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()));

        // zapisz uwierzytelnionego usera do kontekstu Springa
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // wygeneruj token na podstawie usera w authentication
        String token = jwtService.generateToken(authentication);
        // stwórz usera na podstawie głownego uzytkownika
        var user = (UserDetails) authentication.getPrincipal();

        // wyciągnij przypisane role do użytkownika
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // zwróć DTO z tokenem, username oraz rolami
        return new AuthResponseDto(token, user.getUsername(), roles);
    }

    @Override
    @Transactional
    public void createUserByAdmin(CreateUserRequest request) {
        // sprawdź czy w bazie istnieje już user z przekazanym username lub email
        if (userRepository.existsByUsername(request.username()) || userRepository.existsByEmail(request.email())) {
            throw new UsernameAlreadyExistsException("Username or email is already taken");
        }

        // wyciągnij przekazane role (upewniając się że takie role istnieją w bazie) i zapisz je do zbioru roles
        Set<Role> roles = request.roles().stream()
                .map(roleName -> roleService.findByName(roleName.toUpperCase()))
                .collect(Collectors.toSet());

        System.out.println(roles);

        /*// stwórz nowego użytkownika na podstawie przekazanych danych z requestu
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(roles);
        user.setEnabled(true);

        // zapisz nowego użytkownika
        userRepository.save(user);*/
    }

    @Override
    public UserDto findById(Long id) {

        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        Set<String> roles = dbUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserDto(
                dbUser.getId(),
                dbUser.getUsername(),
                dbUser.getEmail(),
                roles
        );
    }

    @Override
    public List<UserDto> findAll() {
        // TODO: Implement
        return List.of();
    }

    @Override
    public void deleteById(Long id) {
        // TODO: Implement

    }

    @Override
    public UserDto update(User user) {
        // TODO: Implement
        return null;
    }
}
